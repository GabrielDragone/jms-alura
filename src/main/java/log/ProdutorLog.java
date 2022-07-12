package log;

import javax.jms.*;
import java.util.Scanner;

public class ProdutorLog {

    // Exemplo de projeto log que foi inserido no mesmo projeto do jms, pois não vi necessidade de separá-los.
    // Nesse exemplo, iremos consumir a fila.log, definindo configurações como:
    // Modo de Entrega: PERSISTENT (guarda no banco) ou NON_PERSISTENT (não guarda no banco) para mensagens a serem consumidas.
    // Prioridade de entrega, que varia de 0 a 9, sendo 9 de maior prioridade.
    // Tempo que a mensagem aguarda para ser consumida antes de ser removida.
    // Alterar os parâmetros do messaProducer.send para simular cenários diferentes e observar dentro do console do ActiveMq. Se o ActiveMQ for reiniciado com modo NON_PERSISTENT as mensagens são perdidas.
    // Usar a classe ConsumidorLog para consumir as mensagens geradas.
    public static void main(String[] args) throws Exception {
        ConnectionUtils utilidade = new ConnectionUtils();

        Connection connection = utilidade.criarConnection();
        Session session = utilidade.criarSession();
        Destination destination = utilidade.criarDestination("LOG");

        MessageProducer messageProducer = utilidade.criarMessageProducer(destination);

        Message message = session.createTextMessage("ERROR - Ocorreu um erro aqui (use sua imaginação xD)");

        // Alterar os parâmetros abaixo para simular outros cenários:
        messageProducer.send(
                message, // A mensagem em si.
                DeliveryMode.NON_PERSISTENT, // NON_PERSISTENT: Não guarda a msg num banco de dados / PERSISTENT: Guarda a msg num banco de dados. Caso o ActiveMq caia, apenas as mensagens não consumidas no modo PERSISTENT serão recuperadas.
                1, // Prioridade: Vai de 0 a 9. Sendo 9 a maior prioridade.
                80000 // Tempo em milisegundos que a mensagem vai aguardar para ser consumida. Se não for consumida nesse tempo, será movida para Dequeued
        );

        System.out.println("[ProdutorLog] Mensagem enviada!");

        new Scanner(System.in).nextLine();

        utilidade.close();
    }
}
