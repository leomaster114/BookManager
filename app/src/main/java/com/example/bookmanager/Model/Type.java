package com.example.bookmanager.Model;

public class Type {
    private int id_type;
    private String typeName;

    public Type() {
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
