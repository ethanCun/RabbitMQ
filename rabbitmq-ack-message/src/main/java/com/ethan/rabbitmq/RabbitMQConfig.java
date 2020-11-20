package com.ethan.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


@Configuration
public class RabbitMQConfig {

    //simple
    public static final String QUEUE_NAME_SIMPLE = "queue.name.simple";

    //work queue
    public static final String QUEUE_NAME_WORKQUEUE1 = "queue.name.work";

    //fanout
    public static final String QUEUE_NAME_FANOUT = "queue.name.fanout";
    public static final String EXCHANGE_NAME_FANOUT = "exchange.name.fanout";

    //routing key direct 固定路由匹配
    public static final String QUEUE_NAME_DIRECT = "queue.name.direct";
    public static final String EXCHANGE_NAME_DIRECT = "exchange.name.direct";
    public static final String ROUTING_KEY_NAME_DIRECT = "routing_key.name.direct";

    //topic 通用路由匹配
    public static final String QUEUE_NQME_TOPIC = "queue.name.topic";
    public static final String EXCHANGE_NAME_TOPIC = "exchange.name.topic";
    public static final String ROUTING_KEY_NAME_TOPIC = "topic.#";

    //header
    public static final String QUEUE_NAME_HEADER = "queue.name.header";
    public static final String EXCHANGE_NAME_HEADER = "exchange.name.header";


    //ttl dlx
    //延时
    public static final String QUEUE_NAME_TTL = "queue.name.ttl";
    //死信
    public static final String QUEUE_NAME_DLX = "queue.name.dlx";
    public static final String EXCHANGE_NAME_DLX = "exchange.name.dlx";
    public static final String ROUTING_KEY_NAME_DLX = "routingkey.name.dlx";


    //simple
    @Bean
    public Queue simpleQueue(){

        return QueueBuilder.durable(QUEUE_NAME_SIMPLE).build();
    }

    //work queue
    @Bean
    public Queue workQueue(){

        return QueueBuilder.durable(QUEUE_NAME_WORKQUEUE1).build();
    }


    //fanout
    @Bean
    public Queue fanoutQueue(){

        return QueueBuilder.durable(QUEUE_NAME_FANOUT).build();
    }

    @Bean
    public FanoutExchange fanoutExchange(){

        return (FanoutExchange) ExchangeBuilder.fanoutExchange(EXCHANGE_NAME_FANOUT)
                .build();
    }


    @Bean
    public Binding fanoutBinding(@Qualifier("fanoutQueue") Queue queue,
                                 @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){

        return BindingBuilder.bind(queue).to(fanoutExchange);
    }


    //direct
    @Bean
    public Queue directQueue(){

        return QueueBuilder.durable(QUEUE_NAME_DIRECT).build();
    }

    @Bean
    public DirectExchange directExchange(){

        return (DirectExchange) ExchangeBuilder.directExchange(EXCHANGE_NAME_DIRECT)
                .build();
    }

    @Bean
    public Binding directBinding(@Qualifier("directQueue") Queue queue,
                                 @Qualifier("directExchange") DirectExchange directExchange){

        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(ROUTING_KEY_NAME_DIRECT);

    }

    //topic
    @Bean
    public Queue topicQueue(){

        return QueueBuilder.durable(QUEUE_NQME_TOPIC).build();
    }

    @Bean
    public TopicExchange topicExchange(){

        return (TopicExchange) ExchangeBuilder.topicExchange(EXCHANGE_NAME_TOPIC)
                .build();
    }

    @Bean
    public Binding topicBinding(@Qualifier("topicQueue") Queue topicQueue,
                                @Qualifier("topicExchange") Exchange exchange){

        return BindingBuilder.bind(topicQueue)
                .to(exchange)
                .with(ROUTING_KEY_NAME_TOPIC) //绑定路由key
                .noargs();
    }



    //headers
    @Bean
    public Queue headerQueue(){

        return QueueBuilder.durable(QUEUE_NAME_HEADER).build();
    }

    @Bean
    public HeadersExchange headerExchange(){

        return (HeadersExchange) ExchangeBuilder.headersExchange(EXCHANGE_NAME_HEADER)
                .build();
    }

    @Bean
    public Binding headerBinding(@Qualifier("headerQueue") Queue queue,
                                 @Qualifier("headerExchange") HeadersExchange exchange){

        HashMap<String, Object> headers = new HashMap<String, Object>();
        headers.put("name", "czy");
        headers.put("age", 20);

        //绑定headersExchange
        return BindingBuilder
                .bind(queue)
                .to(headerExchange())
                //.whereAll(headers) //表示生产者的MessageProperties header中信息完全一致才能发送消息
                .whereAny(headers) //表示只要包含任何一个键值对就能发送消息
                .match();
    }


    //ttl 延时队列
    @Bean
    public Queue ttlQueue(){

        return QueueBuilder.durable(QUEUE_NAME_TTL)
                //指定死信交换机 如果消息过期就投递到这个交换机
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME_DLX)
                //指定死信路由 如果消息过期就指定死信交换机根据这个路由来投递消息
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_NAME_DLX)
                //指定延时队列所有消息过期时间
                .withArgument("x-message-ttl", 30000) //30s后消息过期
                //设置队列长度
                .withArgument("x-max-length", 10)
                .build();
    }

    /**
     *
     * 死信队列： 需要特别注意的是：生产者将消息发布到的是ttl延时队列，
     *  消费者是在死信队列里面消费消息
     * @return
     */
    //dlx 死信队列
    @Bean
    public Queue dlxQueue(){

        return QueueBuilder.durable(QUEUE_NAME_DLX)
                .build();
    }

    //延时队列交换机
    @Bean
    public Exchange ttlExchange(){

        return ExchangeBuilder.topicExchange(EXCHANGE_NAME_DLX)
                .build();
    }

    //死信队列绑定到延时交换机 再通过路由key绑定延时队列
    @Bean
    public Binding ttlBinding(@Qualifier("dlxQueue") Queue queue,
                              @Qualifier("ttlExchange") Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange)
                .with(ROUTING_KEY_NAME_DLX).noargs();
    }


}
