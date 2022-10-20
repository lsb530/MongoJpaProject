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
@Document
public class Users implements Persistable<ObjectId> {

    @Id
    private ObjectId id;

    private String name;

    private String email;

    private String password;

    @Override
    public boolean isNew() {
        return false;
    }
}
