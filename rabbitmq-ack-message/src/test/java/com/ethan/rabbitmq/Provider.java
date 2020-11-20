package com.ethan.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.text.resources.CollationData;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest(classes = RabbitMQ_Applicaiton.class)
@RunWith(SpringRunner.class)
public class Provider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //simple
    @Test
    public void simple(){

        for (int i = 0; i < 10; i++) {

            MessageProperties messageProperties = new MessageProperties();
            //设置消息过期时间为3s
            messageProperties.setHeader("x-message-ttl", 3*1000);

            Message message = new Message("简单消息".getBytes(),
                    messageProperties);

            this.rabbitTemplate.convertAndSend("queue.name.simple", message);
        }

        //消息返回, yml需要配置 publisher-returns: true
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            public void returnedMessage(Message message,
                                        int replyCode,
                                        String replyText,
                                        String exchange,
                                        String routingKey) {

                System.out.println("消息返回: ");
                System.out.println(message);
            }
        });

        //生产者与broker之间的消息确认称为public confirms，
        // public confirms机制用于解决生产者与Rabbitmq服务器之间消息可靠传输，
        // 它在消息服务器持久化消息后通知消息生产者发送成功。
        //消息确认, yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            public void confirm(CorrelationData correlationData,
                                boolean ack,
                                String cause) {

                System.out.println("消息已经成功发送到rabbitmq并落实到broker");
            }
        });

    }

    //work queues
    @Test
    public void workqueues() throws Exception{

        this.rabbitTemplate.convertAndSend("queue.name.work",
                "这是work queue消息");
    }

    //fanout
    @Test
    public void fanout(){

        //广播模式不需要routingKey
        this.rabbitTemplate.convertAndSend("exchange.name.fanout",
                null, "这是广播模式消息");
    }

    //direct
    @Test
    public void direct(){

        //固定路由匹配
        this.rabbitTemplate.convertAndSend("exchange.name.direct",
                "routing_key.name.direct", "这是direct模式固定路由匹配");

        this.rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("消息已经成功投递");
            }
        });

        //找不到相关路由的回调
        this.rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

                System.out.println("找不到相关路由的回调...");
            }
        });
    }

    //topic 手动确认direct模型队列消息
    @Test
    public void topic() throws Exception{

        this.rabbitTemplate.convertAndSend("exchange.name.topic",
                "topic.me",
                "创建一个订单");
    }

    //headers 手动确认header模型队列消息
    @Test
    public void headers() throws Exception{

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("name", "czy");
        //messageProperties.setHeader("age", 20);

        Message message = new Message("这是一个header模型交换信息".getBytes(), messageProperties);

        //headersExchange不需要路由key
        this.rabbitTemplate.convertAndSend("exchange.name.header",
                null, message);
    }

    //ttl
    @Test
    public void ttl() throws Exception{

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("date", new Date());
        map.put("content", "这是延时队列消息");

        this.rabbitTemplate.convertSendAndReceive(
                "queue.name.ttl",
                map);
    }

}
