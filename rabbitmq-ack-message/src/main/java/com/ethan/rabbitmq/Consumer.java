package com.ethan.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class Consumer {

    private int i = 0;

    @RabbitListener(queuesToDeclare = @Queue("queue.name.simple"))
    public void receive(Message msg, Channel channel) throws IOException, InterruptedException {

        i++;
        System.out.println("简单类型消息: " + msg);

       // TimeUnit.SECONDS.sleep(10);

//        if (i < 10){
//
//            //true： 确认消息已经被消费
//            channel.basicAck(msg.getMessageProperties().getDeliveryTag(),
//                    false);
//        }

        channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queuesToDeclare = @Queue("queue.name.work"))
    public void workQueueReceive1(Message message, Channel channel) throws IOException {

        System.out.println("工作队列消息: " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queuesToDeclare = @Queue("queue.name.work"))
    public void workQueueReceive2(Message message, Channel channel) throws IOException {

        System.out.println("工作队列消息: " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queuesToDeclare = @Queue("queue.name.fanout"))
    public void fanoutReceive(Message message, Channel channel) throws IOException {

        System.out.println("广播模式消息： " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queuesToDeclare = @Queue("queue.name.direct"))
    public void directReceive(Message message, Channel channel) throws IOException {

        System.out.println("固定路由模式匹配: " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    @RabbitListener(queuesToDeclare = @Queue("queue.name.topic"))
    public void topicReceive(Message message, Channel channel) throws IOException {

        System.out.println("topic模糊路由模式： " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    @RabbitListener(queuesToDeclare = @Queue("queue.name.header"))
    public void headerReceive(Message message, Channel channel) throws IOException {

        System.out.println("header路由模式： " + message);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    //消费是在死信队列里面消费， 生产是在延时队列里面生产
    //@RabbitListener(queuesToDeclare = @Queue("queue.name.dlx"))
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue("queue.name.dlx"),
//                    exchange = @Exchange(value = "exchange.name.dlx", type = "topic"),
//                    key = {"routingkey.name.dlx"}
//            )
//    })
    public void ttlReceive(Message message, Channel channel) throws IOException {

        System.out.println("ttl延时消息: " + message);
        System.out.println("接受时间： " + new Date());

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
