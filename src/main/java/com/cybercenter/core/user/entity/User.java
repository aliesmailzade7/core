package com.cybercenter.core.user.entity;

import com.cybercenter.core.auth.constant.LoginMethodType;
import com.cybercenter.core.user.constant.Education;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDay;
    private String email;
    private String phoneNumber;
    private String job;
    @Enumerated(EnumType.STRING)
    private Education education;
    private String orientation;
    @Enumerated(EnumType.STRING)
    private LoginMethodType loginMethodType;
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

}
