package com.study.rabbit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.AMQP.Exchange;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

/**
 * 死信队列相关配置
 * <p>
 * <p>
 * 消息延时处理
 * deadLetterExchange()声明了一个Direct 类型的Exchange （死信队列跟交换机没有关系）
 * <p>
 * deadLetterQueue() 声明了一个队列 这个队列 跟前面我们声明的队列不一样 注入了 Map<String,Object> 参数 下面的概念非常重要
 * <p>
 * x-dead-letter-exchange 来标识一个交换机 x-dead-letter-routing-key 来标识一个绑定键（RoutingKey） 这个绑定键 是分配给 标识的交换机的 如果没有特殊指定 声明队列的原routingkey , 如果有队列通过此绑定键 绑定到交换机 那么死信会被该交换机转发到 该队列上 通过监听 可对消息进行消费
 * <p>
 * 可以打个比方 这个是为主力队员 设置了一个替补 如果主力 “死”了 他的活 替补接手 这样更好理解
 * <p>
 * deadLetterBinding() 对这个带参队列 进行了 和交换机的规则绑定 等下 消费者 先把消息通过交换机投递到该队列中去 然后制造条件发生“死信”
 * <p>
 * redirectBinding() 我们需要给标识的交换机 以及对其指定的routingkey 来绑定一个所谓的“替补”队列 用来监听
 * <p>
 * 流程具体是 消息投递到 DL_QUEUE 10秒后消息过期 生成死信 然后转发到 REDIRECT_QUEUE 通过对其的监听 来消费消息
 */
@Configuration
public class DeadQueueConfig {

    /**
     * 死信队列 交换机标识符
     */
    private static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    /**
     * 死信队列交换机绑定键标识符
     */
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange 不影响该类型交换机的特性.
     */
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange() {
        // return (DirectExchange)
        // ExchangeBuilder.directExchange("DL_EXCHANGE").durable(true).build();
        return new DirectExchange("DL_EXCHANGE", true, false);
    }

    /**
     * 声明一个死信队列. x-dead-letter-exchange 对应 死信交换机 x-dead-letter-routing-key 对应
     * 死信队列
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange 声明 死信交换机
        args.put(DEAD_LETTER_QUEUE_KEY, "DL_EXCHANGE");
        // x-dead-letter-routing-key 声明 死信路由键
        args.put(DEAD_LETTER_ROUTING_KEY, "KEY_R");
        return QueueBuilder.durable("DL_QUEUE").withArguments(args).build();
    }

    /**
     * 定义死信队列转发队列.
     *
     * @return the queue
     */
    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable("REDIRECT_QUEUE").build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding("DL_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "DL_KEY", null);
    }

    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding("REDIRECT_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "KEY_R", null);
    }

//    /**
//     * 定义正常接收的队列
//     *
//     * @return the queue
//     */
//    @Bean("tagetQueue")
//    public Queue tagetQueue() {
//        return QueueBuilder.durable("First_Destina").build();
//    }
//
//    @Bean
//    public Binding destinationQueue() {
//        return new Binding("First_Destina", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "DL_KEY", null);
//    }
}

