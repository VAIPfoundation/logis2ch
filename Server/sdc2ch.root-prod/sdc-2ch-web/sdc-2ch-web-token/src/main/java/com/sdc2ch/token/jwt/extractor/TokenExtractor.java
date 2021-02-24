package com.sdc2ch.token.jwt.extractor;


public interface TokenExtractor {
    public String extract(String payload);
}
