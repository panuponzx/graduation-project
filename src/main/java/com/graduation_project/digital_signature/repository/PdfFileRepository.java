package com.graduation_project.digital_signature.repository;

import com.graduation_project.digital_signature.model.PdfFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfFileRepository extends JpaRepository<PdfFile, Long> {
    // สามารถเพิ่ม query method ที่ต้องการได้ที่นี่
}
