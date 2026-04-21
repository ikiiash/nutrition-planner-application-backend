#!/usr/bin/env bash
set -euo pipefail

KEYCLOAK_URL="${KEYCLOAK_URL:-http://localhost:8081}"
ADMIN_REALM="${ADMIN_REALM:-master}"
ADMIN_USER="${ADMIN_USER:-admin}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-admin}"
REALM_NAME="${REALM_NAME:-NUTRITION}"
CLIENT_ID="${CLIENT_ID:-nutrition-planner-client}"
CLIENT_SECRET="${CLIENT_SECRET:-nutrition-planner-client-secret}"
FORCE_RECREATE_REALM="${FORCE_RECREATE_REALM:-true}"

command -v curl >/dev/null
command -v jq >/dev/null

ACCESS_TOKEN="$(
  curl -fsS -X POST "${KEYCLOAK_URL}/realms/${ADMIN_REALM}/protocol/openid-connect/token" \
    -H 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'client_id=admin-cli' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode "username=${ADMIN_USER}" \
    --data-urlencode "password=${ADMIN_PASSWORD}" | jq -r '.access_token'
)"

api_get() { curl -fsS "${KEYCLOAK_URL}$1" -H "Authorization: Bearer ${ACCESS_TOKEN}" -H 'Content-Type: application/json'; }
api_post() { curl -fsS -X POST "${KEYCLOAK_URL}$1" -H "Authorization: Bearer ${ACCESS_TOKEN}" -H 'Content-Type: application/json' --data "$2" >/dev/null; }
api_put() { curl -fsS -X PUT "${KEYCLOAK_URL}$1" -H "Authorization: Bearer ${ACCESS_TOKEN}" -H 'Content-Type: application/json' --data "$2" >/dev/null; }
api_delete() {
  if [[ -n "${2:-}" ]]; then
    curl -fsS -X DELETE "${KEYCLOAK_URL}$1" -H "Authorization: Bearer ${ACCESS_TOKEN}" -H 'Content-Type: application/json' --data "$2" >/dev/null
  else
    curl -fsS -X DELETE "${KEYCLOAK_URL}$1" -H "Authorization: Bearer ${ACCESS_TOKEN}" -H 'Content-Type: application/json' >/dev/null
  fi
}

status="$(curl -sS -o /dev/null -w '%{http_code}' -H "Authorization: Bearer ${ACCESS_TOKEN}" "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}")"
if [[ "$status" == "200" && "$FORCE_RECREATE_REALM" == "true" ]]; then
  api_delete "/admin/realms/${REALM_NAME}"
  status="404"
fi
if [[ "$status" == "404" ]]; then
  api_post "/admin/realms" "$(jq -n --arg realm "$REALM_NAME" '{realm:$realm, enabled:true}')"
fi

for role in ADMIN USER PREMIUM_USER; do
  role_status="$(curl -sS -o /dev/null -w '%{http_code}' -H "Authorization: Bearer ${ACCESS_TOKEN}" "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles/${role}")"
  if [[ "$role_status" == "404" ]]; then
    api_post "/admin/realms/${REALM_NAME}/roles" "$(jq -n --arg name "$role" '{name:$name}')"
  fi
done

client_uuid="$(api_get "/admin/realms/${REALM_NAME}/clients?clientId=${CLIENT_ID}" | jq -r '.[0].id // empty')"
client_payload="$(jq -n --arg clientId "$CLIENT_ID" --arg secret "$CLIENT_SECRET" '{clientId:$clientId,name:$clientId,enabled:true,protocol:"openid-connect",publicClient:false,clientAuthenticatorType:"client-secret",secret:$secret,standardFlowEnabled:true,directAccessGrantsEnabled:true,serviceAccountsEnabled:false,implicitFlowEnabled:false,redirectUris:["*"],webOrigins:["*"],attributes:{"post.logout.redirect.uris":"*"}}')"
if [[ -z "$client_uuid" ]]; then
  api_post "/admin/realms/${REALM_NAME}/clients" "$client_payload"
  client_uuid="$(api_get "/admin/realms/${REALM_NAME}/clients?clientId=${CLIENT_ID}" | jq -r '.[0].id // empty')"
else
  api_put "/admin/realms/${REALM_NAME}/clients/${client_uuid}" "$client_payload"
fi

ensure_user() {
  username="$1"; password="$2"; role="$3"; first_name="$4"; last_name="$5"
  user_id="$(api_get "/admin/realms/${REALM_NAME}/users?username=${username}&exact=true" | jq -r '.[0].id // empty')"
  payload="$(jq -n --arg username "$username" --arg email "$username" --arg firstName "$first_name" --arg lastName "$last_name" '{username:$username,email:$email,firstName:$firstName,lastName:$lastName,enabled:true,emailVerified:true,requiredActions:[]}')"
  if [[ -z "$user_id" ]]; then
    api_post "/admin/realms/${REALM_NAME}/users" "$payload"
    user_id="$(api_get "/admin/realms/${REALM_NAME}/users?username=${username}&exact=true" | jq -r '.[0].id // empty')"
  else
    api_put "/admin/realms/${REALM_NAME}/users/${user_id}" "$payload"
  fi
  api_put "/admin/realms/${REALM_NAME}/users/${user_id}/reset-password" "$(jq -n --arg value "$password" '{type:"password",temporary:false,value:$value}')"
  current_roles="$(api_get "/admin/realms/${REALM_NAME}/users/${user_id}/role-mappings/realm")"
  if [[ "$(jq 'length' <<<"$current_roles")" -gt 0 ]]; then
    api_delete "/admin/realms/${REALM_NAME}/users/${user_id}/role-mappings/realm" "$current_roles"
  fi
  role_repr="$(api_get "/admin/realms/${REALM_NAME}/roles/${role}")"
  api_post "/admin/realms/${REALM_NAME}/users/${user_id}/role-mappings/realm" "[$role_repr]"
}

ensure_user admin@nutrition.local admin123 ADMIN System Admin
ensure_user user@nutrition.local user123 USER Basic User
ensure_user premium@nutrition.local premium123 PREMIUM_USER Premium User

echo "Realm: ${REALM_NAME}"
echo "Client ID: ${CLIENT_ID}"
echo "- admin@nutrition.local / admin123 / ADMIN"
echo "- user@nutrition.local / user123 / USER"
echo "- premium@nutrition.local / premium123 / PREMIUM_USER"
