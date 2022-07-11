package jms.topic.produtor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteProdutorTopicoSelector {

    // Exemplo de ENVIO de mensagens com SELECTOR. Posteriormente usar a classe TesteConsumidorComMessageListenerTopico para testar o recebimento:
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) initialContext.lookup("loja");
        MessageProducer messageProducer = session.createProducer(destination);

        for (int i = 0; i < 100 ; i++) {
            Message message;
            if (i % 2 == 0) {
                message = session.createTextMessage("<pedido><id>" + i + "</id><ebook>true</ebook></pedido>");
                message.setBooleanProperty("ebook", true); // header, esse não deve ser entregue
            } else {
                message = session.createTextMessage("<pedido><id>" + i + "</id><ebook>false</ebook></pedido>");
                //message.setBooleanProperty("ebook", false); // header, esse deve ser entregue
            }
            messageProducer.send(message); // Esse cara faz o envio da mensagem para o topico.
        }

        System.out.println("[TesteProdutorTopicoSelector] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteProdutorTopicoSelector] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
