package log;

import javax.jms.*;
import javax.naming.InitialContext;

public class ConnectionUtils {

    InitialContext initialContext;
    ConnectionFactory connectionFactory;
    Connection connection;
    Session session;
    Destination destination;

    public Connection criarConnection() throws Exception {
        initialContext = new InitialContext();
        connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    public Session criarSession() throws Exception {
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // Não será transacional e o reconhecimento da mensagem será feito automaticamente.
        return session;
    }

    public Destination criarDestination(String propertyLookUp) throws Exception {
        destination = (Destination)initialContext.lookup(propertyLookUp);
        return destination;
    }

    public MessageConsumer criarMessageConsumer(Destination destination, String selector) throws Exception {
        if (!"".equals(selector)) {
            return session.createConsumer(destination, selector);
        }
        return session.createConsumer(destination);
    }

    public MessageProducer criarMessageProducer(Destination destination) throws Exception {
        return session.createProducer(destination);
    }

    public void close() throws Exception {
        System.out.println("Finalizando conexão!");
        session.close();
        connection.close();
        initialContext.close();
        System.out.println("Conexão finalizada!");
    }
}
