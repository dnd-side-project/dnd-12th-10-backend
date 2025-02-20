package com.dnd.reevserver.domain.member.entity.role;

public enum Role {
    NON_MEMBER("NON_MEMBER"),
    MEMBER("MEMBER"),
    LEADER("LEADER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
