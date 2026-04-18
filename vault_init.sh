export VAULT_TOKEN=root-token-2026
export VAULT_ADDR=http://localhost:8200

vault kv put secret/jwt secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
vault kv put secret/database password=secret_password   # если хотите хранить пароль БД в Vault