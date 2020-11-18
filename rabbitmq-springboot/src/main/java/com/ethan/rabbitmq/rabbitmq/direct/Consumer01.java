package com.ethan.rabbitmq.rabbitmq.direct;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//Routing模型
@Component("direct")
public class Consumer01 {


    @RabbitListener(bindings = {

            @QueueBinding(
                    value = @Queue, //临时队列
                    exchange = @Exchange(value = "direct", type = "direct"), //绑定交换机
                    key = {"info", "error"} //路由key
            )
    })
    public void receive01(Object msg){

        System.out.println("direct consumer1: " +msg);
    }

    @RabbitListener(bindings = {

            @QueueBinding(
                    value = @Queue, //临时队列
                    exchange = @Exchange(value = "direct", type = "direct"), //绑定交换机
                    key = {"info"}
            )
    })
    public void receive02(Object msg){

        System.out.println("direct consumer2: " +msg);
    }
}
