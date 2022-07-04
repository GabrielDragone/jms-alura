package jms;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidor {

    // Exemplo de recebimento de apenas uma mensagem:
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
        MessageConsumer messageConsumer = session.createConsumer(destination); // Fica ouvindo uma fila.
        // Finaliza.

        Message message = messageConsumer.receive(); // Recebe a mensagem.
        //Message messageComTempo = messageConsumer.receive(10000); // Recebe a mensagem com espera de 10 segundos. Se após 10s não receber nenhuma mensagem o método receive devolve null

        System.out.println("Recebendo mensagem: " + message);

        System.out.println("[TesteConsumidor] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidor] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }
}
