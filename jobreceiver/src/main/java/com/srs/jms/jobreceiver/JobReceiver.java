package com.srs.jms.jobreceiver;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.QueueSession;
import javax.jms.QueueReceiver;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

public class JobReceiver {
	public static void main(String args[]) throws Exception {
		Properties env = new Properties();					   				env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		env.put("queue.queueSampleQueue","MyNewQueue");
		// get the initial context
		InitialContext ctx = new InitialContext(env);

		// lookup the queue object
		Queue queue = (Queue) ctx.lookup("queueSampleQueue");

		// lookup the queue connection factory
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");

		// create a queue connection
		QueueConnection queueConn = connFactory.createQueueConnection();

		// create a queue session
		QueueSession queueSession = queueConn.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

		// create a queue receiver
		QueueReceiver queueReceiver = queueSession.createReceiver(queue);

		// start the connection
		queueConn.start();

		//Put an infinite loop to receive messages and print them.
		System.out.println("Starting looping and waiting for JMS Message ...\n");
		try {
			int i=0;
			for (;;) {
				// receive a message
				TextMessage message = (TextMessage) queueReceiver.receive();
				// print the message
				System.out.println("received: " + message.getText());
				// include a delay of just one second - this is for demo purposes
				Thread.sleep(1000);
				i++;
				System.out.println("Counter value ==> " + i + "\n");
			}
		} catch (Exception e) {
			System.out.println("Exception --> " + e + "\n");
		} finally {
			// close the queue connection
			queueConn.close();
			System.out.println("Connections closed.\n");
		}
	}

}
