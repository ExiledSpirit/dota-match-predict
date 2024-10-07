# Feature Selection

1. Features Irrelevantes: Algumas features são irrelevantes para o treinamento e outras são vazamentos de informações que só é possivel saber após o término da partida. `radiant_team_id` , `dire_team_id` e `winner_id` são usadas para dizer quem foi o vencedor, ao invés disso, podemos substituir por apenas uma coluna `winner_size` onde 0 é **radiant** e 1 **dire**. `match_duration_seconds` pode representar problema para a base de dados pois é uma informação adquirida apenas após o jogo, atrapalhando predições em tempo-real.
2. Demais features como ID para identificação precisam agregar valores com dados de fora senao serao apenas ruido para o treinamento
3. Aplicar min_max sempre que possivel
4. Utilizar metodos de feature selection para entender quais features explicam melhor a predicao de vitoria. Os modelos utilizados nas modificacoes foram **RFE**, CHI quadrado e f_classif
