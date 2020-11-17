package com.ethan.rabbitmq;

import com.ethan.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class Consumer {

    //注意consume在test里面， 不能接收到队列中的消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel("cc");

        //消费消息
        //参数1： 消费队列的名称
        //参数2： 开启消息自动确认机制
        channel.basicConsume("cc", true, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                System.out.println(new String(body));

            }
        });


        //不关闭消费者会一直监听
        //RabbitMQUtils.close();
    }
}
