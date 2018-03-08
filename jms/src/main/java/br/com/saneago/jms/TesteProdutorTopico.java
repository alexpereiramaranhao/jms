package br.com.saneago.jms;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

/**
 * Hello world!
 *
 */
public class TesteProdutorTopico {
	public static void main(String[] args) {
		
		InitialContext context = null;
		Connection connection = null;
		try {			
			context = new InitialContext();

			ConnectionFactory factory = (ConnectionFactory) context
					.lookup("ConnectionFactory");

			connection = factory.createConnection("user", "senha");
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination topico = (Destination) context.lookup("loja");
			
			MessageProducer produtor = session.createProducer(topico);
			
			Pedido pedito = new PedidoFactory().geraPedidoComValores();
			String xml = serializarObjetoParaXml(pedito);
			
			//System.out.println(xml);
			System.out.println(pedito.toString());
			
			//Message mensagem  = session.createTextMessage(xml);//enviar conteúdo mensagem xml
			
			Message mensagem  = session.createObjectMessage(pedito);//enviar conteúdo mensagem objecto
			
			mensagem.setBooleanProperty("ebook", false);
			
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

	private static String serializarObjetoParaXml(Pedido pedito) {
		StringWriter writer = new StringWriter();
		
		JAXB.marshal(pedito, writer);

		String xml = writer.toString();
		return xml;
	}
}
