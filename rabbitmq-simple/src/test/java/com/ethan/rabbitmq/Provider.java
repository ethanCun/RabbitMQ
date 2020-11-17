package com.ethan.rabbitmq;

import com.ethan.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Provider {

    //rabbitmq直连: 简单的消息投放， 无交换机， 生产者直接将消息放到队列中
    @Test
    public void testSendMessage() throws Exception{

        //获取连接通道
        Channel channel = RabbitMQUtils.getChannel("cc");


        //通过通道发布消息
        //参数1： 交换机名称， 直连没有交换机， 为空
        //参数2： 队列名称
        //参数3： 传递消息额外设置, MessageProperties.PERSISTENT_TEXT_PLAIN:
        //                    在队列重启之后， 消息不会丢失，就是设置消息持久化
        //参数4： 消息内容
        channel.basicPublish("",
                "cc",
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "hello rabbitmq".getBytes());


        //关闭通道 关闭连接
        RabbitMQUtils.close();


    }
}
