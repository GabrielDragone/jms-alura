package log.produtor;

import log.ConnectionUtils;

import javax.jms.*;
import java.util.Scanner;

public class ProdutorLogComPrioridade {

    // Exemplo de projeto log que foi inserido no mesmo projeto do jms, pois não vi necessidade de separá-los.
    // Nesse exemplo, iremos consumir a fila.log, definindo configurações como:
    // Prioridade de entrega, que varia de 0 a 9, sendo 9 de maior prioridade.
    // Usar a classe ConsumidorLog para consumir as mensagens geradas e verifique a ordem na que foram apresentadas. No exemplo abaixo, deve imprimir assim:
    // 9 - ERROR - Ocorreu um erro aqui (use sua imaginação xD)
    // 5 - INFO - Olá Mundo
    // 4 - INFO - Meu nome é Gabriel!
    // 3 - DEBUG - Olá Mundo
    // 0 - INFO - Olá Mundo
    public static void main(String[] args) throws Exception {
        ConnectionUtils utilidade = new ConnectionUtils();

        Connection connection = utilidade.criarConnection();
        Session session = utilidade.criarSession();
        Destination destination = utilidade.criarDestination("LOG");

        MessageProducer messageProducer = utilidade.criarMessageProducer(destination);

        // Quanto maior a prioridade, quando houver um Consumidor on ela receberá na ordem de Prioridade:
        Message message = session.createTextMessage("4 - INFO - Meu nome é Gabriel!");
        enviarMensagem(messageProducer, message, 4, 80000);

        message = session.createTextMessage("5 - INFO - Olá Mundo");
        enviarMensagem(messageProducer, message, 5, 80000);

        message = session.createTextMessage("3 - DEBUG - Olá Mundo");
        enviarMensagem(messageProducer, message, 3, 80000);

        message = session.createTextMessage("9 - ERROR - Ocorreu um erro aqui (use sua imaginação xD)");
        enviarMensagem(messageProducer, message, 9, 80000);

        message = session.createTextMessage("0 - INFO - Olá Mundo");
        enviarMensagem(messageProducer, message, 0, 80000);

        System.out.println("[ProdutorLogComPrioridade] Mensagem enviada!");

        new Scanner(System.in).nextLine();

        utilidade.close();
    }

    private static void enviarMensagem(MessageProducer messageProducer, Message message, int prioridade, int tempo) throws JMSException {
        messageProducer.send(
                message, // A mensagem em si.
                DeliveryMode.NON_PERSISTENT, // NON_PERSISTENT: Não guarda a msg num banco de dados / PERSISTENT: Guarda a msg num banco de dados. Caso o ActiveMq caia/reinicie, apenas as mensagens não consumidas no modo PERSISTENT serão recuperadas.
                prioridade, // Prioridade: Vai de 0 a 9. Sendo 9 a maior prioridade.
                tempo // Tempo em milisegundos que a mensagem vai aguardar para ser consumida. Se não for consumida nesse tempo, será movida para Dequeued
        );
    }
}
