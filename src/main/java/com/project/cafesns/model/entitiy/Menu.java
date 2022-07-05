package com.project.cafesns.model.entitiy;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String menuimg;

    @Column(nullable = false)
    private String menuname;

    @Column(nullable = false)
    private String menuprice;

    @ManyToOne
    @JoinColumn(name = "cafeid")
    private Cafe cafe;
}
