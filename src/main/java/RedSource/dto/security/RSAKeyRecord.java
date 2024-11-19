package RedSource.dto.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public record RSAKeyRecord(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
