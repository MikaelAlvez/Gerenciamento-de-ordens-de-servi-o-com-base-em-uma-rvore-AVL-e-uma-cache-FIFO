Objetivo do seu projeto é um sistema de gerenciamento de ordens de serviço com base em uma árvore AVL e uma cache FIFO

Simulação de uma aplicação cliente/servidor, onde:
■ Uma classe ou conjunto de classes implementa o cliente que pode fazer buscas
na base de dados;
■ Uma classe ou conjunto de classes implementa o servidor que controla a base
de dados.

Sobre as entidades:
■ Cada ordem de serviço possui dados como código, nome, descrição e hora da
solicitação.

Sobre a cache e a base de dados:
■ A base de dados deve ser implementada como uma Árvore AVL.
● A modelagem de cada nó da árvore faz parte da interpretação do
projeto (use orientação a objetos).
■ A cache pode ser implementada como uma lista de tamanho 20 e deve utilizar
uma política baseada em FIFO.

● Entre o cliente e o servidor existe a cache.
● Clientes podem fazer consultas (buscas) usando o código.

Outras operações que um cliente pode fazer são:
● Cadastrar OS.
● Listar ordens de serviço (todos os dados de todas as ordens de serviço).
● Alterar OS.
● Remover OS.
● Acessar a quantidade de registros.

Servidor atende às requisições do cliente.
● O servidor deve manter uma espécie de log informando a altura da
árvore a cada inserção ou remoção e se houve alguma rotação ou não
(sugiro inserir essas informações em um arquivo).
● Além disso, deve mostrar os ítens da cache a cada operação.
○ Para mostrar a política de cache eviction funcionando.

![image](https://github.com/user-attachments/assets/359c45cc-98fe-47ed-8e3a-ba67e1eb9b79)
