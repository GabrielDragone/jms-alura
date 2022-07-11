package jms.topic.consumidor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorTopico {

    // Só vai receber mensagem se estiver online no momento do ProdutorTopic enviar:
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) initialContext.lookup("loja"); // O topico de dentro do jndi.
        MessageConsumer messageConsumer = session.createConsumer(topico); // Fica ouvindo um topico.

        messageConsumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });

        System.out.println("[TesteConsumidorTopico] Teste Antes do nextLine()");
        new Scanner(System.in).nextLine();
        System.out.println("[TesteConsumidorTopico] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();
    }
}
