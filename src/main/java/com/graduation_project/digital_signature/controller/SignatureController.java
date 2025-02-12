package com.graduation_project.digital_signature.controller;

import com.graduation_project.digital_signature.model.Signature;
import com.graduation_project.digital_signature.service.SignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdf/signatures")
public class SignatureController {
    private final SignatureService service;

    public SignatureController(SignatureService service) {
        this.service = service;
    }

    // 1️⃣ API: บันทึกลายเซ็น
    @PostMapping
    public ResponseEntity<Signature> saveSignature(@RequestBody Signature signature) {
        return ResponseEntity.ok(service.saveSignature(signature));
    }

    // 2️⃣ API: ดึงลายเซ็นทั้งหมด
    @GetMapping
    public ResponseEntity<List<Signature>> getAllSignatures() {
        return ResponseEntity.ok(service.getAllSignatures());
    }

    // 3️⃣ API: ดึงลายเซ็นตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<Signature> getSignatureById(@PathVariable Long id) {
        Signature signature = service.getSignatureById(id);
        return signature != null ? ResponseEntity.ok(signature) : ResponseEntity.notFound().build();
    }
//
//    Service & Controller รองรับ Multipart File
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Signature> uploadSignature(@RequestParam("file") MultipartFile file) throws IOException {
//        Signature signature = new Signature();
//        signature.setSignature(file.getBytes());
//        return ResponseEntity.ok(service.saveSignature(signature));
//    }
}
