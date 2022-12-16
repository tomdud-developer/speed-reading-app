package com.src.speedreadingapp.jpa.schultzarraylogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchultzArrayLogRepository extends JpaRepository<SchultzArrayLog, Long> {
}
