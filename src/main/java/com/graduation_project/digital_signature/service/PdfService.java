package com.graduation_project.digital_signature.service;

import com.graduation_project.digital_signature.utils.KeyUtils;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.Security;

@Service
public class PdfService {

    // ลงทะเบียน BouncyCastle Provider
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String signPdf(MultipartFile file) throws Exception {
        // อ่านไฟล์ PDF จาก MultipartFile
        InputStream pdfInputStream = file.getInputStream();
        PdfReader reader = new PdfReader(pdfInputStream);

        // สร้างไฟล์ชั่วคราวสำหรับ PDF ที่เซ็นต์แล้ว
        File tempFile = File.createTempFile("signed_", ".pdf");
        OutputStream signedPdfOutputStream = new FileOutputStream(tempFile);

        // สร้าง PdfSigner โดยใช้ PdfReader, OutputStream และใช้ StampingProperties (ไม่ใช้ append mode)
        PdfSigner signer = new PdfSigner(reader, signedPdfOutputStream, new StampingProperties());

        // โหลด Private Key และ Certificate Chain จาก KeyUtils
        PrivateKey privateKey = KeyUtils.getPrivateKey();
        Certificate[] chain = KeyUtils.getCertificateChain();

        // กำหนดการเซ็นต์ด้วย SHA-384 with RSA (และใช้ BouncyCastle เป็น provider)
        IExternalSignature pks = new PrivateKeySignature(privateKey, "SHA-384", "BC");
        IExternalDigest digest = new BouncyCastleDigest();

        // กำหนดลักษณะของลายเซ็นต์ (Signature Appearance)
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance.setReason("Document signed by User")
                .setLocation("Thailand")
                .setPageNumber(1)
                .setCertificate(chain[0])
                .setLayer2Text("เอกสารนี้ได้รับการเซ็นแล้ว");

        // เซ็นต์ไฟล์ PDF โดยใช้ certificate chain ทั้งหมด
        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);

        return tempFile.getAbsolutePath();  // คืนค่าพาธของไฟล์ PDF ที่เซ็นต์แล้ว
    }

    // ฟังก์ชันเพื่อดึง byte array ของไฟล์ที่เซ็นต์แล้ว
    public byte[] getFileBytes(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] fileBytes = fileInputStream.readAllBytes();
        fileInputStream.close();
        return fileBytes;
    }
}
