package com.example.sbstarter.service;

import com.example.sbstarter.dto.FileEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final Map<String, FileEntity> storage = new ConcurrentHashMap<>();

    public void saveFile(String name, byte[] content) {
        storage.put(name, new FileEntity(content, Instant.now()));
    }

    @Cacheable("files")
    public byte[] getFile(String name) {
        FileEntity entity = storage.get(name);
        return entity != null ? entity.getContent() : null;
    }

    @Scheduled(fixedRate = 3_600_000)
    public void cleanup() {
        storage.entrySet()
                .removeIf(e -> e.getValue().getCreatedAt()
                        .isBefore(Instant.now().minusSeconds(3600)));
    }

    public int countFiles() {
        return storage.size();
    }

    public List<String> getAllFilenames() {
        return storage.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
