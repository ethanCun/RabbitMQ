package com.ethan.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = RabbitMQ_delay_Application.class)
@RunWith(SpringRunner.class)
public class Provider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send(){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "czy");
        map.put("age", 20);

        this.rabbitTemplate.convertAndSend("delayQueue", map, new MessagePostProcessor() {

            public Message postProcessMessage(Message message) throws AmqpException {

                //设置消息延时 延时10s
                message.getMessageProperties().setExpiration("10000");

                return message;
            }
        });

        System.out.println("发送成功： " + new Date());


    }
}
