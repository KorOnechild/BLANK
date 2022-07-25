package com.project.cafesns.model.entitiy;

import com.project.cafesns.model.dto.menu.ModifyMenuDto;
import com.project.cafesns.model.dto.menu.RegistMenuRequestDto;
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
    public Menu(RegistMenuRequestDto registMenuRequestDto, Cafe cafe, String menuimg) {
        this.category = registMenuRequestDto.getCategory();
        this.menuimg = menuimg;
        this.menuname = registMenuRequestDto.getMenuname();
        this.menuprice = registMenuRequestDto.getMenuprice();
        this.cafe = cafe;
    }

    public void changeMenu(ModifyMenuDto modifyMenuDto){
        this.category = modifyMenuDto.getCategory();
        this.menuname = modifyMenuDto.getMenuname();
        this.menuprice = modifyMenuDto.getMenuprice();
    }
}
