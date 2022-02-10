package com.ozoz.springkafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: cnljj1995@gmail.com
 * @Date: 2022/2/10
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class MsgController {

    private static final String TOPIC_TEST = "ozoz.test";

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/v1")
    public ResponseEntity<String> sendMsg(int num) {

        kafkaTemplate.send(TOPIC_TEST,"你发送的数字是:" + num).addCallback(
                success -> log.info("发送成功：topic = {},partition = {},offset = {}",
                            success.getRecordMetadata().topic(),
                            success.getRecordMetadata().partition(),
                            success.getRecordMetadata().offset()),
                failure -> log.info("消息发送失败"));
        return ResponseEntity.ok("ok");
    }


    /**
     * 通过注解添加事务
     * 如果用申明式事务，可使用kafkaTemplate.executeInTransaction()方法
     * @param num number
     * @return ok
     */
    @GetMapping("/v2")
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> sendMsg2(int num) {

        kafkaTemplate.send(TOPIC_TEST,"发送不为0的数字:{}" + num);

        if (num == 0) {
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_TEST,"不应该发送数字2:{}" + num);

        return ResponseEntity.ok("ok");
    }
}
