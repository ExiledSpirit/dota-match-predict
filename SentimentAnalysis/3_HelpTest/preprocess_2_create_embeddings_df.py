import torch
from sentence_transformers import SentenceTransformer
import pandas as pd
import pickle
import pandas as pd

EMBEDDING_DIMENSIONS = 1024
model = SentenceTransformer(
    "dunzhang/stella_en_400M_v5",
    cache_folder="./models",
    device="cuda",
    trust_remote_code=True,
)

source_data = pd.read_csv("./dataset/output_subset_64k.csv")

comment_texts = source_data["comment_text"].tolist()

# Start the multi-process pool on all available CUDA devices
pool = model.start_multi_process_pool()

# Compute the embeddings using the multi-process pool
embeddings = model.encode_multi_process(comment_texts, pool)
print("Embeddings computed. Shape:", embeddings.shape)

# Optional: Stop the processes in the pool
model.stop_multi_process_pool(pool)

with open("./embeddings.pickle", "wb") as f:
    pickle.dump(embeddings, f)

# Na RTX 3060: 10min, 28s para computar 64k embeddings com o Stella 400M v5


embeddings = None
with open("./embeddings.pickle", "rb") as f:
    embeddings = pickle.load(f)


X_df = pd.DataFrame(
    data=embeddings, columns=[f"d{i}" for i in range(0, EMBEDDING_DIMENSIONS)]
)
y_df = pd.read_csv("./dataset/output_subset_64k.csv")[["target"]]

dataset = pd.concat([y_df, X_df], axis=1)
dataset.to_parquet("./dataset/output_embeddings.parquet", index=False)
