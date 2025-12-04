# GIAI ĐOẠN 1: BUILD (Biên dịch dự án thành tệp .war)
# Sử dụng Maven và JDK 17
FROM maven:3.8.1-openjdk-17-slim AS builder

# Thiết lập thư mục làm việc bên trong container Docker
WORKDIR /app

# Copy toàn bộ mã nguồn vào thư mục làm việc
COPY . .

# Chạy Maven để biên dịch và đóng gói thành tệp .war
# Dòng này sẽ tạo ra /app/target/Tuan1_Buoi2-1.0-SNAPSHOT.war
RUN mvn clean package -DskipTests


# GIAI ĐOẠN 2: RUNTIME (Chạy ứng dụng bằng Tomcat)
# Sử dụng Tomcat 9 với JDK 17
FROM tomcat:10.1-jdk17-temurin

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# COPY tệp .war đã tạo ở Giai đoạn 1 vào thư mục webapps của Tomcat
# Đổi tên thành ROOT.war để ứng dụng chạy ở đường dẫn gốc (/)
COPY --from=builder /app/target/ch14_ex1_email-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng mặc định của Tomcat
EXPOSE 8080

# Lệnh khởi động Tomcat
CMD ["catalina.sh", "run"]