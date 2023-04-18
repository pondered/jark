package com.jark.template.common.utils.tools;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

/**
 * @date 2022/3/3011:30
 */
@Slf4j
public final class RsaUtil {
    /**
     * 默认算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * RSA 常量配置
     */
    private static final String ALGORTHM_RSA = "RSA";

    /**
     * * 获取密钥对
     *
     * @return 密钥对
     */
    private static final int INITIALIZE_NUMBER = 2048;

    private RsaUtil() {
    }

    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORTHM_RSA);
        generator.initialize(INITIALIZE_NUMBER);
        return generator.generateKeyPair();
    }


    /**
     * 私钥加密。
     *
     * @param test 字符串
     * @param privateKey 私钥
     * @return 加密后的字符串
     */
    public static String encryptionRsa(final String test, final String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = Base64.decode(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORTHM_RSA);
        //取私钥匙对象
        PrivateKey pk = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(pk);
        signature.update(test.getBytes());

        return Base64.encode(signature.sign());
    }

    /**
     * 公钥解密
     *
     * @param text 字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return true:表示能解密
     */
    public static boolean checkRsa(final String text, final String publicKey, final String sign) {
        try {
            byte[] keyBytes = Base64.decode(publicKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORTHM_RSA);
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey2);
            signature.update(text.getBytes());
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            log.error("checkRSA error, text:{}, publicKey:{}, sign:{}", sign, publicKey, sign, e);
        }
        return false;
    }


    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @param publicKey 公钥
     *
     * @return 密文
     *
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(final String str, final String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORTHM_RSA).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(ALGORTHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @param privateKey 私钥
     *
     * @return 铭文
     *
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(final String str, final String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORTHM_RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ALGORTHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}
