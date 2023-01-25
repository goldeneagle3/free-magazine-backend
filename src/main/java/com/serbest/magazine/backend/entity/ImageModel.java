package com.serbest.magazine.backend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "image_model")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String type;

    // @Formula("(select count(id) from image_model")
    // private int isImageExist;
    @Lob
    @Column(name = "pic_byte")
    private byte[] picByte;

    public ImageModel() {
    }

    public ImageModel(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }

    public ImageModel(UUID id, String name, String type, byte[] picByte) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }

    public UUID getId() {

        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }
}
