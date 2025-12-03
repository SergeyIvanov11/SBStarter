package com.example.sbstarter.controller;

import com.example.sbstarter.service.FileService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        service.saveFile(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok("Ok, file is uploaded");
    }

    @GetMapping("/count")
    public int count() {
        return service.countFiles();
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> showAllFilenames() {
        List<String> filenames = service.getAllFilenames();
        return ResponseEntity.ok(filenames);
    }


    @GetMapping("/{name}")
    public ResponseEntity<byte[]> download(@PathVariable String name) {
        byte[] content = service.getFile(name);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }
        // определение Content-Type
        String contentType = determineContentType(name);

        // заголовки для скачивания
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(name)
                        .build()
        );
        headers.setContentLength(content.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        switch (extension) {
            case "txt": return "text/plain";
            case "pdf": return "application/pdf";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default: return "application/octet-stream";
        }
    }
}
