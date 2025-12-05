package murach.util;

import java.util.Properties;
import jakarta.mail.*; 
import jakarta.mail.internet.*;

public class MailUtilEmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1. Cấu hình SMTP cho Brevo (Sửa lại cho đúng chuẩn STARTTLS) [cite: 333]
        Properties props = new Properties();
        
        // QUAN TRỌNG: Dùng 'smtp' thay vì 'smtps' cho cổng 2525/587
        props.put("mail.transport.protocol", "smtp"); 
        
        props.put("mail.smtp.host", "smtp-relay.brevo.com");
        props.put("mail.smtp.port", "587"); // Nếu 587 bị chặn, đổi thành 2525
        props.put("mail.smtp.auth", "true");
        
        // BẮT BUỘC: Phải bật STARTTLS cho cổng 587/2525
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.starttls.required", "true");
        
        // Trust chứng chỉ để tránh lỗi xác thực trên môi trường Cloud
        props.put("mail.smtp.ssl.trust", "smtp-relay.brevo.com");
        
        Session session = Session.getInstance(props, null);
        session.setDebug(true); // Xem log lỗi [cite: 340]

        // 2. Tạo nội dung email [cite: 346]
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // 3. Thiết lập người gửi và nhận [cite: 357]
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4. Gửi mail
        try (Transport transport = session.getTransport("smtp")) {      
            
            // Lấy key từ biến môi trường
            String smtpKey = "xsmtpsib-d7a8b3fb130922b3782ea3da764b5ae69c8ceec694f7785c710b422c57d8b534-UaKAOMwyabxKltCI";

            // Nếu không tìm thấy biến môi trường (khi chạy local mà chưa cài đặt)
            if (smtpKey == null || smtpKey.isEmpty()) {
                // TUYỆT ĐỐI KHÔNG ĐIỀN KEY THẬT VÀO ĐÂY KHI PUSH GITHUB
                // Hãy ném ra lỗi để mình biết mà cấu hình lại máy tính
                throw new MessagingException("LỖI: Chưa cấu hình biến môi trường BREVO_SMTP_KEY trên máy tính/server!");
            }

            transport.connect("smtp-relay.brevo.com", "9d52bf001@smtp-brevo.com", smtpKey);
            transport.sendMessage(message, message.getAllRecipients());
        }
    }
}