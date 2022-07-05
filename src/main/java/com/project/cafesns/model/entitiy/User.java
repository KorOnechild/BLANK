package com.project.cafesns.model.entitiy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String businessname;

    @Column
    private String businessnum;

    @Column
    private String profileimg;

    @Column
    private String logoimg;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-cafe-FK")
    private List<Cafe> cafeList;
}
