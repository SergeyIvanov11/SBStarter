package com.example.sbstarter.service;

import com.example.sbstarter.dto.FileEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    void saveAndGetFile() {
        byte[] data = "hello".getBytes();
        fileService.saveFile("test.txt", data);

        byte[] loaded = fileService.getFile("test.txt");

        assertArrayEquals(data, loaded);
    }

    @Test
    void getFileReturnsNullIfNotExists() {
        byte[] result = fileService.getFile("no-file");
        assertNull(result);
    }

    @Test
    void countFilesWorks() {
        fileService.saveFile("a", "1".getBytes());
        fileService.saveFile("b", "2".getBytes());

        assertEquals(2, fileService.countFiles());
    }

    @Test
    void cleanupRemovesOldFiles() throws Exception {
        fileService = new FileService();
        fileService.saveFile("old.txt", "123".getBytes());
        fileService.saveFile("new.txt", "456".getBytes());

        Field f = FileService.class.getDeclaredField("storage");
        f.setAccessible(true);
        Map<String, FileEntity> map = (Map<String, FileEntity>) f.get(fileService);

        map.put("old.txt", new FileEntity("123".getBytes(), Instant.now().minus(Duration.ofHours(2))));
        map.put("new.txt", new FileEntity("456".getBytes(), Instant.now()));

        fileService.cleanup();

        assertNull(fileService.getFile("old.txt"));
        assertNotNull(fileService.getFile("new.txt"));
        assertEquals(1, fileService.countFiles());
    }
}