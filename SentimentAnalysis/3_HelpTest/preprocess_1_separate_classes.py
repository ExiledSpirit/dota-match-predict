import pandas as pd

df = pd.read_csv("./dataset/output_trimmed.csv")

offensive = df[df["target"] >= 0.5].sample(n=32768, random_state=2003)
inoffensive = df[df["target"] < 0.5].sample(n=32768, random_state=2003)

subset = pd.concat([offensive, inoffensive], ignore_index=True, sort=False)

subset.to_csv("./dataset/output_subset_64k.csv", index=False)
