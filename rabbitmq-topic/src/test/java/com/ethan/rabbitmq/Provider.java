package com.ethan.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

//topic发布订阅模型 动态路由 类似消费者通过正则匹配来匹配对应的队列进行消息消费
@SpringBootTest
public class Provider {

    @Test
    public void test() throws Exception{

        Channel channel = RabbitMQUtils.getChannel("topic");

        channel.exchangeDeclare("logs-topic", "topic");

        //发布消息
        channel.basicPublish("logs-topic", "czy",
                null, "topic动态路由".getBytes());

        RabbitMQUtils.close();
    }

}
