package br.com.saneago.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 *
 */
public class TesteConsumidorTopico {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		InitialContext context = null;
		Connection connection = null;
		try {
			context = new InitialContext();

			ConnectionFactory factory = (ConnectionFactory) context
					.lookup("ConnectionFactory");

			connection = factory.createConnection();
			connection.setClientID("estoque");
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Topic topico = (Topic) context.lookup("loja");
			
			MessageConsumer consumidor = session.createDurableSubscriber(topico, "assinatura");
			
			consumidor.setMessageListener(new MessageListener(){

				public void onMessage(Message mensagem) {					
					try {
						TextMessage mensagemTexto = (TextMessage)mensagem;
						
						System.out.println("Mensagem recebida: " + mensagemTexto.getText());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
