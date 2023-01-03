package com.src.speedreadingapp.jpa.numbersdisappearexerciselog;

import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumbersDisappearExerciseLogRepository extends JpaRepository<NumbersDisappearExerciseLog, Long> {
}
