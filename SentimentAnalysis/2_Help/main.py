import requests
import numpy as np
from sentence_transformers import SentenceTransformer
import gc

LLAMAFILER_API = "http://localhost:8080"
EMBEDDING_DIMENSIONS = 1024

# hack para não crashar o GNOME durante execução desse bloco várias vezes
# liberar modelo carregado anteriormente e forçar coleção de memória
model = None
gc.collect()

model = SentenceTransformer(
    "dunzhang/stella_en_1.5B_v5", trust_remote_code=True, device="cpu"
)


def make_embedding_transformers(content: str):
    # configuração do modelo Stella
    query_prompt_name = "s2p_query"
    embeddings = model.encode([content])

    return embeddings[0]


def make_embedding_llama(content: str):
    response = requests.post(LLAMAFILER_API + "/embedding", json={"content": content})
    # Check if the request was successful
    if response.status_code == 200:
        # Parse the JSON response
        data = response.json()

        # Get the "embedding" field and convert it to a numpy array
        embedding = np.array(data["embedding"])

        return embedding
    else:
        print("Failed to retrieve data")
        raise AssertionError("Resposta inválida")


def make_embedding(content: str):
    return make_embedding_transformers(content)


make_embedding("hi")
