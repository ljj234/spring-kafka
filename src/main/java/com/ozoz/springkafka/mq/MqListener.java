package com.ozoz.springkafka.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author: cnljj1995@gmail.com
 * @Date: 2022/2/10
 */
@Component
@Slf4j
public class MqListener {

    @KafkaListener(topics = {"ozoz.test"}, groupId = "g1")
    public void listen(ConsumerRecord<?,?> record, Acknowledgment ack) {

        log.info("消费者收到的消息是:{},topic={},partition={},offset={}",
                record.value(),
                record.topic(),
                record.partition(),
                record.offset()
        );

        // 提交偏移量
        ack.acknowledge();
    }
}
