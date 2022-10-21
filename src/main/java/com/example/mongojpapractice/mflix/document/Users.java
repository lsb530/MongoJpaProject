package com.example.mongojpapractice.mflix.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
//@Document
public class Users {

    @Id
    private ObjectId id;

    private String name;

    private String email;

    private String password;
}
