package com.graduation_project.digital_signature.service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.Security;

@Service
public class PdfService {

    // Keystore details
    private static final String KEYSTORE_PATH = "new_keystore.p12"; // Keystore file path (located in resources)
    private static final String KEYSTORE_PASSWORD = "123456"; // Keystore password
    private static final String KEY_ALIAS = "myalias"; // Alias of the Private Key
    private static final String KEY_PASSWORD = "123456"; // Password of the Private Key

    static {
        // Register Bouncy Castle provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public String signPdf(MultipartFile file) throws Exception {
        // Read the PDF file from MultipartFile
        InputStream pdfInputStream = file.getInputStream();
        PdfReader reader = new PdfReader(pdfInputStream);

        // Create a temporary file for the signed PDF
        File tempFile = File.createTempFile("signed_", ".pdf");
        OutputStream signedPdfOutputStream = new FileOutputStream(tempFile);

        // Create PdfSigner in append mode (false = create a new document)
        PdfSigner signer = new PdfSigner(reader, signedPdfOutputStream, new StampingProperties().useAppendMode());
        // Load Keystore from resources
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(KEYSTORE_PATH);
        if (keystoreStream == null) {
            throw new FileNotFoundException("Keystore file not found in resources: " + KEYSTORE_PATH);
        }
        keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray());

        // Retrieve Private Key and Certificate Chain from keystore
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        Certificate[] chain = keyStore.getCertificateChain(KEY_ALIAS);
        if (chain == null || chain.length == 0) {
            throw new Exception("No certificate found with alias: " + KEY_ALIAS);
        }

        // Configure the signature with SHA-384 and RSA
        IExternalSignature pks = new PrivateKeySignature(privateKey, "SHA-384", "BC");
        IExternalDigest digest = new BouncyCastleDigest();

        // Set signature appearance
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance.setReason("Document signed by User")
                .setLocation("Thailand")
                .setPageNumber(1)
                .setCertificate(chain[0]);

        // Sign the PDF (use the entire certificate chain)
        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);

        // Close the PdfSigner and OutputStream (PdfSigner will close PdfDocument automatically)
        return tempFile.getAbsolutePath();  // Return the path of the signed PDF
    }

    // Function to retrieve byte array of the signed file
    public byte[] getFileBytes(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] fileBytes = fileInputStream.readAllBytes();
        fileInputStream.close();
        return fileBytes;
    }
}
