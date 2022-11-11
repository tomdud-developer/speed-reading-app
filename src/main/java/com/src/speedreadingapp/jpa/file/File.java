package com.src.speedreadingapp.jpa.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue
    Long id;

    @Lob
    byte[] bytes;
    String name;
    String extension;
}
