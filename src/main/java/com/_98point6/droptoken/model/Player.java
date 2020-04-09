package com._98point6.droptoken.model;

public class Player {

    private String id;
    private Integer token;

    public Player(String id, Integer token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public Integer getToken() {
        return token;
    }
}
