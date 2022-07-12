package log.consumidor;

import log.ConnectionUtils;

import javax.jms.*;
import java.util.Scanner;

public class ConsumidorLogComPrioridadeMaiorQueQuatro {

    // Exemplo de projeto log que foi inserido no mesmo projeto do jms, pois não vi necessidade de separá-los.
    // Nesse exemplo, iremos consumir a fila.log, definindo configurações como, com mensagens com prioridade maior que 4 apenas:
    // Usar a classe ProdutorLog para gerar mensagens a serem consumidas.
    // No exemplo abaixo, deve imprimir assim:
    // 9 - ERROR - Ocorreu um erro aqui (use sua imaginação xD)
    // 5 - INFO - Olá Mundo
    public static void main(String[] args) throws Exception {

        ConnectionUtils utilidade = new ConnectionUtils();

        Connection connection = utilidade.criarConnection();
        Session session = utilidade.criarSession();
        Destination destination = utilidade.criarDestination("LOG");
        MessageConsumer messageConsumer = utilidade.criarMessageConsumer(destination, "JMSPriority > 4");

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
