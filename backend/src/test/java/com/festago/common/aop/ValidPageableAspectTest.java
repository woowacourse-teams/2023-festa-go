package com.festago.common.aop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.common.exception.ErrorCode;
import com.festago.support.CustomWebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CustomWebMvcTest
class ValidPageableAspectTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void size가_없으면_200_응답이_반환된다() throws Exception {
        mockMvc.perform(get("/test")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void size가_maxSize를_초과하면_400_응답이_반환된다() throws Exception {
        mockMvc.perform(get("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("size", "11"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PAGING_MAX_SIZE.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void size가_0_또는_음수이면_400_응답이_반환된다(String size) throws Exception {
        mockMvc.perform(get("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("size", size))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_NUMBER_FORMAT_PAGING_SIZE.getMessage()));
    }

    @Test
    void size가_정수로_파싱할_수_없으면_400_응답이_반환된다() throws Exception {
        mockMvc.perform(get("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("size", "size"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_NUMBER_FORMAT_PAGING_SIZE.getMessage()));
    }

    @Test
    void sizeKey를_지정할_수_있다() throws Exception {
        mockMvc.perform(get("/test-custom-size-key")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("limit", "11"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PAGING_MAX_SIZE.getMessage()));
    }
}

@RestController
class TestController {

    @GetMapping("/test")
    @ValidPageable(maxSize = 10)
    public ResponseEntity<Void> testHandler() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test-custom-size-key")
    @ValidPageable(maxSize = 10, sizeKey = "limit")
    public ResponseEntity<Void> testHandler_with_customSizeKey() {
        return ResponseEntity.ok().build();
    }
}
