package com.example.mongojpapractice.mflix.repository;

import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

public interface MflixCustomRepository {
    AggregationResults<?> findTestAggregation(int age);
}
