package com.hq.tool.misc;

//import com.aisino.gsir.common.general.DefContants;
//import org.apache.commons.codec.binary.Base64;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 描述：RSA加解密工具
 *
 * @author Liyp
 * @date 2020-10-29
 * @since v1.0
 */
public class RSAUtil {

    // 加密数据和秘钥的编码方式
    public static final String UTF_8 = "UTF-8";

    // 填充方式
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
//    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_NOPADDING = "RSA";

    /**
     * 默认的RSA加密方法 一般用来解密 参数 小数据
     *
     * @param publicKey
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encryptRSADefault(String publicKey, String plainText) throws Exception {
        // base64编码的公钥
        byte[] decoded = Base64.decode(publicKey, Base64.NO_WRAP);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NOPADDING);
        RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
        //
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return new String(Base64.encode(cipher.doFinal(plainText.getBytes(UTF_8)), Base64.NO_WRAP));
    }

    /**
     * Description:默认的RSA解密方法 一般用来解密 参数 小数据
     *
     * @param privateKeyStr
     * @param data
     * @return
     * @throws Exception
     * @author wh.huang  DateTime 2018年12月14日 下午3:43:11
     */
    public static String decryptRSADefault(String privateKeyStr, String data) throws Exception {
        // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NOPADDING);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr, Base64.NO_WRAP));
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        // 解密
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_NOPADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decode(data, Base64.NO_WRAP)), UTF_8);
    }
//
//    public static void main(String[] args) throws Exception {
//        // 公钥加密
//        String userName = "hb.123456";
//        String rsaDefault = encryptRSADefault(DefContants.RSA_PUBLIC_KEY, userName);
//        System.out.println(rsaDefault);
//
//        //私钥解密
//        String decryptData = decryptRSADefault(DefContants.RSA_PRIVATE_KEY, rsaDefault);
//        System.out.println(decryptData);
//    }

}
