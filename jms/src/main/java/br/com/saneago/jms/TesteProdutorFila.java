package br.com.saneago.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 *
 */
public class TesteProdutorFila {
	
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

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination fila = (Destination) context.lookup("financeiro");
			
			MessageProducer produtor = session.createProducer(fila);
			
			Message mensagem  = session.createTextMessage("texto enviado!");
			
			produtor.send(mensagem);
			
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
