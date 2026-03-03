import urllib.request
import json

def lambda_handler(event, context):
    print(f"event: {event}")

    for record in event['Records']:
        body_json = record['body']
        body = json.loads(body_json)

        url = body.get('url')

        if url is not None:
            registrar_usuario(url, body)
        else:
            application = body.get('application')
            print(f"Aplicação desconhecida: {application}")

def registrar_usuario(url, body):

    application = body.get('application')
    authorization = body['authorization'] # referente a permissao de registrar na aplicação
    access_token = body['access_token'] # referente a descoberta do usuario

    url = f"http://{url}/{application}/users/register/{access_token}"

    json_data = json.dumps(body.get('payload')).encode("utf-8")

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
        print(f"Err {e.code}: {error_body}")
        raise Exception("Falha ao registrar mantendo na fila")
