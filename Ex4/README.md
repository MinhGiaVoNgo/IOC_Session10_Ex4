# [BÀI TẬP 4 - GIỎI] ĐỊNH DẠNG DỮ LIỆU TRẢ VỀ (RESPONSE FORMATTING)

## 1. Mục tiêu kiến thức & Kỹ năng
- **Kiến thức:** Hiểu và áp dụng quy tắc định dạng dữ liệu trả về thống nhất (*Unified Response Formatting*) trong RESTful API (Lesson 06).
- **Kỹ năng:** Xây dựng lớp Wrapper tổng quát `ApiResponse<T>` và `MetaData` trong Java Spring Boot để tự động chuẩn hóa mọi phản hồi từ Server.

---

## 2. Thiết kế mẫu JSON trả về cho API "Lấy danh sách bác sĩ"

Theo yêu cầu nghiệp vụ, thay vì trả về một mảng trần `[...]`, dữ liệu được bao bọc trong một cấu trúc JSON đồng nhất:

```json
{
  "status": "success",
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "BS. Nguyễn Văn A",
      "specialty": "Tim mạch",
      "phone": "0987654321"
    },
    {
      "id": 2,
      "name": "BS. Trần Thị B",
      "specialty": "Nhi khoa",
      "phone": "0912345678"
    }
  ],
  "meta": {
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

### Ý nghĩa của các trường:
- `status` (`String`): Trạng thái nghiệp vụ cấp cao (`"success"` hoặc `"error"`). Giúp Frontend kiểm tra nhánh logic `if (res.status === 'success')` cực kỳ nhanh chóng.
- `code` (`int`): Mã trạng thái HTTP phản ánh trong payload JSON (đồng bộ với HTTP status header, ví dụ: 200, 201, 400, 404, 500).
- `data` (`Generic T`): Chứa nội dung chính của tài nguyên (mảng danh sách bác sĩ, đối tượng đơn lẻ, hoặc `null` khi có lỗi).
- `meta` (`MetaData`): Chứa các thông tin siêu dữ liệu (phân trang: `currentPage`, `pageSize`, `totalElements`, `totalPages`), hỗ trợ Frontend dựng thanh phân trang (Pagination UI) dễ dàng.

---

## 3. Thiết kế các Class Java thực hành

### 3.1. Lớp siêu dữ liệu `MetaData.java`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaData {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
```

### 3.2. Lớp bao bọc tổng quát `ApiResponse<T>.java`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "code", "message", "data", "meta"})
public class ApiResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;
    private MetaData meta;

    public static <T> ApiResponse<T> success(T data, MetaData meta) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .data(data)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(code)
                .message(message)
                .build();
    }
}
```

---

## 4. Lợi ích của việc chuẩn hóa Response Format
1. **Frontend dễ dự đoán và xử lý (Predictable API Contract):**
   - Đội ngũ Frontend chỉ cần viết một Interceptor / Middleware duy nhất để giải nén `data`, kiểm tra `status` và bắt lỗi đồng bộ.
2. **Tách bạch rõ ràng dữ liệu chính và siêu dữ liệu:**
   - Dữ liệu nghiệp vụ nằm trong `data`, thông tin phân trang/lọc nằm trong `meta`, tránh làm ô nhiễm model của thực thể chính.
3. **Mở rộng dễ dàng (Extensibility):**
   - Khi cần bổ sung thông tin như thời gian phản hồi (`executionTime`), liên kết HATEOAS (`links`), có thể mở rộng `ApiResponse` mà không làm vỡ cấu trúc của `data`.

---

## 5. Chạy và kiểm thử mã nguồn đính kèm
```bash
cd M:\JAVA_BACKEND\BTVN_moi\SS10\Ex4
mvn test
```
