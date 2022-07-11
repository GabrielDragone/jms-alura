package jms.queue.consumidor;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteConsumidorMLTransacional {

    // Exemplo de recebimento de mensagens utilizando o TRANSACTIONAL para fazer commit e rollbacks (ja que nao existe um unknowledge).
    // Usar TesteProdutorFila para testar.
    // Quando usar o rollback, utilizar o ConsumidorDLQ para pegar as mensagens que não conseguimos entregar.
    public static void main(String[] args) throws Exception {

        // Inicializa:
        // Criar context, factory, connection
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start(); // Inicializa a conexão explicitamente.

        Session session = connection.createSession(
                true, // Queremos uma transação, ou seja, poderemos utilizar o commit e rollback na session.
                Session.SESSION_TRANSACTED); // Comportamentop transacional (commit ou rollback).

        Destination destination = (Destination) initialContext.lookup("financeiro"); // A fila DLQ de dentro do jndi.
        MessageConsumer messageConsumer = session.createConsumer(destination); // Fica ouvindo uma fila.
        // Finaliza.

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println(textMessage.getText());
                    //session.commit(); // Faz o conhecimento do recebimento da mensagem.
                    session.rollback(); // Não confirma/anula o recebimento da mensagem. Utilizar o ConsumidorDLQ para consumir as mesmas.
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println("[TesteConsumidorMLTransacional] Teste Antes do nextLine()");

        // Deixa a aplicação "pausada" enquanto algo não é digitado no teclado:
        new Scanner(System.in).nextLine();

        System.out.println("[TesteConsumidorMLTransacional] Teste Depois do nextLine()");

        session.close();
        connection.close();
        initialContext.close();

    }

}
