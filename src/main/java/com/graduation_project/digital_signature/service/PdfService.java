package com.graduation_project.digital_signature.service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.cert.Certificate;
import java.security.*;

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

            // ใช้ StampingProperties เพื่อกำหนดคุณสมบัติการเซ็น
            StampingProperties properties = new StampingProperties().useAppendMode(); // กำหนดเป็น append mode

            // สร้าง PdfSigner โดยใช้ PdfReader, OutputStream, และ StampingProperties
            PdfSigner signer = new PdfSigner(reader, pdfOutput, properties);

            // ใช้คีย์และ Certificate แบบไม่ต้องยืนยันตอนนี้
            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream("src/main/resources/new_keystore.p12")) {
                ks.load(fis, "123456".toCharArray());
            }
            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, "123456".toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);

            IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "BC");
            IExternalDigest digest = new BouncyCastleDigest();

            signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);

            pdfDocument.close();
        }

        return signedPdfPath;
    }

    public byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        try (InputStream is = new FileInputStream(file)) {
            is.read(bytes);
        }
        return bytes;
    }
}
