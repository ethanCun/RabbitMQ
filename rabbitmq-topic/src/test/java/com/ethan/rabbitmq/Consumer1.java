package com.ethan.rabbitmq;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class Consumer1 {

    public static void main(String[] args) throws Exception{

        final Channel channel = RabbitMQUtils.getChannel("topic");

        //一次从临时队列中获取1条消息
        channel.basicQos(1);

        //通道绑定交换机
        channel.exchangeDeclare("logs-topic", "topic");

        //创建临时队列
        String queue = channel.queueDeclare().getQueue();

        //临时队列绑定交换机 使用动态通配符绑定
        //routing key 以点.分隔
        // *: 匹配一个单词
        // #: 匹配0个或者多个单词
        //channel.queueBind(queue, "logs-topic", "czy.*");
        channel.queueBind(queue, "logs-topic", "czy.#");

        //消费
        channel.basicConsume(queue, false, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("消费者1： " + new String(body));

                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }
}
