package com.graduation_project.digital_signature.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "signatures")
@Data
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // ถ้าผู้ใช้ไม่ได้ login ให้เป็น null

    @Lob
    @Column(nullable = false)
    private String signature; // เก็บ Base64 หรือ BLOB

    private LocalDateTime createdAt = LocalDateTime.now();
}

//
//ทำให้ API รองรับไฟล์ BLOB (ถ้าต้องการ)
//@Lob
//@Column(columnDefinition = "BYTEA")
//private byte[] signature;
