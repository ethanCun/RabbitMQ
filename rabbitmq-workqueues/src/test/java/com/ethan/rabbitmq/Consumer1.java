package com.ethan.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

//work queues模型 拥有对个消费者
public class Consumer1 {

    public static void main(String[] args) throws Exception {

        final Channel channel = RabbitMQUtils.getChannel("work");

        //设置每次只能消费一个消息
        channel.basicQos(1);

        //false: 关闭消费者自动确认消费机制
        channel.basicConsume("work", false, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                System.out.println("消费者1： " + new String(body));

                //手动确认消息消费机制
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        //无需关闭通道
    }
}
