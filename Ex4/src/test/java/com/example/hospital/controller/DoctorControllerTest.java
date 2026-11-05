package com.example.hospital.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/doctors - Trả về JSON bọc bởi ApiResponse có status, code, data, meta")
    void testGetDoctorsUnifiedResponse() throws Exception {
        mockMvc.perform(get("/api/v1/doctors")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("BS. Nguyễn Văn A"))
                .andExpect(jsonPath("$.data[0].specialty").value("Tim mạch"))
                .andExpect(jsonPath("$.meta.currentPage").value(0))
                .andExpect(jsonPath("$.meta.pageSize").value(2))
                .andExpect(jsonPath("$.meta.totalElements").value(3))
                .andExpect(jsonPath("$.meta.totalPages").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/doctors/1 - Trả về một đối tượng đơn lẻ bọc trong ApiResponse")
    void testGetDoctorByIdUnifiedResponse() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("BS. Nguyễn Văn A"));
    }

    @Test
    @DisplayName("GET /api/v1/doctors/999 - Trả về lỗi bọc trong ApiResponse chuẩn (status: error, code: 404)")
    void testGetDoctorByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Không tìm thấy bác sĩ với ID = 999"));
    }
}
