package com.example.mongojpapractice.mflix.mapper;

import com.example.mongojpapractice.mflix.document.Users;
import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-20T21:47:27+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Amazon.com Inc.)"
)
public class MflixUserMapperImpl implements MflixUserMapper {

    @Override
    public MflixUserRes toDto(Users arg0) {
        if ( arg0 == null ) {
            return null;
        }

        MflixUserRes mflixUserRes = new MflixUserRes();

        mflixUserRes.setId( arg0.getId() );
        mflixUserRes.setName( arg0.getName() );
        mflixUserRes.setEmail( arg0.getEmail() );
        mflixUserRes.setPassword( arg0.getPassword() );

        return mflixUserRes;
    }

    @Override
    public Users toEntity(MflixUserRes arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Users users = new Users();

        users.setId( arg0.getId() );
        users.setName( arg0.getName() );
        users.setEmail( arg0.getEmail() );
        users.setPassword( arg0.getPassword() );

        return users;
    }
}
