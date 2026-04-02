# AI Job Scout 🚀

Hệ thống phân tích mô tả công việc (Job Description - JD) thông minh sử dụng Microservices, Kafka và AI (LangChain4j).

## 🏗️ Kiến trúc hệ thống
Dự án được xây dựng theo mô hình Event-Driven Microservices:

1. **Gateway Service**: Cổng vào quản lý routing cho toàn hệ thống.
2. **Job Ingestion Service**: Nhận JD từ người dùng, lưu vào **AWS S3** và gửi sự kiện vào **Kafka**.
3. **Job Analyzer Service**: 
   - Lắng nghe Kafka để xử lý bất động bộ.
   - Hỗ trợ đọc nội dung văn bản thuần và cả file **PDF** (Sử dụng Apache PDFBox).
   - Sử dụng **LangChain4j** kết nối với **Ollama** (Local AI) hoặc **OpenAI** để phân tích JD.
   - Lưu kết quả phân tích (điểm số, gợi ý) vào **AWS DynamoDB**.
4. **Notification Lambda**: Xử lý việc thông báo khi có kết quả phân tích (Đang chạy trên LocalStack).

## 🛠️ Công nghệ sử dụng
- **Backend**: Spring Boot 3.2, Java 17.
- **AI**: LangChain4j, Ollama, OpenAI.
- **Infrastructure**: AWS (S3, DynamoDB, Lambda) giả lập qua **LocalStack**.
- **Messaging**: Apache Kafka.
- **Database**: S3 (Blob storage), DynamoDB (NoSQL).

## 🚀 Cách khởi chạy nhanh (Local)
1. **Khởi động hạ tầng**: 
   - Chạy Docker Compose để có LocalStack và Kafka: `docker-compose up -d`
2. **Cài đặt Lambda**: 
   - Truy cập `notification-lambda/` và chạy `deploy.ps1`.
3. **Chạy các Service**: 
   - Chạy `Gateway Service`, `Job Ingestion`, `Job Analyzer` qua IDE hoặc lệnh `mvn spring-boot:run`.

## 📂 Cấu trúc thư mục
- `/gateway-service`: Quản lý API Gateway.
- `/job-ingestion-service`: Tiếp nhận JD.
- `/job-analyzer-service`: Xử lý AI & PDF.
- `/notification-lambda`: Node.js Lambda cho thông báo.

