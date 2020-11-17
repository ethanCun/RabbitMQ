package com.ethan.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

//routing路由相当于是广播的子集， 多个消费者筛选自己感兴趣的消息进行消费
//Routing订阅模型直连 Direct
//这个时候交换机不再把消息交给每一个绑定的队列， 而是根据消息的Routing key判断，只有队列
//的Routing key和消息的Routing key一致时，交换机才将消息交给该队列；
@SpringBootTest
public class Provider {

    @Test
    public void test() throws Exception{

        Channel channel = RabbitMQUtils.getChannel("routing");

        //通道绑定交换机
        //交换机类型： direct 路由模式
        channel.exchangeDeclare("logs-direct", "direct");

        //发送消息
        channel.basicPublish("logs-direct", "info", null,
                "direct模型发布基于routingkey的消息: info".getBytes());

        //关闭
        RabbitMQUtils.close();

    }
}
