package com.ethan.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class Consumer4 {

    public static void main(String[] args) throws Exception {


        final Channel channel = RabbitMQUtils.getChannel("routing");

        //消费者1次从通道中获取1条消息
        channel.basicQos(1);

        //通道绑定交换机
        channel.exchangeDeclare("logs-direct", "direct");

        //创建临时队列
        String queue = channel.queueDeclare().getQueue();

        //队列绑定交换机
        channel.queueBind(queue, "logs-direct", "error");

        //消费消息
        channel.basicConsume(queue, false, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("消费者1： " + new String(body));

                //手动确认消息被消费 1次值确认一条
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }
}
