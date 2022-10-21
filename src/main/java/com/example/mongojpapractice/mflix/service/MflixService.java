package com.example.mongojpapractice.mflix.service;

import com.example.mongojpapractice.mflix.document.Users;
import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import com.example.mongojpapractice.mflix.mapper.MflixUserMapper;
import com.example.mongojpapractice.mflix.repository.MflixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MflixService {
    private final MflixRepository mflixRepository;

    public List<MflixUserRes> getMflixUsers() {
        List<Users> users = mflixRepository.findAll();
        return users.stream().map(MflixUserMapper.INSTANCE::toDto).toList();
    }

    public Object getMflixUsersWithAge(int age) {
        AggregationResults<?> aggResult = mflixRepository.findTestAggregation(age);
        return aggResult.getMappedResults();
    }

}
