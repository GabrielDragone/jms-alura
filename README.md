# jms-alura
Projeto desenvolvido no curso do Alura: JMS e ActiveMQ: mensageria com Java:
https://cursos.alura.com.br/course/jms

### Conteúdo:
* JMS: Java Message Service. Api padrão para enviar e receber mensagens.
* MOM/Broker: Middleware Orientado a Mensagem. Lugar onde os dados ficam salvos temporariamente até serem consumidos por outro sistema. Fará a entrega assíncrona e desacoplada. Ajuda a lidar com indisponibilidade e picos de processamento de sistemas.
* Filas: Um produtor envia uma mensagem para um Consumidor (1 pra 1). A mesma mensagem não pode ser entregue pra outro Consumidor.
* Tópicos: Um produtor envia mensagem para um ou mais Consumidores (1 para n). A mesma mensagem pode ser entregue para mais de um Consumidor.
* Produtores: Responsável por enviar a mensagem para uma fila/tópico.
* Consumidores: Responsável por receber a mensagem enviada para uma fila/tópico.
* Tópico e assinaturas duráveis: Só existem para tópico. É um consumidor de um tópico que se identificou. Ou seja, o tópico sabe da existência desse consumidor. O tópico, por padrão, não garante a entrega da mensagem, pois não sabe se existe 1 ou mais consumidores. Então de cara, o tópico só entrega mensagens para consumidores que estiverem online.
* Selector: Usado quando não queremos receber todas as mensagens, apenas algumas específicas. Funciona de maneira parecida ao SQL.
* DLQ: Dead Letter Queue. Fila onde as mensagens que não conseguiram ser entregues ficam armazenadas depois de certa quantidade de tentativas de entrega (no ActiveMQ são 6 tentativas).
* Acknowledge: Faz o reconhecimento do recebimento da mensagem. Parâmetros variam entre reconhecimento automatico ou manual.
* Exemplo de envio de mensagens por TextMessage e ObjectMessage.
* Prioridade e tempo de vida das mensagens: Define a ordem como cada mensagem será consumida e o tempo que a mesma ficará aguardando para ser consumida.

### Como rodar (resumidamente):
* O projeto inteiro está cheio de comentários, logo o entendimento do mesmo será mais fácil.
* Baixar o apache-activemq-5.16.5 através do link: https://activemq.apache.org/components/classic/download/
* Descompactar o arquivo em uma pasta.
* Acessar através do terminal a pasta bin. Exemplo: cd \Projects\activemq-alura\apache-activemq-5.16.5\bin
* Rodar o comando: activemq start.
* Acessar e criar as filas/tópicos: http://localhost:8161/
* Dentro do arquivo jndi.properties, definir as files e tópicos.
* Temos duas formas de gerar mensagens:
  * Através do console do ActiveMq, ir em queues, clicar em send to e escrever a mensagem pra enviar.
  * Ou través das classes com nome Produtor.
* Para receber as mensagens, basta rodar as classes Consumidor e ler na IDE as mensagens enviadas.
* Rodar o projeto através das classes: TesteConsumidor ou TesteConsumidorComMessageListener.
