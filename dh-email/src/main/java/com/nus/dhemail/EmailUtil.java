package com.nus.dhemail;

import com.nus.dhmodel.dto.EmailContent;
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

  public static boolean sendEmail(EmailContent emailContent){
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
    try {
      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(emailContent.getEmail());
      mimeMessageHelper.setSubject("LowestPrice Update for " + emailContent.getProductName());
      mimeMessageHelper.setText(
              "Dear Customer,\n\n" +
                      "We're thrilled to share some fantastic news with you! " +
                      "The product '" + emailContent.getProductName() + "' you've subscribed to has just hit a new lowest price.\n\n" +
                      "Now is the perfect time to grab this amazing deal and make your purchase.\n\n " +
                      "The new lowest price for " + emailContent.getProductName() + " has been updated to $" + emailContent.getPrice() + ".\n\n" +
                      "Don't miss out on the savings! Visit our website to explore the latest pricing and secure your order.\n\n" +
                      "Thank you for being a valued subscriber. We hope you enjoy your shopping experience with us!\n\n" +
                      "Best Regards,\n[Deal hunter]", true);
      javaMailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
