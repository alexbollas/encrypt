package com.example.alex.encrypt;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.*;

// REFURL: http://www.java2s.com/Code/Java/Security/MessagewithouttamperingwithMACDESencryptionAESinCTRmode.htm

public class EncryptSMS {
    private static String _ENCODING = "UTF-8";
    private static String _KFALGORITHM = "PBKDF2WithHmacSHA1";
    private static int _KEYSIZE = 256;
    private static int _ITERATIONS = 512;

    private final SecureRandom _RANDOM = new SecureRandom();

    private Cipher _smsC;
    private String _algorithm;

    /**
     * @brief EncryptSMS Constructor
     * @refurl http://developer.android.com/reference/javax/crypto/Cipher.html
     *
     * @param p_algorithm   Algorithm as string, we want 'AES'
     * @param p_mode        Mode as string, we want 'CBC'
     * @param p_padding     Padding as string, we want 'PKCS5Padding'
     * @throws Exception
     */
    public EncryptSMS(String p_algorithm, String p_mode, String p_padding) throws Exception {
        this._algorithm = p_algorithm;
        this._smsC = Cipher.getInstance(p_algorithm + "/" + p_mode + "/" + p_padding);
    }

    /**
     * @brief Encrypt plain text returning it as a CipheredMessage ready to transmit
     *
     * @param p_key         Encryption key as string
     * @param p_plaintext   Plaintext as string
     * @return
     * @throws Exception
     */
    // no equivalent of const functions, awesome!
    public CipheredMessage EncryptMessage(String p_key, String p_plaintext) throws Exception {
        byte[] salt = new byte[_KEYSIZE / 8];
        this._RANDOM.nextBytes(salt);

        KeySpec pbeks = new PBEKeySpec(p_key.toCharArray(), salt, this._ITERATIONS, this._KEYSIZE);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(this._KFALGORITHM);
        byte[] kb = skf.generateSecret(pbeks).getEncoded();
        SecretKey sk = new SecretKeySpec(kb, this._algorithm);

        byte[] iv = new byte[this._smsC.getBlockSize()];
        this._RANDOM.nextBytes(iv);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        this._smsC.init(Cipher.ENCRYPT_MODE, sk, ivps);
        return (new CipheredMessage(this._smsC.doFinal(p_plaintext.getBytes(this._ENCODING)), iv, salt, "]"));
    }

    /**
     * @brief Decrypt a CipheredMessage and return plaintext as string
     *
     * @param p_key     Encryption key as string
     * @param p_cm      CipheredMessage
     * @return
     * @throws Exception
     */
          public String DecryptMessage(String p_key, CipheredMessage p_cm) throws Exception {
                try {
                    KeySpec ks = new PBEKeySpec(p_key.toCharArray(), p_cm.get_salt(), this._ITERATIONS, this._KEYSIZE);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance(this._KFALGORITHM);

            byte[] key = skf.generateSecret(ks).getEncoded();
            SecretKey sk = new SecretKeySpec(key, this._algorithm);

            IvParameterSpec ivps = new IvParameterSpec(p_cm.get_iv());
            this._smsC.init(Cipher.DECRYPT_MODE, sk, ivps);

            return (new String(this._smsC.doFinal(p_cm.get_ciphertext()), this._ENCODING));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
