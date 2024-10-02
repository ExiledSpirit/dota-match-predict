import pickle
import os
import pandas as pd
import torch
from sentence_transformers import SentenceTransformer

embedding_model = SentenceTransformer(
    "dunzhang/stella_en_400M_v5",
    cache_folder="./models",
    device="cuda",
    trust_remote_code=True,
)
EMBEDDING_DIMENSIONS = 1024


def make_embedding(content: str):
    return embedding_model.encode(content)


regressors = {}


def classify(content: str):
    # carregar regressores se não estiverem carregados
    if not regressors:
        for filePath in os.listdir("./models"):
            if not filePath.endswith(".pickle"):
                continue

            regressor_name = filePath.replace(".pickle", "")
            with open(f"./models/{filePath}", "rb") as f:
                regressors[regressor_name] = pickle.load(f)

    embeddings = make_embedding(content)
    X = pd.DataFrame(columns=[f"d{i}" for i in range(0, EMBEDDING_DIMENSIONS)])
    X.loc[0] = embeddings
    results = {}
    for regressor_name, regressor in regressors.items():
        y = regressor.predict(X)[0]
        results[regressor_name] = str(y)

    return results, embeddings


# classify("I love strawberries")
# classify("Artificial Intelligence is bad and should be exterminated")
classify("trump vs obama in a physical fight. who wins?")

import pickle
import os
import pandas as pd
import torch
from sentence_transformers import SentenceTransformer
from http.server import BaseHTTPRequestHandler, HTTPServer
import json

embedding_model = SentenceTransformer(
    "dunzhang/stella_en_400M_v5",
    cache_folder="./models",
    device="cuda",
    trust_remote_code=True,
)
EMBEDDING_DIMENSIONS = 1024


def make_embedding(content: str):
    return embedding_model.encode(content)


regressors = {}


def classify(content: str):
    # carregar regressores se não estiverem carregados
    if not regressors:
        for filePath in os.listdir("./models"):
            if not filePath.endswith(".pickle"):
                continue

            regressor_name = filePath.replace(".pickle", "")
            with open(f"./models/{filePath}", "rb") as f:
                regressors[regressor_name] = pickle.load(f)

    embeddings = make_embedding(content)
    X = pd.DataFrame(columns=[f"d{i}" for i in range(0, EMBEDDING_DIMENSIONS)])
    X.loc[0] = embeddings
    results = {}
    for regressor_name, regressor in regressors.items():
        print(regressor_name, regressor)
        y = regressor.predict(X)[0]
        results[regressor_name] = float(y)

    return results, embeddings


class RequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-Type", "text/html")
        self.end_headers()
        with open("./classifier-webui.html", "rb") as f:
            self.wfile.write(f.read())

    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "*")
        self.send_header("Access-Control-Allow-Headers", "*")
        self.end_headers()

    def do_POST(self):
        if self.path != "/classify":
            self.send_response(404)
            self.end_headers()
            return

        content_length = int(self.headers["Content-Length"])
        body = self.rfile.read(content_length)
        try:
            data = json.loads(body)
            prompt = data.get("prompt")

            if prompt is None:
                self.send_response(400)
                self.send_header("Content-Type", "application/json")
                self.send_header("Access-Control-Allow-Origin", "*")
                self.end_headers()
                self.wfile.write(b'{"error": "Missing prompt"}')
                return

            # Simple classification model
            result, embedding = classify(prompt)

            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Access-Control-Allow-Origin", "*")
            self.end_headers()
            self.wfile.write(
                json.dumps({"result": result, "embedding": embedding.tolist()}).encode()
            )
        except json.JSONDecodeError:
            self.send_response(400)
            self.send_header("Content-Type", "application/json")
            self.send_header("Access-Control-Allow-Origin", "*")
            self.end_headers()
            self.wfile.write(b'{"error": "Invalid JSON"}')


server_address = ("", 8000)
httpd = HTTPServer(server_address, RequestHandler)
print("Server running on port 8000...")
httpd.serve_forever()
