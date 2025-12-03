package com.example.sbstarter.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@Component
public class FileMetrics implements MeterBinder {

    private final FileService fileService;

    public FileMetrics(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        registry.gauge("files.count", fileService, fs -> fs.countFiles());
    }
}
