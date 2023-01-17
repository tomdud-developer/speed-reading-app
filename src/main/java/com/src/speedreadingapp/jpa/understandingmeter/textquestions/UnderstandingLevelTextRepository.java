package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnderstandingLevelTextRepository extends JpaRepository<UnderstandingLevelText, Long> {
    @Query(value="SELECT x FROM understanding_level_text x WHERE x.index = ?1")
    UnderstandingLevelText getTextByIndex(int i);
}
