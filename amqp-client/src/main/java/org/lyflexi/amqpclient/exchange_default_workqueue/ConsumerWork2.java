package org.lyflexi.amqpclient.exchange_default_workqueue;

import com.rabbitmq.client.*;
import org.lyflexi.amqpclient.util.ConnectionUtil;

import java.io.IOException;

public class ConsumerWork2 {

    static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("Consumer2 body："+new String(body));

            }

        };

        channel.basicConsume(QUEUE_NAME,true,consumer);

    }

}
