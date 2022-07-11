package jms.queue.consumidor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorComMessageListenerDLQ {

    // Exemplo de recebimento de mensagens venenosas, ou seja, mensagens que não tiveram o recebimento confirmado após x vezes que o MOM tentou entregar.
    // Essas mensagens são armazenadas na fila DLQ.
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

        Destination destination = (Destination) initialContext.lookup("DLQ"); // A fila DLQ de dentro do jndi.
        MessageConsumer messageConsumer = session.createConsumer(destination); // Fica ouvindo uma fila.
        // Finaliza.

        messageConsumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                System.out.println(message);
            }

        });

        System.out.println("[TesteConsumidorComMessageListenerDLQ] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorComMessageListenerDLQ] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
