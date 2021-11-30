package com.efimchick.ifmo.io.filetree;

public enum Pref {
    LAST("└─ "),
    STICK("│  "),
    BRANCH("├─ ");
    private String value;

    Pref(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}
