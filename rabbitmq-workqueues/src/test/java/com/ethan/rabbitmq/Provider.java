package com.ethan.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Provider {

    //RabbitMQ work queues 工作队列
    @Test
    public void test() throws Exception{

        Channel channel = RabbitMQUtils.getChannel("work");

        //通过通道发送消息
        for (int i=0; i<10; i++){

            channel.basicPublish("", "work", null, ("work queues" + i).getBytes());
        }

        //关闭连接
        RabbitMQUtils.close();
    }

}
