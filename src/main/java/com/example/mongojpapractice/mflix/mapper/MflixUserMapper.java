package com.example.mongojpapractice.mflix.mapper;

import com.example.mongojpapractice.GenericMapper;
import com.example.mongojpapractice.mflix.document.Users;
import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MflixUserMapper extends GenericMapper<MflixUserRes, Users> {
    MflixUserMapper INSTANCE = Mappers.getMapper(MflixUserMapper.class);
}
