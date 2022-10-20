package com.example.mongojpapractice.mflix.repository;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;

public interface MflixCustomRepository {
    AggregationResults<Object> findTestAggregation(int age);
}
