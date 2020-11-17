package com.ethan.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class Consumer3 {

    public static void main(String[] args) throws Exception{


        final Channel channel = RabbitMQUtils.getChannel("routing");

        channel.basicQos(1);

        channel.exchangeDeclare("logs-direct", "direct");

        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, "logs-direct", "warn");

        channel.basicConsume(queue, false, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("消费者3： " + new String(body));

                //手动确认
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
