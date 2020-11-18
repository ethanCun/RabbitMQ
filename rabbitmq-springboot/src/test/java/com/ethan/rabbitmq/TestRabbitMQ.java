package com.ethan.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RabbitMQ_Application.class)
@RunWith(SpringRunner.class)
public class TestRabbitMQ {

    //模板对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //注意， 生产者发送消息后，并不会马上创建队列， 只有消费者过来消费了才会创建队列
    //注意： 测试类和应用类会开启两个进程来消费消息， 所以这里测试不用启动应用类
    @Test
    public void simple(){

        for (int i = 0; i < 10; i++) {

            rabbitTemplate.convertAndSend("hello", "simple msg");

        }
    }

    //work queues模型
    @Test
    public void workQueues(){

        for (int i=0; i<15; i++){

            this.rabbitTemplate.convertAndSend("work", "workqueues:" + String.valueOf(i));
        }
    }

    //只有消费者来消费才会创建交换机
    @Test
    public void fanout(){

        for (int i=0; i<20; i++){

            //广播模式 路由key无意义
            this.rabbitTemplate.convertAndSend("logs", "", "广播模式fanout");
        }
    }


    @Test
    public void routing(){

        for (int i = 0; i < 5; i++) {

            this.rabbitTemplate.convertAndSend("direct", "info", "direct路由交换机");
        }
    }

    @Test
    public void topic(){

        for (int i = 0; i < 5; i++) {

            this.rabbitTemplate.convertAndSend("topic", "user.info", "topic路由交换机");
        }
    }

}
