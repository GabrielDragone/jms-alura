package jms.queue.produtor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteProdutorFila {

    // Exemplo de ENVIO de mensagens. Posteriormente usar a classe TesteConsumidorComMessageListener para testar o recebimento:
    public static void main(String[] args) throws Exception {

        // Inicializa:
        // Criar context, factory, connection
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start(); // Inicializa a conexão explicitamente.

        Session session = connection.createSession( // Abstrai o trabalho transacional e a parte de confirmação de recebimento da msg.
                false, // Se queremos ou não uma transação.
                Session.AUTO_ACKNOWLEDGE); // Confirma automaticamente o recebimento da mensagem.

        Destination destination = (Destination) initialContext.lookup("financeiro"); // A fila de dentro do jndi.

        MessageProducer messageProducer = session.createProducer(destination);

        for(int i = 0; i < 1000 ; i++){
            Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
            messageProducer.send(message); // Esse cara faz o envio da mensagem para a fila.
        }

        System.out.println("[TesteConsumidorComMessageListener] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorComMessageListener] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
