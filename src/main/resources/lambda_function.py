import urllib.request
import urllib.error
import json


def lambda_handler(event, context):
    print(f"event: {event}")

    for record in event.get("Records", []):
        body = json.loads(record["body"])

        url = body.get("register_url")

        if url:
            registrar_usuario(url, body)
        else:
            application = body.get("name")
            print(f"Aplicação desconhecida: {application}")

    return {"statusCode": 200}


def registrar_usuario(url, body):

    authorization = body["authorization"]

    url = f"{url}/auth/register"

    payload = body.get("payload")

    if not payload:
        print("Payload vazio")
        return

    json_data = json.dumps(payload).encode("utf-8")

    headers = {
        "Authorization": authorization,
        "Content-Type": "application/json"
    }

    req = urllib.request.Request(
        url,
        data=json_data,
        method="POST",
        headers=headers
    )

    try:
        with urllib.request.urlopen(req) as response:
            resp_body = response.read()
            resp_json = json.loads(resp_body)
            print(f"Registrado: {resp_json}")

    except urllib.error.HTTPError as e:
        error_body = e.read().decode()
        print(f"Erro {e.code}: {error_body}")
        raise Exception("Falha ao registrar mantendo na fila")
