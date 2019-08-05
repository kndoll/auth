package com.example.auth.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Members {

    @Id
    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 72, nullable = false)
    private String password;

}
