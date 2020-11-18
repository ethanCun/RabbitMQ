package com.ethan.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //创建延时队列
    @Bean("delayQueue")
    public Queue delayQueue(){

        return QueueBuilder.durable("delayQueue")
                .withArgument("x-dead-letter-exchange", "my-dlx-exchange")
                .withArgument("x-dead-letter-routing-key", "routing-key-delay")
                .build();
    }


    //消息延时未处理后 处理该延时消息的死信队列
    @Bean("dlxQueue")
    public Queue dlxQueue(){

        return QueueBuilder.durable("my-dlx-queue").build();
    }

    //死信交换机
    @Bean("dlxExchange")
    public Exchange dlxExchange(){

        return ExchangeBuilder.directExchange("my-dlx-exchange").build();
    }


    //绑定死信队列和死信交换机
    @Bean
    public Binding dlxBinding(@Qualifier("dlxExchange") Exchange exchange,
                              @Qualifier("dlxQueue") Queue queue){

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("routing-key-delay")
                .noargs();
    }


}


