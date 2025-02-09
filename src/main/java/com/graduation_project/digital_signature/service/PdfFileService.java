package com.graduation_project.digital_signature.service;

import com.graduation_project.digital_signature.model.PdfFile;
import com.graduation_project.digital_signature.repository.PdfFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PdfFileService {

    @Autowired
    private PdfFileRepository pdfFileRepository;

    // ดึงไฟล์ PDF ตาม ID
    public Optional<PdfFile> getPdfFileById(Long id) {
        return pdfFileRepository.findById(id);
    }

    // สามารถเพิ่ม method สำหรับการอัปโหลดหรือจัดการ PDF ได้ด้วย
}
