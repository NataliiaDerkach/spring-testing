package com.example.jmc;


import jakarta.persistence.EntityManagerFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@Configuration
@EnableJms
public class MessagingConfiguration {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

   private static final Logger log = LoggerFactory.getLogger(MessagingConfiguration.class);

    public static final String MESSAGE_QUEUE = "message-queue";
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                new ActiveMQConnectionFactory(user, password, brokerUrl));
        factory.setClientId("myclientId");
        factory.setSessionCacheSize(100);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory());
        factory.setMessageConverter(messageConverter());
        //   factory.setTransactionManager(transactionManager());
        factory.setErrorHandler(t->{
            log.info(" Handling error in Listener for messages, error: " + t.getMessage());
        });
        return factory;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        return new JmsTransactionManager(cachingConnectionFactory());
//    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate=new JmsTemplate(cachingConnectionFactory());
        jmsTemplate.setMessageConverter(messageConverter());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }


    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
