package com.example.ramish.popularmovies1;

import java.util.Date;

public class MovieReview {
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    String author;

    public AuthorDetails getAuthor_details() {
        return this.author_details;
    }

    public void setAuthor_details(AuthorDetails author_details) {
        this.author_details = author_details;
    }

    AuthorDetails author_details;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;

    public Date getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    Date created_at;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Date getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    Date updated_at;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
}
