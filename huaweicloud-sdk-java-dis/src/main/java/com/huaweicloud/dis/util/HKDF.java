package com.huaweicloud.dis.util;

import com.huaweicloud.dis.core.util.BinaryUtils;
import com.huaweicloud.dis.core.util.StringUtils;
import com.huaweicloud.dis.exception.DISAuthenticationException;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HKDF {
    public static String getDerKey(String accessKey, String secretKey, String info, String algorithm)
            throws DISAuthenticationException {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            String msg = "The" + (StringUtils.isNullOrEmpty(accessKey) ? "AK" : "SK") + "is empty.";
            throw new DISAuthenticationException(msg);
        }
        String msg = "Failed to expand AK " + secretKey + " with info " + info + ".";
        try{
            byte[] tmpKey = extract(secretKey.getBytes(StandardCharsets.UTF_8),
                    accessKey.getBytes(StandardCharsets.UTF_8), algorithm);
            byte[] derSecretKey = expand(tmpKey, info.getBytes(StandardCharsets.UTF_8), algorithm);
            if(null != derSecretKey) {
                return BinaryUtils.toHex(derSecretKey);
            }
            msg += "derSecretKey is null";
        } catch (Exception e) {
            msg += e.getMessage();
        }

        throw  new DISAuthenticationException(msg);
    }

    public static byte[] extract(byte[] ikm, byte[] salt, String hmacAlgorithm)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(hmacAlgorithm);
        mac.init(new SecretKeySpec(salt, hmacAlgorithm));
        return mac.doFinal(ikm);
    }

    static byte[] expand(byte[] prk, byte[] info, String hmacAlgorithm)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] result = new byte[info.length + 1];
        System.arraycopy(info,0,result,0,info.length);
        result[info.length] = 0x01;
        Mac mac = Mac.getInstance(hmacAlgorithm);
        mac.init(new SecretKeySpec(prk,hmacAlgorithm));
        return mac.doFinal(result);
    }
}
