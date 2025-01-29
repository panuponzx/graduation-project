package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.service.PdfService;
import com.itextpdf.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/sign")
    public ResponseEntity<String> signPdf(@RequestParam("file") MultipartFile file) {
        try {
            String signedPdfPath = pdfService.signPdf(file);
            return ResponseEntity.ok("Signed PDF saved at: " + signedPdfPath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error signing PDF: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }
}
