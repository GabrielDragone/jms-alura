package jms.topic.produtor;

import modelo.Pedido;
import modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Scanner;

public class TesteProdutorTopicoComPedidoEmXML {

    // Exemplo de ENVIO de mensagens. Posteriormente usar a classe TesteConsumidorComMessageListenerTopico para testar o recebimento:
    // Essa classe demonstra o exemplo utilizando Pedido e XML:
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) initialContext.lookup("loja");
        MessageProducer messageProducer = session.createProducer(destination);

        //Gera o Pedido:
        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        //Transforma o mesmo em XML utilizando biblioteca JAXB:
        StringWriter writer = new StringWriter();
        JAXB.marshal(pedido, writer);
        String strXml = writer.toString();

        Message message = session.createTextMessage(strXml);
        messageProducer.send(message); // Esse cara faz o envio da mensagem para o topico.

        System.out.println("[TesteProdutorTopicoComPedidoEmXML] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteProdutorTopicoComPedidoEmXML] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
