# Objetivo

Treinamento de um modelo pequeno que classificará um fragmento de texto como ofensivo/inofensivo (violando diretrizes do Twitter).

# Procedimento

## Semana 1: Pesquisa de métodos prévios

Pesquisar métodos de classificação de comentários. Métodos encontrados:

- Naive-Bayes, Bag of Words, word2vec, classificação com modelo de linguagem

Atribuições: Lucas Bortoli, Pedro Frasson

## Semana 2: Obtenção de Dataset

Atribuições: Lucas Bortoli, Pedro Frasson

Foi obtido um dataset contendo tweets pré-anotados como ofensivos.

Para a parte do dataset de tweets, será usado o modelo de linguagem open-source LLaMA 3.1 8B, sendo executado por meio do runtime llama.cpp, atarefado com o objetivo de gerar inúmeros tweets genéricos, que, por conta do alinhamento de segurança imposto pela Meta, não violarão diretrizes de nenhuma rede social.

O objetivo é obter cerca de 20_000 tweets sintéticos.

## Semana 3: Obtenção do Dataset Sintético

Atribuições: Lucas Bortoli

Criar um script e um prompt que criará os dados sintéticos, executando em uma RTX 3060 12 GB.

## Semana 4: Estudo de treinamento BERT

Atribuições: Lucas Bortoli, Pedro Frasson

Estudo de treinamento BERT. É possível treinar o BERT a partir do zero em apenas uma RTX 3060.

## Semana 5: Treinamento

Atribuições: Lucas Bortoli, Pedro Frasson

Treinamento do modelo.

# Referências

1. [Naive Bayes e a Classificação de Sentimento](https://medium.com/@lainetnr/naive-bayes-e-a-classifica%C3%A7%C3%A3o-de-sentimento-e696a123cc2d)
2. [BERT-based Sentiment Classification Mode ](https://huggingface.co/tabularisai/robust-sentiment-analysis): modelo BERT treinado para classificar sentimento nas seguintes categorias: "Very Negative", "Negative", "Neutral", "Positive", "Very Positive".
3. [Dataset de tweets com anotações de sentimento](https://huggingface.co/datasets/cardiffnlp/tweet_eval)
4. [Notes on training BERT from scratch on an 8GB consumer GPU](https://sidsite.com/posts/bert-from-scratch/)
