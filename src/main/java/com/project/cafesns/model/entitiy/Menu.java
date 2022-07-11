package com.project.cafesns.model.entitiy;

import com.project.cafesns.model.dto.cafe.RegistMenuRequestDto;
import lombok.Builder;
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
    private int menuprice;

    @ManyToOne
    @JoinColumn(name = "cafeid")
    private Cafe cafe;

    @Builder
    public Menu(RegistMenuRequestDto registMenuRequestDto, Cafe cafe) {
        this.category = registMenuRequestDto.getCategory();
        this.menuimg = registMenuRequestDto.getMenuimg();
        this.menuname = registMenuRequestDto.getMenuname();
        this.menuprice = registMenuRequestDto.getMenuprice();
        this.cafe = cafe;
    }
}
