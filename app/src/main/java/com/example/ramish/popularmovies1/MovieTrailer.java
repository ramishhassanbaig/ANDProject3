package com.example.ramish.popularmovies1;

public class MovieTrailer {

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getIso_639_1() {
        return this.iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    private String iso_639_1;

    public String getIso_3166_1() {
        return this.iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    private String iso_3166_1;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getSite() {
        return this.site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    private String site;

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private int size;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getTrailerUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    public String getTrailerThumbnailUrl(){
        return "https://img.youtube.com/vi/"+ key +"/default.jpg";
    }



}

