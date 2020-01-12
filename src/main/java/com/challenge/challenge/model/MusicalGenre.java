package com.challenge.challenge.model;

public enum MusicalGenre {

    PARTY("party"),
    POP("pop"),
    ROCK("rock"),
    CLASSICAL("classical");

    private String value;

    MusicalGenre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
