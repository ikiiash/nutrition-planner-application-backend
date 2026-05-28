param()

$ErrorActionPreference = 'Stop'

function Get-EnvOrDefault {
    param([string]$Name, [string]$DefaultValue)
    $value = [Environment]::GetEnvironmentVariable($Name)
    if ([string]::IsNullOrWhiteSpace($value)) { return $DefaultValue }
    return $value
}

function Get-BoolEnvOrDefault {
    param([string]$Name, [bool]$DefaultValue)
    $value = [Environment]::GetEnvironmentVariable($Name)
    if ([string]::IsNullOrWhiteSpace($value)) { return $DefaultValue }
    return @('1', 'true', 'yes', 'y', 'on') -contains $value.Trim().ToLowerInvariant()
}

$KeycloakUrl = (Get-EnvOrDefault -Name 'KEYCLOAK_URL' -DefaultValue 'http://localhost:8081').TrimEnd('/')
$AdminRealm = Get-EnvOrDefault -Name 'ADMIN_REALM' -DefaultValue 'master'
$AdminUser = Get-EnvOrDefault -Name 'ADMIN_USER' -DefaultValue 'admin'
$AdminPassword = Get-EnvOrDefault -Name 'ADMIN_PASSWORD' -DefaultValue 'admin'
$RealmName = Get-EnvOrDefault -Name 'REALM_NAME' -DefaultValue 'NUTRITION'
$ClientId = Get-EnvOrDefault -Name 'CLIENT_ID' -DefaultValue 'nutrition-planner-client'
$PreferredClientSecret = Get-EnvOrDefault -Name 'CLIENT_SECRET' -DefaultValue 'nutrition-planner-client-secret'
$ForceRecreateRealm = Get-BoolEnvOrDefault -Name 'FORCE_RECREATE_REALM' -DefaultValue $true

$Roles = @('ADMIN', 'USER', 'PREMIUM_USER')
$Users = @(
    @{ Username = 'admin@nutrition.local'; Password = 'admin123'; Role = 'ADMIN'; FirstName = 'System'; LastName = 'Admin' },
    @{ Username = 'user@nutrition.local'; Password = 'user123'; Role = 'USER'; FirstName = 'Basic'; LastName = 'User' },
    @{ Username = 'planner@nutrition.local'; Password = 'planner123'; Role = 'USER'; FirstName = 'Meal'; LastName = 'Planner' },
    @{ Username = 'premium@nutrition.local'; Password = 'premium123'; Role = 'PREMIUM_USER'; FirstName = 'Premium'; LastName = 'User' }
)

$script:AccessToken = $null
$script:ClientSecretValue = $null

function Get-AuthHeaders { @{ Authorization = "Bearer $($script:AccessToken)" } }
function ConvertTo-JsonBody { param($Payload) (ConvertTo-Json -InputObject $Payload -Depth 20 -Compress) }
function ConvertTo-Array { param($Value) if ($null -eq $Value) { @() } elseif ($Value -is [System.Array]) { $Value } else { @($Value) } }
function Invoke-CurlJsonRequest {
    param(
        [string]$Method,
        [string]$Path,
        $Payload = $null
    )

    $arguments = @('-s', '-X', $Method, '-H', "Authorization: Bearer $($script:AccessToken)")
    if ($null -ne $Payload) {
        $arguments += @('-H', 'Content-Type: application/json', '--data', (ConvertTo-JsonBody -Payload $Payload))
    }
    $arguments += "$KeycloakUrl$Path"

    $response = & curl.exe @arguments
    if ([string]::IsNullOrWhiteSpace($response)) {
        return $null
    }

    return $response
}

function Invoke-ApiGet { param([string]$Path) Invoke-RestMethod -Method Get -Uri "$KeycloakUrl$Path" -Headers (Get-AuthHeaders) }
function Invoke-ApiPost { param([string]$Path, $Payload) Invoke-CurlJsonRequest -Method 'POST' -Path $Path -Payload $Payload | Out-Null }
function Invoke-ApiPut { param([string]$Path, $Payload) Invoke-CurlJsonRequest -Method 'PUT' -Path $Path -Payload $Payload | Out-Null }
function Invoke-ApiDelete {
    param([string]$Path, $Payload = $null)
    Invoke-CurlJsonRequest -Method 'DELETE' -Path $Path -Payload $Payload | Out-Null
}

function Get-StatusCode {
    param([string]$Method, [string]$Path, $Payload = $null)
    $url = "$KeycloakUrl$Path"
    $headers = @('-H', "Authorization: Bearer $($script:AccessToken)")
    $arguments = @('-s', '-o', 'NUL', '-w', '%{http_code}', '-X', $Method) + $headers

    if ($null -ne $Payload) {
        $arguments += @('-H', 'Content-Type: application/json', '--data', (ConvertTo-JsonBody -Payload $Payload))
    }

    $arguments += $url
    $statusCode = & curl.exe @arguments
    return [int]$statusCode
}

function Get-AdminToken {
    $tokenResponse = Invoke-RestMethod -Method Post -Uri "$KeycloakUrl/realms/$AdminRealm/protocol/openid-connect/token" -ContentType 'application/x-www-form-urlencoded' -Body @{
        client_id  = 'admin-cli'
        grant_type = 'password'
        username   = $AdminUser
        password   = $AdminPassword
    }
    if ([string]::IsNullOrWhiteSpace($tokenResponse.access_token)) {
        throw 'Unable to obtain admin access token.'
    }
    $script:AccessToken = $tokenResponse.access_token
}

function Ensure-Realm {
    $status = Get-StatusCode -Method 'GET' -Path "/admin/realms/$RealmName"
    if ($status -eq 200 -and $ForceRecreateRealm) {
        Invoke-ApiDelete -Path "/admin/realms/$RealmName"
        $status = 404
        Start-Sleep -Seconds 2
    }
    if ($status -eq 404) {
        Invoke-ApiPost -Path '/admin/realms' -Payload @{ realm = $RealmName; enabled = $true }
        for ($attempt = 0; $attempt -lt 10; $attempt++) {
            Start-Sleep -Milliseconds 500
            if ((Get-StatusCode -Method 'GET' -Path "/admin/realms/$RealmName") -eq 200) {
                break
            }
        }
    }
}

function Ensure-RealmRole {
    param([string]$RoleName)
    $status = Get-StatusCode -Method 'GET' -Path "/admin/realms/$RealmName/roles/$([uri]::EscapeDataString($RoleName))"
    if ($status -eq 404) {
        Invoke-ApiPost -Path "/admin/realms/$RealmName/roles" -Payload @{ name = $RoleName }
    }
}

function Get-ClientUuid {
    $clients = ConvertTo-Array (Invoke-ApiGet -Path "/admin/realms/$RealmName/clients?clientId=$([uri]::EscapeDataString($ClientId))")
    if ($clients.Count -eq 0) { return $null }
    return $clients[0].id
}

function Ensure-Client {
    $clientUuid = Get-ClientUuid
    $payload = @{
        clientId = $ClientId; name = $ClientId; enabled = $true; protocol = 'openid-connect'
        publicClient = $true
        standardFlowEnabled = $true; directAccessGrantsEnabled = $true; serviceAccountsEnabled = $false
        implicitFlowEnabled = $false; redirectUris = @('http://localhost:4200/*', 'https://nutrition-planner.net/*'); webOrigins = @('http://localhost:4200', 'https://nutrition-planner.net')
        attributes = @{ 'post.logout.redirect.uris' = '*' }
    }
    if ([string]::IsNullOrWhiteSpace($clientUuid)) {
        Invoke-ApiPost -Path "/admin/realms/$RealmName/clients" -Payload $payload
        $clientUuid = Get-ClientUuid
    } else {
        Invoke-ApiPut -Path "/admin/realms/$RealmName/clients/$clientUuid" -Payload $payload
    }
    $script:ClientSecretValue = $null
}

function Get-UserId {
    param([string]$Username)
    $users = ConvertTo-Array (Invoke-ApiGet -Path "/admin/realms/$RealmName/users?username=$([uri]::EscapeDataString($Username))&exact=true")
    if ($users.Count -eq 0) { return $null }
    return $users[0].id
}

function Ensure-User {
    param([hashtable]$User)
    $userId = Get-UserId -Username $User.Username
    $payload = @{
        username = $User.Username; email = $User.Username; firstName = $User.FirstName; lastName = $User.LastName
        enabled = $true; emailVerified = $true; requiredActions = @()
    }
    if ([string]::IsNullOrWhiteSpace($userId)) {
        Invoke-ApiPost -Path "/admin/realms/$RealmName/users" -Payload $payload
        $userId = Get-UserId -Username $User.Username
    } else {
        Invoke-ApiPut -Path "/admin/realms/$RealmName/users/$userId" -Payload $payload
    }
    Invoke-ApiPut -Path "/admin/realms/$RealmName/users/$userId/reset-password" -Payload @{ type = 'password'; temporary = $false; value = $User.Password }
    $currentRoles = ConvertTo-Array (Invoke-ApiGet -Path "/admin/realms/$RealmName/users/$userId/role-mappings/realm")
    if ($currentRoles.Count -gt 0) {
        Invoke-ApiDelete -Path "/admin/realms/$RealmName/users/$userId/role-mappings/realm" -Payload $currentRoles
    }
    $roleRepresentation = Invoke-ApiGet -Path "/admin/realms/$RealmName/roles/$($User.Role)"
    Invoke-ApiPost -Path "/admin/realms/$RealmName/users/$userId/role-mappings/realm" -Payload @($roleRepresentation)
}

Get-AdminToken
Ensure-Realm
foreach ($role in $Roles) { Ensure-RealmRole -RoleName $role }
Ensure-Client
foreach ($user in $Users) { Ensure-User -User $user }

Write-Host "Realm: $RealmName"
Write-Host "Client ID: $ClientId"
Write-Host "- admin@nutrition.local / admin123 / ADMIN"
Write-Host "- user@nutrition.local / user123 / USER"
Write-Host "- planner@nutrition.local / planner123 / USER"
Write-Host "- premium@nutrition.local / premium123 / PREMIUM_USER"
