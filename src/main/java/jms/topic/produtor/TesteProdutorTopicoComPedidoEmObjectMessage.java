package jms.topic.produtor;

import modelo.Pedido;
import modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Scanner;

public class TesteProdutorTopicoComPedidoEmObjectMessage {

    // Exemplo de ENVIO de mensagens. Posteriormente usar a classe TesteConsumidorComMessageListenerTopico para testar o recebimento:
    // Essa classe demonstra o exemplo utilizando Pedido e ObjectMessage, ou seja, estaremos enviando mesmo um objeto serializado:
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) initialContext.lookup("loja");
        MessageProducer messageProducer = session.createProducer(destination);

        //Gera o Pedido e o envia através de ObjectMessage:
        Pedido pedido = new PedidoFactory().geraPedidoComValores();
        Message message = session.createObjectMessage(pedido);

        messageProducer.send(message); // Esse cara faz o envio da mensagem para o topico.

        System.out.println("[TesteProdutorTopicoComPedidoEmObjectMessage] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteProdutorTopicoComPedidoEmObjectMessage] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
