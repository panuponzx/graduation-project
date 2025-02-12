package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.model.PdfFile;
import com.graduation_project.digital_signature.service.PdfFileService;
import com.graduation_project.digital_signature.repository.PdfFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/select/pdf")
@CrossOrigin(origins = "*") //  all domain
public class PdfFileController {

    @Autowired
    private PdfFileService pdfFileService;

    @Autowired
    private PdfFileRepository pdfFileRepository;

    // API สำหรับอัปโหลดไฟล์ PDF ลงในฐานข้อมูล
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdfFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // แปลงไฟล์เป็น byte[]
            byte[] content = file.getBytes();
            // สร้าง entity สำหรับเก็บไฟล์ PDF
            PdfFile pdfFile = new PdfFile(file.getOriginalFilename(), content);
            // บันทึกลงฐานข้อมูล
            PdfFile savedFile = pdfFileRepository.save(pdfFile);
            return ResponseEntity.ok("File uploaded successfully with ID: " + savedFile.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }
    // GET API ดึงไฟล์ PDF ตาม id
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdfFile(@PathVariable("id") Long id) {
        return pdfFileService.getPdfFileById(id)
                .map(pdfFile -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + pdfFile.getFilename())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdfFile.getContent()))
                .orElse(ResponseEntity.notFound().build());
    }
}


