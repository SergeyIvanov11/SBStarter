package com.example.sbstarter.controller;

import com.example.sbstarter.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void uploadReturnsOk() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "hello.txt", "text/plain", "aaa".getBytes());

        mockMvc.perform(multipart("/files").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(fileService).saveFile("hello.txt", "aaa".getBytes());
    }

    @Test
    void downloadReturnsBytes() throws Exception {
        when(fileService.getFile("aaa.txt")).thenReturn("hello".getBytes());

        mockMvc.perform(get("/files/aaa.txt"))
                .andExpect(status().isOk())
                .andExpect(content().bytes("hello".getBytes()));
    }

    @Test
    void countReturnsNumber() throws Exception {
        when(fileService.countFiles()).thenReturn(5);

        mockMvc.perform(get("/files/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}