package com.example.mongojpapractice.mflix.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Getter
@Setter
public class MflixUserRes implements Serializable {
    private ObjectId id;
    private String name;
    private String email;
    private String password;
}
