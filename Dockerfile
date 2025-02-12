# ใช้ JDK 17 หรือเวอร์ชันที่รองรับ Spring Boot
FROM openjdk:17-jdk-slim

# กำหนด working directory ใน container
WORKDIR /app

# คัดลอกไฟล์ JAR จาก target ไปยัง container
COPY target/*.jar app.jar

# กำหนด port ที่ Spring Boot ใช้
EXPOSE 8080

# คำสั่งรันแอปพลิเคชัน
CMD ["java", "-jar", "app.jar"]
