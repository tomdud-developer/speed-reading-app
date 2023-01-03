package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnderstandingLevelTextRepository extends JpaRepository<UnderstandingLevelText, Long> {
}
