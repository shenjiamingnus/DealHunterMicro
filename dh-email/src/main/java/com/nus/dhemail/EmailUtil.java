package com.nus.dhemail;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

  @Autowired
  private JavaMailSender mailSender;

  private static JavaMailSender javaMailSender;

  @PostConstruct
  public void init() {
    javaMailSender = mailSender;
  }

  @Value("${spring.mail.username}")
  public void setHost(String from) {
    EmailUtil.from = from;
  }

  private static String from;

  public static boolean sendEmail(String userEmail, String productName, double newLowestPrice){
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
    try {
      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(userEmail);
      mimeMessageHelper.setSubject("LowestPrice Update for " + productName);
      mimeMessageHelper.setText("The newLowestPrice for " + productName + " has been updated to " + newLowestPrice,true);
      javaMailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
