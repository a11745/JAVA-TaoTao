package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javassist.convert.TransformReadField;

public class TestActiveMq {
	@Test
	public void testQueueProducer() throws Exception{

		//1.创建一个连接工厂对象ConnectionFactory对象，需要指定Mq服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.88:61616");
		//2.使用ConnectionFactory创建连接
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用Connection对象创建一个session对象
		//第一个参数：是否开始事物，一般不使用事务，保证数据的最终一致，可以使用消息队列实现
		//如果一个参数为true，name第二个参数自动忽略；如果不开启事务。第二个蚕食为消息的应答模式，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象，两种形式 queue，topic,现在应该使用queue
		//参数：消息队列的名称
		Queue queue = session.createQueue("test-queue");
		//6.使用session对象创建Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个TestMessage对象
		//TextMessage textMessage = new ActiveMQTextMessage();
		//textMessage.setText("hello activemq");
		TextMessage textMessage = session.createTextMessage("hello activemq");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testQueueConsumer() throws Exception{
		//创建连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.88:61616");
		//使用连接工厂对象创建连接
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Destination，应该和消息的发送端一致
		Queue queue = session.createQueue("test-queue");
		//使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		//向consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//取消息内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						
						//打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		//系统等待接受消息
		/*while(true){
			Thread.sleep(100);
		}*/
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
		
		
	}
	
	//topic
	//Producer
	@Test
	public void testTopicProduer() throws Exception{
		//创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.88:61616");
		//创建连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建Destination，应该使用topic
		Topic topic = session.createTopic("test-topic");
		//创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		//创建一个TextMessager对象
		TextMessage textMessage = session.createTextMessage("hello activemq topic");
		//发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testTopicConsumser() throws Exception{
		//创建连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.88:61616");
		//使用连接工厂对象创建连接
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Destination，应该和消息的发送端一致
		Topic topic = session.createTopic("test-topic");
		//使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		//向consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//取消息内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						
						//打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		//系统等待接受消息
		/*while(true){
			Thread.sleep(100);
		}*/
		System.out.println("topic消费者2。。。。");
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
		

	}
}
