package org.hankyu.simpleboard_v1.util;

import org.hankyu.simpleboard_v1.entity.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String fromEmailpassword;

    private Session session;

    public MailUtil() {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable", "true");

        this.session = Session.getDefaultInstance(prop, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(fromEmail, fromEmailpassword);
            }
        });

    }

    public boolean sendSignupMail(String key, Member member){
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(member.getEmail()));
            message.setSubject("이메일 인증코드 전송");

            message.setContent(
                    "<h1>[이메일 인증]</h1> <p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p> " +
                            "<a href='http://localhost:8080/member/signup/confirm?key="+key+"' target='_blenk'>이메일 인증 확인</a>"
                    ,"text/html;charset=euc-kr"
            );

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
