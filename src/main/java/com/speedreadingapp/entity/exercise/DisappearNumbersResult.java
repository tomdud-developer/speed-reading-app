package com.speedreadingapp.entity.exercise;

import com.speedreadingapp.entity.ApplicationUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "disappear_number_result")
public class DisappearNumbersResult {

    @Id
    @SequenceGenerator(
            name = "disappear_number_result_sequence",
            sequenceName = "disappear_number_result_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "disappear_number_result_sequence"
    )
    @Immutable
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    ApplicationUser applicationUser;
}
