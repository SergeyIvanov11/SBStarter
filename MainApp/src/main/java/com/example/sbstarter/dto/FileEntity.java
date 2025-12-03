package com.example.sbstarter.dto;

import java.time.Instant;

public class FileEntity {
    byte[] content;
    Instant createdAt;

    public FileEntity(byte[] content, Instant createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }


    public byte[] getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
