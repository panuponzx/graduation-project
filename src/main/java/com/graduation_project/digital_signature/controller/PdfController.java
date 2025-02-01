package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
            // ตรวจสอบประเภทของไฟล์ก่อน
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body(null);  // ถ้าไม่ใช่ PDF ให้คืนค่าผิดพลาด
            }

            // เซ็นต์ไฟล์
            String signedPdfPath = pdfService.signPdf(file);

            // อ่านไฟล์ที่เซ็นต์แล้ว
            byte[] pdfBytes = pdfService.getFileBytes(signedPdfPath);

            // ส่งกลับไฟล์ PDF ที่เซ็นต์แล้ว
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + signedPdfPath)
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfBytes);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // จัดการข้อผิดพลาดในการอ่านไฟล์
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // จัดการข้อผิดพลาดทั่วไป
        }
    }
}
