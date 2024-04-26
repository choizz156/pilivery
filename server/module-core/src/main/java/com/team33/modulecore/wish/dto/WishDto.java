package com.team33.modulecore.wish.dto;

import com.team33.modulecore.item.dto.NutritionFactResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.team33.modulecore.item.domain.Brand;

public class WishDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishItemResponseDto {

        private long itemId;
        private String thumbnail;
        private String title;
        private String content;
        private int capacity;
        private int price;
        private double discountRate;
        private int discountPrice;
        private Brand brand;
        private List<NutritionFactResponseDto> nutritionFacts;
        private double starAvg;
        private int reviewSize;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishResponseDto {

        private long itemId;
        private int wish;
        private int totalWishes;
    }

}
