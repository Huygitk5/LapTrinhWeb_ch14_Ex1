package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilEmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1 - get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "sandbox.smtp.mailtrap.io");
        props.put("mail.smtps.port", "2525");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Bắt buộc
        props.put("mail.smtps.quitwait", "false");
//        Session session = Session.getDefaultInstance(props);
//        session.setDebug(true);

        String username = "fa27adc8690b25"; 
        String password = "1544ca9e25aa90";
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        session.setDebug(true);
        // 2 - create a message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }
        // 3 - address the message
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - send the message
        Transport transport = session.getTransport();
        transport.connect("quochuydoan2005@gmail.com", "hwia ielt wjxj obve");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
