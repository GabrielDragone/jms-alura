package jms.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.Scanner;

public class TesteConsumidorComMessageListenerEPropertiesFila {

    // Clase pra mostrar como utilizar a classe Properties ao invés do JNDI:
    public static void main(String[] args) throws Exception {

        // Inicializa:
        // Criar context, factory, connection
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
        properties.setProperty("queue.financeiro", "fila.financeiro");

        InitialContext initialContext = new InitialContext(properties);
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start(); // Inicializa a conexão explicitamente.

        Session session = connection.createSession( // Abstrai o trabalho transacional e a parte de confirmação de recebimento da msg.
                false, // Se queremos ou não uma transação.
                Session.AUTO_ACKNOWLEDGE); // Confirma automaticamente o recebimento da mensagem.

        Destination destination = (Destination) initialContext.lookup("financeiro"); // A fila de dentro do jndi.
        MessageConsumer messageConsumer = session.createConsumer(destination); // Fica ouvindo uma fila.
        // Finaliza.

        messageConsumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage)message;
            try {
                System.out.println(textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        System.out.println("[TesteConsumidorComMessageListener] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorComMessageListener] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
