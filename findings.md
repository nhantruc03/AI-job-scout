# Project Findings & Knowledge Base

## DevOps & Docker
- **S3 Access Style**: Cần cấu hình `.forcePathStyle(true)` khi dùng SDK v2 với LocalStack để tránh lỗi `UnknownHostException` trên Windows (do DNS không phân giải được name bucket dưới dạng subdomain).

## Kafka
- *To be updated with Topics, Partitions, Consumer Groups.*

## Project Architecture Guidelines
- **Package Structure**:
  - `com.ailab.ingestion.controller`: REST APIs.
  - `com.ailab.ingestion.service`: Business Logic.
  - `com.ailab.ingestion.model`: Entities & DTOs.
  - `com.ailab.ingestion.config`: Configuration & Beans.
- **Rule**: Luôn giữ Logic xử lý bên trong `service`, Controller chỉ làm nhiệm vụ điều hướng.
