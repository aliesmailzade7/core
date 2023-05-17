package com.cybercenter.core.secority.Entity;

import com.cybercenter.core.secority.constant.LoginMethodType;
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
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private LoginMethodType loginMethodType;
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

}
