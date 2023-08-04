package com.speedreadingapp.repository;

import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.PDF;
import com.speedreadingapp.entity.exercise.SpeedMeterResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PDFRepository extends JpaRepository<PDF, Long> {

    @Query("SELECT e FROM PDF e WHERE e.applicationUser.id = ?1")
    List<PDF> findAllByUserId(Long userId);

    @Query("SELECT e FROM PDF e WHERE e.applicationUser.id = ?1 AND e.name LIKE ?2")
    Optional<PDF> findPDFByNameAndUserId(Long userId, String name);

}
