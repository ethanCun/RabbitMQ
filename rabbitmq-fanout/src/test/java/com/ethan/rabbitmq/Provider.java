package com.ethan.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class Provider {

    //rabbitmq模型之fanout 广播模式 所有的消费者都能收到 相当于RocketMQ的广播模式
    @Test
    public void test() throws Exception{

        Channel channel = RabbitMQUtils.getChannel("fanout");

        //将通道声明到广播类型的交换机
        //参数1： 交换机名称  参数2：交换机类型
        channel.exchangeDeclare("logs", "fanout");

        //发送消息到交换机 广播类型的routingkey无意义
        channel.basicPublish("logs", "", null,
                "fanout message".getBytes());

        //关闭连接
        RabbitMQUtils.close();
    }
}
