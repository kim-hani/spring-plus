package org.example.expert.domain.log.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class Log {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime timeStamp = LocalDateTime.now();

    @Column(nullable = false)
    private String details;

}
