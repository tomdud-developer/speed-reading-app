package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnderstandingLevelQuestionRepository extends JpaRepository<UnderstandingLevelQuestion, Long> {
    @Query("SELECT u FROM understandinglevelquestion u WHERE u.understandingLevelText.id = ?1")
    public List<UnderstandingLevelQuestion> getQuestionsToTextNumberX(Long number);
}
