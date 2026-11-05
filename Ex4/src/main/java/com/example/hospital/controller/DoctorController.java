package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.MetaData;
import com.example.hospital.model.Doctor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final List<Doctor> mockDoctors = new ArrayList<>();

    public DoctorController() {
        mockDoctors.add(Doctor.builder()
                .id(1L)
                .name("BS. Nguyễn Văn A")
                .specialty("Tim mạch")
                .phone("0987654321")
                .build());
        mockDoctors.add(Doctor.builder()
                .id(2L)
                .name("BS. Trần Thị B")
                .specialty("Nhi khoa")
                .phone("0912345678")
                .build());
        mockDoctors.add(Doctor.builder()
                .id(3L)
                .name("BS. Lê Văn C")
                .specialty("Ngoại khoa")
                .phone("0933445566")
                .build());
    }

    /**
     * API: Lấy danh sách bác sĩ kèm thông tin phân trang chuẩn hóa
     * URL: GET /api/v1/doctors
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctor>>> getDoctors(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        int totalElements = mockDoctors.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Tính toán phân trang
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<Doctor> pagedList = mockDoctors.subList(fromIndex, toIndex);

        MetaData meta = MetaData.builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        ApiResponse<List<Doctor>> response = ApiResponse.success(pagedList, meta);
        return ResponseEntity.ok(response);
    }

    /**
     * API: Lấy chi tiết một bác sĩ
     * URL: GET /api/v1/doctors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> getDoctorById(@PathVariable("id") Long id) {
        return mockDoctors.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .map(d -> ResponseEntity.ok(ApiResponse.success(d)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(ApiResponse.error(404, "Không tìm thấy bác sĩ với ID = " + id)));
    }
}
