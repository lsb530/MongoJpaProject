//package com.example.mongojpapractice.controller.test;
//
//import com.example.mongojpalogic.test.recommendation.document.Recommendation;
//import com.example.mongojpalogic.test.recommendation.repository.RecommendationRepository;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/test")
//@RestController
//public class RecommendationController {
//
//    private final RecommendationRepository recommendationRepository;
//
//    @GetMapping("/recommendations")
//    public ResponseEntity<Object> getAllRecommendations() {
//        List<Recommendation> recommendations = recommendationRepository.findAll();
//        return ResponseEntity.ok(recommendations);
//    }
//
//}
