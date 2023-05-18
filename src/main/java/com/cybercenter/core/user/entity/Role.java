package com.cybercenter.core.user.entity;

import com.cybercenter.core.user.constant.Authority;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Role implements Serializable {

    @Id
    private Integer id;

    private String name;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Authority> authorities;
}
