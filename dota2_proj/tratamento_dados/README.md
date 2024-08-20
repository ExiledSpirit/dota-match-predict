# Por que
A ideia desse desse programa é remover colunas desnecessárias para simplificar a analise dos dados.

Além de remover as colunas desnecessárias outros tratamentos devem ser feitos como correlacionar variáveis em uma nova um exemplo seria correlacionar uma coluna com outra base de dados como uma contendo o winrate dos personagens. Também é possivel unir duas ou mais variáveis em um coeficiente só em uma nova coluna.

# Como rodar
- subsitua o conteúdo do arquivo <code>input.csv</code> pela base de dados que você queira tratar.
- coloque as colunas a serem removidas no arquivo <code>headers_to_remove</code>
- para rodar basta executar <code>node index.js</code>
- ao final do processo o programa vai gerar um arquivo <code>output.csv</code> com os dados tratados
- talvez seja necessário substituir ocorrências de \n para \r\n devido a formatação automatica do git