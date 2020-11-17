package com.ethan.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQUtils {

    public static ConnectionFactory connectionFactory;
    public static Connection connection;
    public static Channel channel;

    static {

        //ConnectionFactory是一个重量级别的类，在类加载的时候执行
        connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("49.235.231.176");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("vh01");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("123456");
    }

    public static Connection getConnection(){

        try {
            connection = connectionFactory.newConnection();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    public static Channel getChannel(String queue){

        try {
            channel = getConnection().createChannel();

            //通道绑定对应的消息队列
            //参数1： 队列名称， 不存在就自动创建
            //参数2： D, 队列是否需要持久化 mq关闭的时候时候将队列持久化到磁盘， 为false时
            //      队列会在rabbitmq服务重新启动的时候删除该队列, 但是队列的消息仍会删除
            //      解决：在发送消息的时候设置消息的持久化：MessageProperties.PERSISTENT_TEXT_PLAIN,
            //参数3： 是否独占队列，当前队列是否只当前连接可用, 为true时其他连接链接时会报错
            //参数4： AD, 是否在消费完成后并且消费者断开连接之后， 自动删除队列
            //参数5： 额外参数
            channel.queueDeclare(queue, true, false, false,
                    null);

            return channel;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void close(){

        try {
            if (channel != null)channel.close();
            if (connection != null)connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
