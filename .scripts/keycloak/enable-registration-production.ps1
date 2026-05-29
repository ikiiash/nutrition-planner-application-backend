param()

$ErrorActionPreference = 'Stop'

$KeycloakUrl = "https://nutrition-planner.net/auth"
$AdminRealm  = "master"
$AdminUser   = "admin"
$AdminPassword = Read-Host "Keycloak admin password"
$RealmName   = "NUTRITION"
$ClientId    = "nutrition-planner-client"

Write-Host "Connecting to $KeycloakUrl ..."

$tokenResponse = Invoke-RestMethod -Method Post `
    -Uri "$KeycloakUrl/realms/$AdminRealm/protocol/openid-connect/token" `
    -ContentType 'application/x-www-form-urlencoded' `
    -Body @{
        client_id  = 'admin-cli'
        grant_type = 'password'
        username   = $AdminUser
        password   = $AdminPassword
    }

$token = $tokenResponse.access_token
if ([string]::IsNullOrWhiteSpace($token)) { throw "Failed to get admin token." }
Write-Host "Token OK."

$headers = @{ Authorization = "Bearer $token" }

function api_put($path, $body) {
    $json = $body | ConvertTo-Json -Depth 10 -Compress
    & curl.exe -s -X PUT -H "Authorization: Bearer $token" -H "Content-Type: application/json" --data $json "$KeycloakUrl$path"
}

# 1. Enable registration in realm settings
Write-Host "Enabling registration..."
api_put "/admin/realms/$RealmName" @{
    registrationAllowed         = $true
    registrationEmailAsUsername = $false
    verifyEmail                 = $false
}

# 2. Update client redirect URIs
Write-Host "Updating client redirect URIs..."
$clients = Invoke-RestMethod -Method Get -Uri "$KeycloakUrl/admin/realms/$RealmName/clients?clientId=$ClientId" -Headers $headers
$clientUuid = $clients[0].id
api_put "/admin/realms/$RealmName/clients/$clientUuid" @{
    clientId                   = $ClientId
    name                       = $ClientId
    enabled                    = $true
    publicClient               = $true
    standardFlowEnabled        = $true
    directAccessGrantsEnabled  = $true
    redirectUris               = @("http://localhost:4200/*", "https://nutrition-planner.net/*")
    webOrigins                 = @("http://localhost:4200", "https://nutrition-planner.net")
}

Write-Host ""
Write-Host "Done. Registration is now enabled on https://nutrition-planner.net"
