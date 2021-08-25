package com.thephiloswap.thephiloswapbeta;

public class Book {

    private String title;
    private String author;
    private String ownerObjectId;

    private String objectId;
    private String description;
    private String keywords;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwnerObjectId() {
        return ownerObjectId;
    }

    public void setOwnerObjectId(String ownerObjectId) {
        this.ownerObjectId = ownerObjectId;
    }
}
