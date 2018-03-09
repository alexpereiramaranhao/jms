package br.com.saneago.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 *
 */
public class TesteConsumidorFilaDlq {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		InitialContext context = null;
		Connection connection = null;
		try {
			context = new InitialContext();

			ConnectionFactory factory = (ConnectionFactory) context
					.lookup("ConnectionFactory");

			connection = factory.createConnection();
			connection = factory.createConnection("user", "senha");
			connection.start();

			final Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			
			Destination fila = (Destination) context.lookup("dlq");
			
			MessageConsumer consumidor = session.createConsumer(fila);
			
			consumidor.setMessageListener(new MessageListener(){

				public void onMessage(Message mensagem) {					
					System.out.println("Mensagem recebida: " + mensagem);	
					try {
						session.commit();
					} catch (JMSException e) {						
						e.printStackTrace();
						try {
							session.rollback();
						} catch (JMSException e1) {							
							e1.printStackTrace();
						}
					}
				}
				
			});
			
			new Scanner(System.in).nextLine();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
				if(context != null){
					context.close();
				}
			} catch (JMSException e) {				
				e.printStackTrace();

			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

	}
}
