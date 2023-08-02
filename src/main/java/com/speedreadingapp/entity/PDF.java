package com.speedreadingapp.entity;

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
@Table(name = "pdf")
public class PDF {

    @Id
    @SequenceGenerator(
            name = "application_user_sequence",
            sequenceName = "application_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_user_sequence"
    )
    @Immutable
    private long id;

    @Column(name = "name")
    private String name;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser applicationUser;

}
