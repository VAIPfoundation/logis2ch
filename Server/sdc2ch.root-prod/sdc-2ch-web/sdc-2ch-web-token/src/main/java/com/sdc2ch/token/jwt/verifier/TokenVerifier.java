package com.sdc2ch.token.jwt.verifier;


public interface TokenVerifier {
    public boolean verify(String jti);
}
