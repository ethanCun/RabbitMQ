package com.ethan.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class Consumer2 {

    public static void main(String[] args) throws Exception{


        Channel channel = RabbitMQUtils.getChannel("fanout");

        //绑定交换机
        channel.exchangeDeclare("logs", "fanout");

        //交换机绑定临时队列
        String queue = channel.queueDeclare().getQueue();

        //绑定交换机和队列
        //第三个参数：路由key, 在广播里面无意义
        channel.queueBind(queue, "logs", "");

        //消费消息
        channel.basicConsume(queue, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("消费者1： " + new String(body));
            }
        });
    }
}
