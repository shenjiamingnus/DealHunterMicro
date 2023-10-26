package com.nus.dhemail;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

  @RabbitListener(queues = {"${queue.name}"})
  public void receive(@Payload String fileBody) {

    String[] args = fileBody.split("--");
    EmailUtil.sendEmail(args[0], args[1], Double.parseDouble(args[2]));
  }

}

