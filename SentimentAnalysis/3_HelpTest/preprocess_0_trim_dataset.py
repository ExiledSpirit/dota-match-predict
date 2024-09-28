import pandas as pd

# source CSV: https://huggingface.co/datasets/SetFit/toxic_conversations , file "original.csv"
df = pd.read_csv("./dataset/setfit_toxicconversations.csv")
df = df[
    [
        "comment_text",
        "target",
        "severe_toxicity",
        "obscene",
        "identity_attack",
        "insult",
        "threat",
        "sexual_explicit",
    ]
]
df["comment_text"] = df["comment_text"].map(lambda x: str(x).strip())
df.to_csv("./dataset/output_trimmed.csv", index=False)
