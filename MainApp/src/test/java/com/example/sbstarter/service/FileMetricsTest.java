package com.example.sbstarter.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileMetricsTest {
    @Autowired
    private FileService fileService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void filesCountMetricWorks() {
        fileService.saveFile("one", "1".getBytes());
        fileService.saveFile("two", "2".getBytes());

        Gauge gauge = meterRegistry.find("files.count").gauge();

        assertNotNull(gauge);
        assertEquals(2.0, gauge.value());
    }
}