package com.desafio.logistica.service;

import com.desafio.logistica.exeption.ErrorFileException;
import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProcessFileServiceTest {
    @InjectMocks
    private ProcessFileService service;

    @Test
    void shouldProcessFileSuccessfully() throws Exception {
        String line1 =
                String.format("%-10s", "1") +
                        String.format("%-45s", "Alice") +
                        String.format("%-10s", "100") +
                        String.format("%-10s", "200") +
                        String.format("%-12s", "150.50") +
                        "20230101";
        String line2 =
                String.format("%-10s", "1") +
                        String.format("%-45s", "Alice") +
                        String.format("%-10s", "100") +
                        String.format("%-10s", "201") +
                        String.format("%-12s", "49.50") +
                        "20230101";

        String content = line1 + System.lineSeparator() + line2;
        MockMultipartFile goodFile = new MockMultipartFile(
                "file", "test.txt", "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        Set<UserEntity> users = service.processFiles(goodFile);
        assertEquals(1, users.size());

        UserEntity user = users.iterator().next();
        assertEquals(1, user.getUserId());
        assertEquals("Alice", user.getName());

        assertEquals(1, user.getOrders().size());
        OrderEntity order = user.getOrders().iterator().next();
        assertEquals(100, order.getOrderId());
        assertEquals(2, order.getProducts().size());

        // 150.50 + 49.50 = 200.00
        assertEquals(new BigDecimal("200.00"), order.getTotal());
    }
    @Test
    void shouldThrowErrorFileExceptionWhenFileIsInvalid() {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        try {
            Mockito.when(mockFile.getInputStream()).thenThrow(new RuntimeException("Error"));

            assertThrows(ErrorFileException.class, () -> {
                service.processFiles(mockFile);
            });
        } catch (Exception e) {
            fail("Error");
        }
    }
}