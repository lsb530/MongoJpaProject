package com.example.mongojpapractice.mflix.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ObjectUtils;

public class MflixCustomRepositoryImpl implements MflixCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Lazy
    public MflixCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AggregationResults<Object> findTestAggregation(int age) {

        Criteria criteria = Criteria.where("email").regex("^jason");
        MatchOperation matchStage = Aggregation.match(criteria);

        AddFieldsOperation addFieldStage = Aggregation.addFields()
                .addField("age").withValue(age).build();
        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                addFieldStage
        );
        AggregationResults<Object> aggResult = mongoTemplate.aggregate(aggregation, "users", Object.class);
        return aggResult;
    }
}
