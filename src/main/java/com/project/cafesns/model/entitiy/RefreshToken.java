package com.project.cafesns.model.entitiy;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshtoken;

    @Column(nullable = false)
    private Date expriyDate;

    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

    @Builder
    public RefreshToken(String refreshtoken, Date expriyDate, User user){
        this.refreshtoken = refreshtoken;
        this.expriyDate = expriyDate;
        this.user = user;
    }
}

