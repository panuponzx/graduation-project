package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/sign")
    public ResponseEntity<byte[]> signPdf(@RequestParam("file") MultipartFile file) {
        try {
            // เซ็นต์ไฟล์
            String signedPdfPath = pdfService.signPdf(file);

            // อ่านไฟล์ที่เซ็นต์แล้ว
            byte[] pdfBytes = pdfService.getFileBytes(signedPdfPath);

            // ส่งกลับไปยัง client
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + signedPdfPath)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
