package com.sdc2ch.require.enums;


public enum Role {
    ADMIN, PREMIUM_MEMBER, MEMBER, USER, PREMIUM;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
