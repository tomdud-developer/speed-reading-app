package com.speedreadingapp.repository;

import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisappearNumbersResultRepository extends JpaRepository<DisappearNumbersResult, Long> {

    @Query("SELECT e FROM DisappearNumbersResult e WHERE e.applicationUser.id = ?1")
    List<DisappearNumbersResult> findAllByUserId(Long userId);
}
