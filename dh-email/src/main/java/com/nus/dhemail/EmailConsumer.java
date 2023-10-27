package com.nus.dhemail;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.nus.dhmodel.dto.EmailContent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

  @RabbitListener(queues = {"${queue.name}"})
  public void receive(@Payload String fileBody) {
    EmailContent emailContent = JacksonUtils.toObj(fileBody, EmailContent.class);
    EmailUtil.sendEmail(emailContent);
  }

}

