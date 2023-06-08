package dhbw.fileconverter.cipher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import dhbw.fileconverter.IFormatter;
import dhbw.fileconverter.ProcessingException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

public abstract class CipherFormatter implements IFormatter {
    protected ObjectMapper mapper = new ObjectMapper();
    protected static final String ENCRYPTION_ALGORITHM = "AES";
    protected static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    protected static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    protected static final int SECRET_KEY_LENGTH = 256;
    protected static final int ITERATION_COUNT = 65536;
    protected static String SECRET_KEY = "dhbw";
    protected static final byte[] SALT = new byte[] { 0x1a, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef };

    @Override
    public JsonNode to(JsonNode input, String[] parameters) throws ProcessingException {
        if (parameters.length == 0) {
            throw new ProcessingException("No secret key provided");
        }

        SECRET_KEY = parameters[0];

        try {
            SecretKeySpec secretKeySpec = generateSecretKey();
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

            if (input.isObject()) {
                return transformObject((ObjectNode) input, cipher, secretKeySpec);
            } else if (input.isArray()) {
                return transformArray((ArrayNode) input, cipher, secretKeySpec);
            } else if (input.isTextual()) {
                return new TextNode(transform(input.asText(), cipher, secretKeySpec));
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | CipherException exception) {
            throw new ProcessingException(exception);
        }

        return input;
    }

    @Override
    public JsonNode from(JsonNode input, String[] parameters) {
        return input;
    }

    protected abstract String transform(String input, Cipher cipher, SecretKey key);

    protected ObjectNode transformObject(ObjectNode object, Cipher cipher, SecretKey key) {
        object.fields().forEachRemaining(field -> {
            JsonNode value = field.getValue();
            if (value.isTextual()) {
                object.set(field.getKey(), new TextNode(transform(value.asText(), cipher, key)));
            } else if (value.isObject()) {
                object.set(field.getKey(), transformObject((ObjectNode) value, cipher, key));
            } else if (value.isArray()) {
                object.set(field.getKey(), transformArray((ArrayNode) value, cipher, key));
            }
        });
        return object;
    }

    protected ArrayNode transformArray(ArrayNode array, Cipher cipher, SecretKey key) {
        return StreamSupport.stream(array.spliterator(), false).map(node -> {
            if (node.isTextual()) {
                return new TextNode(transform(node.asText(), cipher, key));
            } else if (node.isObject()) {
                return transformObject((ObjectNode) node, cipher, key);
            } else {
                return node;
            }
        }).collect(Collector.of(() -> mapper.createArrayNode(), ArrayNode::add, ArrayNode::addAll));
    }

    protected SecretKeySpec generateSecretKey() {
        if (SECRET_KEY.length() % 16 != 0) { // AES needs a 16 character long key
            SECRET_KEY += "x".repeat(16 - SECRET_KEY.length() % 16); // if user didn't enter a 16 character long key, make it 16 characters long
        }
        return new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM); // generate secret key from user input
    }
}
