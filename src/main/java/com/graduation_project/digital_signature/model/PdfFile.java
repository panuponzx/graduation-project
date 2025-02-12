package com.graduation_project.digital_signature.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "pdf_files")
public class PdfFile {

    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String filename;

    @Setter
    @Lob
    private byte[] content;

    @Setter
    private LocalDateTime uploadedAt;

    // Constructors
    public PdfFile() {
        this.uploadedAt = LocalDateTime.now();
    }

    public PdfFile(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
        this.uploadedAt = LocalDateTime.now();
    }

}