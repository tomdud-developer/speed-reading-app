package com.src.speedreadingapp.jpa.understandingmeter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnderstandingLevelLogRepostitory extends JpaRepository<UnderstandingLevelLog, Long> {
}
