package com.study.rabbit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class RabbitmqConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);

//    @Resource
//    private RabbitTemplate rabbitTemplate;

    /**
     * 定义一个队列
     * Queue 可以有四个参数
     * 1.队列名
     * 2.durable     持久化消息队列，rabbitmq充气的时候不需要创建新的队列，默认为true
     * 3.auto-delete 表示消息队列没有在使用时将被自动删除  默认为 false
     * 4.exclusive   表示该消息队列是否只在当前connection生效，默认为false
     *
     * @return
     */
    @Bean
    public Queue helloQueue() {
        return new Queue("queue_test");
    }

    /**
     * 定制化amqp模版
     * <p>
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     */
//    @Bean
//    public RabbitTemplate rabbitTemplate(RabbitTemplate rabbitTemplate) {
//        //消息发送失败返回到队列中，yml需要配置publisher-returns: true
//        rabbitTemplate.setMandatory(true);
//
//        //消息返回，yml需要配置 publisher-returns: true
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            String correlationId = message.getMessageProperties().getCorrelationId();
//            log.debug("消息：{}发送是啊比，应答码：{} 原因：{} 交换机：{} 路由键：{}", correlationId, replyCode, replyText, exchange, routingKey);
//        });
//        //消息确认，yml需要配置publisher-confirms: true
//        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
//            if (ack) {
//                log.debug("消息发送成功，id:{}", correlationData.getId());
//            } else {
//                log.debug("消息发送到exchange失败的原因：{}", cause);
//            }
//        }));
//        return rabbitTemplate;
//    }

    //
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private CachingConnectionFactory connectionFactory;
//
//    @Autowired
//    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;
//
//
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //
//
//    @Bean
//    @Qualifier(RabbitMQConstant.FIRST_SIMPLE_QUEUE)
//    Queue queue() {
//        return new Queue(RabbitMQConstant.FIRST_SIMPLE_QUEUE, false, false, true);
//    }

    @Bean(name = RabbitMQConstant.FIRST_SIMPLE_QUEUE)
    public Queue queue001() {
        return new Queue(RabbitMQConstant.FIRST_SIMPLE_QUEUE, false, false, false);
    }

    //
//    /**
//     * 单一消费者
//     *
//     * @return
//     */
//    @Bean(name = "singleListenerContainer")
//    public SimpleRabbitListenerContainerFactory listenerContainer() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setConcurrentConsumers(1);
//        factory.setMaxConcurrentConsumers(1);
//        factory.setPrefetchCount(1);
//        factory.setTxSize(1);
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        return factory;
//    }
//
//    /**
//     * 多个消费者
//     *
//     * @return
//     */
//    @Bean(name = "multiListenerContainer")
//    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factoryConfigurer.configure(factory, connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
//
//        //并发设置
//        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency", int.class));
//        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency", int.class));
//        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch", int.class));
//
//        return factory;
//    }
//
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
//        connectionFactory.setPublisherConfirms(true);
//        connectionFactory.setPublisherReturns(true);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);

        //消息返回，yml需要配置 publisher-returns: true
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.debug("消息：{}发送是啊比，应答码：{} 原因：{} 交换机：{} 路由键：{}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        //消息确认，yml需要配置publisher-confirms: true
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送成功");
            } else {
                log.debug("消息发送到exchange失败的原因：{}", cause);
            }
        }));


//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
//
//            }
//        });
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            @Override
//            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
//            }
//        });
        return rabbitTemplate;
    }
}