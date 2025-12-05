package murach.email;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.MessagingException; // Import thư viện mail

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtilEmail;
 // Import class gửi mail


@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";
        String message = "";
        
        // Lấy action hiện tại
        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; // Mặc định là join
        }

        // Xử lý các action
        if (action.equals("join")) {
            url = "/index.jsp"; 
        } 
        else if (action.equals("add")) {
            // 1. Lấy dữ liệu từ form
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            

            User user = new User(email, firstName, lastName);

            // 2. KIỂM TRA TRÙNG LẶP (Logic từ file cũ của bạn)
            if (UserDB.emailExists(email)) {
                message = "This email address already exists.<br>" +
                          "Please enter another email address.";
                url = "/index.jsp"; // Quay lại trang nhập để báo lỗi
            } 
            else {
                // 3. NẾU KHÔNG TRÙNG -> LƯU VÀO DB
                UserDB.insert(user);
                
                // 4. GỬI EMAIL (Logic gửi mail)
                String to = email;
                String from = "genshinpart23@gmail.com"; 
                String subject = "Welcome to our email list";
                String body = "Dear " + firstName + ",\n\n"
                        + "Thanks for joining our email list. "
                        + "We'll make sure to send you announcements about new products and promotions.\n"
                        + "Have a great day!\n\n"
                        + "Mike Murach & Associates";
                
                boolean isBodyHTML = false;

                try {
                    // --- THÊM DÒNG NÀY ĐỂ KIỂM TRA ---
                    System.out.println("============================================");
                    System.out.println("DEBUG: ĐANG GỌI MAIL UTIL GMAIL (BREVO)...");
                    System.out.println("Email: " + email);
                    System.out.println("============================================");
                    // ----------------------------------
                    
                    MailUtilEmail.sendMail(to, from, subject, body, isBodyHTML);
                    url = "/thanks.jsp"; // Gửi thành công thì sang trang cảm ơn
                } catch (MessagingException e) {
                    // Nếu lỗi gửi mail thì vẫn báo lỗi nhưng đã lưu DB rồi
                    message = "ERROR: Unable to send email. " + e.getMessage();
                    this.log("Unable to send email: " + message);
                    // Có thể chọn về lại trang email hoặc vẫn sang thanks tùy bạn, ở đây mình giữ ở trang hiện tại để hiện lỗi
                }
            }
            
            // Đặt các thuộc tính để hiển thị bên JSP
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        
        // Chuyển hướng
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
    }
    
    // Hỗ trợ cả phương thức GET (gọi lại doPost)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}