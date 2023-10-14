package com.cybercenter.core.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "scopes")
@Data
public class ScopeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "enable", nullable = false)
    private boolean enable;
}
