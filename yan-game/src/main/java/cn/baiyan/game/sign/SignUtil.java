package cn.baiyan.game.sign;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SignUtil {
    /*
     * appid/client_key对应的client_secret TODO 这里换成服务商的appsecret
     */
    private static final String secret = "1e80aa2bb9b7caea9b94c2f09f6c4b57d806db10";
    private static final String key;

    private static final String iv;

    static {
        key = parseSecret(secret);
        iv =key.substring(16);
    }

    /**
     * @Description AES解密
     * @param data base64后的密文
     * @return 明文
     */
    public static String decryptAES(String data) throws Exception {
        try
        {
            byte[] encrypted1 = decode(data);//先用base64解密

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * base64编码
     */
    public static String encode(byte[] byteArray) {
        return new String(Base64.getEncoder().encode(byteArray));
    }

    /**
     * base64解码
     */
    public static byte[] decode(String base64EncodedString) {
        return Base64.getDecoder().decode(base64EncodedString);
    }

    private static String parseSecret(String secret) {
        secret = fillSecret(secret);
        secret = cutSecret(secret);
        return secret;
    }

    private static String cutSecret(String secret) {
        if (secret.length() <= 32) {
            return secret;
        }

        int rightCnt = (secret.length() - 32) / 2;
        int leftCnt = secret.length() - 32 - rightCnt;

        return secret.substring(leftCnt, 32 + leftCnt);
    }

    private static String fillSecret(String secret) {
        if (secret.length() >= 32) {
            return secret;
        }
        int rightCnt = (32 - secret.length()) / 2;
        int leftCnt = 32 - secret.length() - rightCnt;

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < leftCnt; i++) {
            sb.append('#');
        }
        sb.append(secret);
        for (int i = 0; i < rightCnt; i++) {
            sb.append('#');
        }
        return sb.toString();
    }
}