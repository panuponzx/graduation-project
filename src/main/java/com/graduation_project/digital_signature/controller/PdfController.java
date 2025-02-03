package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.model.SignatureResponse;
import com.graduation_project.digital_signature.service.PdfService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

@Getter
@Setter
@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    @PostMapping("/sign")
    public ResponseEntity<byte[]> signPdf(@RequestParam("file") MultipartFile file) {
        try {
            // ตรวจสอบประเภทของไฟล์ก่อน
            if (!"application/pdf".equals(file.getContentType())) {
                return ResponseEntity.badRequest().body(null);  // ถ้าไม่ใช่ PDF ให้คืนค่าผิดพลาด
            }
            // เซ็นต์ไฟล์
            String signedPdfPath = pdfService.signPdf(file);

            // อ่านไฟล์ที่เซ็นต์แล้ว
            byte[] pdfBytes = pdfService.getFileBytes(signedPdfPath);

            // สร้าง response object โดยตั้งค่าข้อมูลตามที่ต้องการ
            SignatureResponse response = new SignatureResponse();
            // ใช้ชื่อไฟล์จากพาธ (เช่น "signed_document.pdf")
            response.setFile(new File(signedPdfPath).getName());
            response.setStatus("VALID");
            response.setSignedBy("AdminXYZ");  // กำหนดผู้เซ็นต์ตามที่คุณต้องการ
            response.setSignDate("2025-02-01");  // กำหนดวันที่เซ็นต์
            response.setCertificateNumber("12345");  // ตัวอย่าง certificate number
            response.setExpires("2026-12-31");  // ตัวอย่างวันหมดอายุ

            // ส่งกลับไฟล์ PDF ที่เซ็นต์แล้ว
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + signedPdfPath)
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfBytes);
        } catch (IOException e) {
            // Log the exception with a more robust logging framework
            logger.error("Error reading the file", e);
            return ResponseEntity.status(500).body(null); // Handle file reading error
        } catch (Exception e) {
            // Log the exception with a more robust logging framework
            logger.error("An unexpected error occurred", e);
            return ResponseEntity.status(500).body(null); // Handle general errors
        }
    }
}
