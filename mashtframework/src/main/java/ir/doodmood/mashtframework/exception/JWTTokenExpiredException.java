package ir.doodmood.mashtframework.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JWTTokenExpiredException extends Exception {
    public JWTTokenExpiredException(String message) {
        super(message);
    }
}