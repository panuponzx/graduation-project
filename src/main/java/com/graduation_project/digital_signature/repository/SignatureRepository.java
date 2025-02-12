package com.graduation_project.digital_signature.repository;

import com.graduation_project.digital_signature.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepository extends JpaRepository<Signature, Long> {
}