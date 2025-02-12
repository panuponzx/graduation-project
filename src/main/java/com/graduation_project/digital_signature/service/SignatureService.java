package com.graduation_project.digital_signature.service;

import com.graduation_project.digital_signature.model.Signature;
import com.graduation_project.digital_signature.repository.SignatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignatureService {
    private final SignatureRepository repository;

    public SignatureService(SignatureRepository repository) {
        this.repository = repository;
    }

    public Signature saveSignature(Signature signature) {
        return repository.save(signature);
    }

    public List<Signature> getAllSignatures() {
        return repository.findAll();
    }

    public Signature getSignatureById(Long id) {
        return repository.findById(id).orElse(null);
    }
}