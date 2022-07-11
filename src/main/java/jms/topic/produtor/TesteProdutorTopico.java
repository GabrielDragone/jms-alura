package jms.topic.produtor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteProdutorTopico {

    // Exemplo de ENVIO de mensagens. Posteriormente usar a classe TesteConsumidorComMessageListenerTopico para testar o recebimento:
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) initialContext.lookup("loja");
        MessageProducer messageProducer = session.createProducer(destination);

        //for(int i = 0; i < 1000 ; i++){
//            Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
            Message message = session.createTextMessage("<pedido><id>123</id></pedido>");
            messageProducer.send(message); // Esse cara faz o envio da mensagem para o topico.
        //}

        System.out.println("[TesteProdutorTopico] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteProdutorTopico] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
