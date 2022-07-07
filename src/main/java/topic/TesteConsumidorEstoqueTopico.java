package topic;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorEstoqueTopico {

    // Vai receber a mensagem mesmo se o Consumidor estiver offline, pois foi feito a configuração seClienteID e assinatura.
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("estoque");

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) initialContext.lookup("loja"); // O topico de dentro do jndi.
        MessageConsumer messageConsumer = session.createDurableSubscriber(topico, "assinatura"); // Fica ouvindo um topico.

        messageConsumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("TesteConsumidorEstoqueTopico: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });

        System.out.println("[TesteConsumidorEstoqueTopico] Teste Antes do nextLine()");
        new Scanner(System.in).nextLine();
        System.out.println("[TesteConsumidorEstoqueTopico] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();
    }
}
