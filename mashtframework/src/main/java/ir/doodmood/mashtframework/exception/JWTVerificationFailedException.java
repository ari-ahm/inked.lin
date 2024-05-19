package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JWTVerificationFailedException extends Exception {
    public JWTVerificationFailedException(String message) {
        super(message);
    }
}