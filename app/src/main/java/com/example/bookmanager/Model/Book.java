package com.example.bookmanager.Model;

public class Book {
    private int id;
    private String bookName;
    private String author;
    private String type;
    private String description;
    private byte[] imageUri;

    public Book() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(byte[] imageUri) {
        this.imageUri = imageUri;
    }

    public Book(String bookName, String author, String type, String description, byte[] imageUri) {
        this.bookName = bookName;
        this.author = author;
        this.type = type;
        this.description = description;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImageUri() {
        return imageUri;
    }
}
