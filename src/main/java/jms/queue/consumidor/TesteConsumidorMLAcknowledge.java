package jms.queue.consumidor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorMLAcknowledge {

    // Exemplo de recebimento de mensagens e conhecimento (Acknowledge) sobre as mesmas.
    // Usar TesteProdutorFilaAcknowledge para testar.
    public static void main(String[] args) throws Exception {

        // Inicializa:
        // Criar context, factory, connection
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start(); // Inicializa a conexão explicitamente.

        Session session = connection.createSession(
                false, // Se queremos ou não uma transação.
                Session.CLIENT_ACKNOWLEDGE); // Nós devemos informar o recebimento da mensagem.

        Destination destination = (Destination) initialContext.lookup("financeiro"); // A fila DLQ de dentro do jndi.
        MessageConsumer messageConsumer = session.createConsumer(destination); // Fica ouvindo uma fila.
        // Finaliza.

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    message.acknowledge(); // Faz o conhecimento da mensagem recebida. Sem esse método, toda vez que o Consumidor rodar, ele vai receber a mesma mensagem.
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        System.out.println("[TesteConsumidorMLAcknowledge] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorMLAcknowledge] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
