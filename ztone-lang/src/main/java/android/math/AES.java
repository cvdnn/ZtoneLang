/*
 * ASE.java
 *
 * Copyright 2011 sillar team, Inc. All rights reserved.
 *
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.math;

import android.assist.Assert;
import android.log.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密算法
 *
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2012-9-8
 */
public class AES {
    private static final String TAG = "AES";

    // 密钥算法
    private static final String KEY_ALGORITHM = "AES";

    // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * AES加密
     *
     * @param seed
     * @param cleartext
     * @return
     * @throws Exception
     */
    public static String encrypt(String seed, String cleartext) {
        String result = null;

        if (Assert.notEmpty(seed) && Assert.notEmpty(cleartext)) {
            try {
                result = Maths.toHex(encrypt(getRawKey(seed.getBytes()), cleartext.getBytes()));
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    /**
     * AES解密
     *
     * @param seed
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted) {
        String result = null;

        if (Assert.notEmpty(seed) && Assert.notEmpty(encrypted)) {
            try {
                result = new String(decrypt(getRawKey(seed.getBytes()), Maths.asByte(encrypted)));
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);

        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(128, sr); // 192 and 256 bits may not be available

        return kgen.generateKey().getEncoded();
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(raw, KEY_ALGORITHM), new IvParameterSpec(new byte[cipher.getBlockSize()]));

        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, KEY_ALGORITHM), new IvParameterSpec(new byte[cipher.getBlockSize()]));

        return cipher.doFinal(encrypted);
    }

    private AES() {

    }
}
