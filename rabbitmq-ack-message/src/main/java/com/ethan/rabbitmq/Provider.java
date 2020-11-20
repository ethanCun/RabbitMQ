package com.ethan.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
public class Provider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/ttl")
    public String ttl() throws Exception{

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("date", new Date());
        map.put("content", "这是延时队列消息");

        System.out.println("发送时间： " + new Date());


        //注意延时队列， 发送是往延时队列里面发送， 比如生产订单队列
        //消费是在延时队列里面消费， 比如取消订单的队列

        //指定消息的过期时间
//        this.rabbitTemplate.convertSendAndReceive(
//                "queue.name.ttl",
//                map,
//                new MessagePostProcessor() {
//
//                    //消息处理器
//                    public Message postProcessMessage(Message message) throws AmqpException {
//
//                        message.getMessageProperties().setExpiration("10000");
//
//                        return message;
//                }
//        });

        for (int i = 0; i < 10; i++) {

            //不指定消息的过期时间， 统一设置队列里面所有消息的过期时间
            this.rabbitTemplate.convertAndSend("queue.name.ttl", map);
        }

        /**
         * 1、confirm机制，消息的确认，是指生产者投递消息之后，
         * 如果Broker收到消息，则会给生产者一个应答，生产者能接收应答，
         * 用来确定这条消息是否正常的发送到Broker，
         * 这种机制是消息可靠性投递的核心保障。
         * confirm机制是只保证消息到达exchange，
         * 并不保证消息可以路由到正确的queue。
         */
        this.rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("消息已经发送到exchange， 并放入broker中的回调");
            }
        });

        /**
         *
         * 2. return机制，用于处理一些不可路由的消息，
         * 在一些特殊的情况下，当前的exchange不存在或者指定的路由key路由不到，
         * 这时如果我们需要及时监听这种消息，就需要return机制。
         */
        this.rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

                System.out.println("消息放入broker， 但是没有消费者消费回调");
            }
        });

        return "success";
    }
}
