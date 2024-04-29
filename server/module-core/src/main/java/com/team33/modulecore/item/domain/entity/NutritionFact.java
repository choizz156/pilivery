package com.team33.modulecore.item.domain.entity;

import com.team33.modulecore.item.dto.NutritionFactPostDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NutritionFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrition_fact_id")
    private Long id;

    private String ingredient;

    @Column(nullable = false)
    private String volume;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private NutritionFact(Item item, String ingredient, String volume) {
        this.item = item;
        this.ingredient = ingredient;
        this.volume = volume;
    }

    public static List<NutritionFact> createList(List<NutritionFactPostDto> nutritionFacts) {
        return nutritionFacts.stream()
            .map(dto -> NutritionFact.builder()
                .ingredient(dto.getIngredient())
                .volume(dto.getVolume())
                .build()
            )
            .collect(Collectors.toUnmodifiableList());
    }

    public void addItem(Item item) {
        this.item = item;
    }
}
