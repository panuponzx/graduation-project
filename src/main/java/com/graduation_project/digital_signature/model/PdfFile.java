package com.graduation_project.digital_signature.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_files")
public class PdfFile {

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

    // Getters
    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getContent() {
        return content;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}