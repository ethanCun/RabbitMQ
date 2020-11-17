package com.ethan.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Consumer2 {

    public static void main(String[] args) throws Exception {

        final Channel channel = RabbitMQUtils.getChannel("work");

        //通道每次只能消费一个消息
        channel.basicQos(1);

        //参数2： 自动确认， 表示消费者向rabbitmq表示消息消费了, 确认之后队列就会将
        //分配给这个消费者的消息删除, 为true时这个会存在问题， 消费者可能没有消费消息，但是队列
        //已经将消息删除了
        //
        channel.basicConsume("work", false, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                System.out.println("消费者2： " + new String(body));

                //模拟消费者2消费较慢
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //手动确认
                //参数1： 手动确认消息表示
                //参数2： false： 每次确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        //无需关闭通道
    }
}
