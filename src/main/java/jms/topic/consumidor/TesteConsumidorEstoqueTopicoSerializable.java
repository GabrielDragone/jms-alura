package jms.topic.consumidor;

import modelo.Pedido;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.util.Scanner;

public class TesteConsumidorEstoqueTopicoSerializable {

    // Vai receber a mensagem mesmo se o Consumidor estiver offline, pois foi feito a configuração seClienteID e assinatura.
    public static void main(String[] args) throws Exception {

        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*"); // Essa linha informa ao ActiveMQ que todos os pacotes podem ser serializados

        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("estoque");

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) initialContext.lookup("loja"); // O topico de dentro do jndi.
        MessageConsumer messageConsumer = session.createDurableSubscriber(topico, "assinatura"); // Fica ouvindo um topico.

        messageConsumer.setMessageListener(message -> {
            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
//                Serializable pedido = objectMessage.getObject(); // Deserializa o objeto serializado que foi enviado
                Pedido pedido = (Pedido)objectMessage.getObject(); // Deserializa o objeto serializado que foi enviado
                System.out.println("Pedido recebido: " + pedido.getCodigo());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        System.out.println("[TesteConsumidorEstoqueTopicoSerializable] Teste Antes do nextLine()");
        new Scanner(System.in).nextLine();
        System.out.println("[TesteConsumidorEstoqueTopicoSerializable] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();
    }
}
