package com.graduation_project.digital_signature.service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.KeyStore;

@Service
public class PdfService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String signPdf(MultipartFile file) throws Exception {
        String signedPdfPath = "signed_" + file.getOriginalFilename();

        try (InputStream pdfInput = file.getInputStream();
             OutputStream pdfOutput = new FileOutputStream(signedPdfPath)) {

            PdfReader reader = new PdfReader(pdfInput);
            PdfWriter writer = new PdfWriter(pdfOutput);
            PdfDocument pdfDocument = new PdfDocument(reader, writer, new StampingProperties().useAppendMode());

            PdfSigner signer = new PdfSigner(reader, pdfOutput, true);

            // ✅ โหลด PrivateKey และ Certificate จาก KeyStore
            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream("keystore.p12")) {
                ks.load(fis, "password".toCharArray());
            }
            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, "password".toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);

            // ✅ ใช้ PrivateKeySignature และ BouncyCastleDigest
            IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "BC");
            IExternalDigest digest = new BouncyCastleDigest();

            signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);

            pdfDocument.close();
        }

        return signedPdfPath;
    }
}
