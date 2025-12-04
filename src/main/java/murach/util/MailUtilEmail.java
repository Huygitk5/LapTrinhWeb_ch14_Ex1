package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilEmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {
        
//        String smtpHost = System.getenv("smtp-relay.brevo.com");       // v.d: smtp-relay.brevo.com
//        String smtpPort = System.getenv("587");       // v.d: 587 hoặc 2525
//        String smtpUsername = System.getenv("9d4a84001@smtp-brevo.com"); // Email login của Brevo/SendGrid
//        String smtpPassword = System.getenv("xsmtpsib-a64876cda3188efa0b3b652d7f10a8a52a1cbe92c404321ca634dc45f17aab64-bp7mgP1HDcKEgijs"); // Key SMTP của Brevo/SendGrid

        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587"; 
        String smtpUsername = "9d4a84001@smtp-brevo.com";
        String smtpPassword = "xsmtpsib-a64876cda3188efa0b3b652d7f10a8a52a1cbe92c404321ca634dc45f17aab64-bp7mgP1HDcKEgijs";
// Nếu chạy localhost (chưa có env), có thể fallback về giá trị mặc định để test (nhưng KHÔNG khuyến khích hardcode production)
        if (smtpHost == null) {
             // Fallback logic hoặc ném lỗi
             System.out.println("Chưa cấu hình biến môi trường gửi mail!");
             return; 
        }

        // 1 - get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        
//        props.put("mail.smtps.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
//        Session session = Session.getDefaultInstance(props);
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });
        session.setDebug(true);
        
        // 2 - create a message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=utf-8");
        } else {
            message.setText(body);
        }
        // 3 - address the message
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - send the message
//        Transport transport = session.getTransport();
//        transport.connect("quochuydoan2005@gmail.com", "hwia ielt wjxj obve");
//        transport.sendMessage(message, message.getAllRecipients());
//        transport.close();
        Transport.send(message);
    }
}