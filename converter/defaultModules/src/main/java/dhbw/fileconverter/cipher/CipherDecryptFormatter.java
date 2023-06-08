package dhbw.fileconverter.cipher;

import dhbw.fileconverter.ModuleName;
import dhbw.fileconverter.SupportParameters;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.util.Base64;

@ModuleName("cipherdecrypt")
@SupportParameters
public class CipherDecryptFormatter extends CipherFormatter {
    protected String transform(String input, Cipher cipher, SecretKey key) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedInput = Base64.getDecoder().decode(input);
            return new String(cipher.doFinal(decodedInput));
        } catch (InvalidKeyException invalidKeyException) {
            throw new CipherException(invalidKeyException);
        } catch (BadPaddingException | IllegalBlockSizeException exception) {
            return input;
        }
    }
}
