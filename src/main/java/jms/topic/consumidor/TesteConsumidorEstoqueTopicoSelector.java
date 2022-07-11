package jms.topic.consumidor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorEstoqueTopicoSelector {

    // Vai receber a mensagem mesmo se o Consumidor estiver offline, pois foi feito a configuração seClienteID e assinatura.
    // Selector: Quando o sistema não está interessado em todas as mensagens.
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("estoque");

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) initialContext.lookup("loja"); // O topico de dentro do jndi.
        MessageConsumer messageConsumer = session.createDurableSubscriber(topico,
                "assinatura",
                "ebook=false OR ebook is null", // MessageSelector: Só irá receber mensagens produzidas pelo Produtor com header informando o ebook=false.
                false);

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("TesteConsumidorEstoqueTopicoSelector: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });

        System.out.println("[TesteConsumidorEstoqueTopicoSelector] Teste Antes do nextLine()");
        new Scanner(System.in).nextLine();
        System.out.println("[TesteConsumidorEstoqueTopicoSelector] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();
    }
}
