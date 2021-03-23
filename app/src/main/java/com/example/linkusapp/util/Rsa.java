package com.example.linkusapp.util;


import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.cert.CertificateException;


@RequiresApi(api = Build.VERSION_CODES.M)
public class Rsa {
    KeyPairGenerator keyPairGenerator;
    private static final String TAG = "RSACryptor";
    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    KeyPair kp;

    public void createKey() {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            "key1",
                            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setAlgorithmParameterSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build());
            kp = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

    }

    public byte[] encrypt(final String text) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, CertificateException, IOException {
        Log.d(TAG, "encrypt test: " + text + "");
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        PublicKey publicKey = keyStore.getCertificate("key1").getPublicKey();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);


        Log.d(TAG, "publicKey1" + publicKey);
        String pubkey3 = publicKey.getEncoded().toString();
        Log.d(TAG, "publicKey3" + pubkey3);
        Log.d(TAG, "publicKey2" + android.util.Base64.encodeToString(publicKey.getEncoded(), android.util.Base64.DEFAULT));
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(), 0);
        String pubKey = new String(publicKeyBytes);
        Log.d(TAG, "publicKey4444" + pubkey3);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes("utf-8"));
        Log.d(TAG, "encry" + encryptedBytes.toString());
        return encryptedBytes;
    }

    public String decrypt(final byte[] encryptedText) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("key1", null);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedText = cipher.doFinal(encryptedText);
            Log.d(TAG, "end decrypt" + decryptedText.toString());
            return new String(decryptedText);
        } catch (UnrecoverableKeyException | IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            Log.e("decrypt catch", e.getMessage() + "");
            String text = new String(encryptedText);
            return text;
        }

    }
}
