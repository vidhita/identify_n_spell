package com.game.identifyobject;

public class DisplayImage {
    private String id;
    private String spelling;

    public DisplayImage(String id, String spelling) {
        this.id = id;
        this.spelling = spelling;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }
}
