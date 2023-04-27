package dhbw.fileconverter.cipher;

import dhbw.fileconverter.ModuleName;
import dhbw.fileconverter.SupportParameters;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.util.Base64;

@ModuleName("cipherencrypt")
@SupportParameters
public class CipherEncryptFormatter extends CipherFormatter {
    @Override
    protected String transform(String value, Cipher cipher, SecretKey key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new CipherException(e);
        }
    }
}
