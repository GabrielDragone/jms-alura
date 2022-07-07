package jms.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteQueueBrowser {

    // Exemplo de recebimento de várias mensagens enquanto sistema estiver rodando:
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
        QueueBrowser queueBrowser = session.createBrowser((Queue) destination);

        System.out.println("Fila: " + queueBrowser.getQueue()); // Obtém a fila associada a este navegador de filas.
        System.out.println("MessageSelector: " + queueBrowser.getMessageSelector()); // Obtém a expressão do seletor de mensagens deste navegador de filas.
        System.out.println("Enumeration: " + queueBrowser.getEnumeration()); // Obtém uma enumeração para navegar pelas mensagens da fila atual na ordem em que seriam recebidas.
        // Finaliza.

        System.out.println("[TesteConsumidorComMessageListener] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorComMessageListener] Teste Depois do nextLine()");

        queueBrowser.close(); // Fecha o QueueBrowser.

        session.close();
        connection.close();
        initialContext.close();

    }

}
