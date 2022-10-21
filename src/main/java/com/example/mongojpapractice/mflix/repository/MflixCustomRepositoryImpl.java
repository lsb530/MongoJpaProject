package com.example.mongojpapractice.mflix.repository;

import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class MflixCustomRepositoryImpl implements MflixCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Lazy
    public MflixCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AggregationResults<?> findTestAggregation(int age) {

//        Criteria criteria = Criteria.where("email").regex("^jason");
//        MatchOperation matchStage = Aggregation.match(criteria);

        AddFieldsOperation addFieldStage = Aggregation.addFields()
                .addField("age").withValue(age).build();

        ProjectionOperation projectStage = Aggregation.project()
                .andExpression("$name").as("email");

//                .and("name").as("이메일")
//                .and("email").as("E-mail")
//                .and("password").as("pwd");

        Aggregation aggregation = Aggregation.newAggregation(
//                matchStage,
//                projectStage,
                addFieldStage
        );

        var aggResult = mongoTemplate.aggregate(aggregation, "payee", Object.class);
        return aggResult;
    }
}
