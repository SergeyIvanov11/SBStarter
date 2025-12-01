package com.example.sbstarter.controller;

import com.example.sbstarter.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        service.saveFile(file.getOriginalFilename(), file.getBytes());
        return "ok";
    }

    @GetMapping("/{name}")
    public byte[] download(@PathVariable String name) {
        return service.getFile(name);
    }

    @GetMapping("/count")
    public int count() {
        return service.countFiles();
    }
}
