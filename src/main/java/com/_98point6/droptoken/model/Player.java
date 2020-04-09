package com._98point6.droptoken.model;

public class Player {

    private String id;
    private Integer token;
    
    private boolean playing;

    public Player(String id, Integer token, boolean playing) {
        this.id = id;
        this.token = token;
        this.playing = playing;
    }

    public String getId() {
        return id;
    }

    public Integer getToken() {
        return token;
    }

    public boolean isPlaying() {
        return playing;
    }
    
    public void quit() {
        playing = false;
    }
}
