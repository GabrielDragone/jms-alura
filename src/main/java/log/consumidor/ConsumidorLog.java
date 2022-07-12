package log.consumidor;

import log.ConnectionUtils;

import javax.jms.*;
import java.util.Scanner;

public class ConsumidorLog {

    // Exemplo de projeto log que foi inserido no mesmo projeto do jms, pois não vi necessidade de separá-los.
    // Nesse exemplo, iremos consumir a fila.log, definindo configurações como:
    // Modo de Entrega: PERSISTENT (guarda no banco) ou NON_PERSISTENT (não guarda no banco) para mensagens a serem consumidas.
    // Prioridade de entrega, que varia de 0 a 9, sendo 9 de maior prioridade.
    // Tempo que a mensagem aguarda para ser consumida antes de ser removida.
    // Usar a classe ProdutorLog para gerar mensagens a serem consumidas.
    public static void main(String[] args) throws Exception {

        ConnectionUtils utilidade = new ConnectionUtils();

        Connection connection = utilidade.criarConnection();
        Session session = utilidade.criarSession();
        Destination destination = utilidade.criarDestination("LOG");
        MessageConsumer messageConsumer = utilidade.criarMessageConsumer(destination, "");

        messageConsumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage)message;
            try {
                System.out.println(textMessage.getText());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("[ConsumidorLog] Mensagem consumida!");

        // Pausa o sistema até receber um comando no terminal:
        new Scanner(System.in).nextLine();

        // Fechamos as conexões:
        utilidade.close();
    }
}
