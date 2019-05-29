package com.example.bullet_journal.wrapperClasses;


import com.example.bullet_journal.enums.RatingCategory;

public class RatingCategoryWrapper {

    private RatingCategory category;
    private int categoryNameIdx;

    public RatingCategoryWrapper(){}

    public RatingCategoryWrapper(RatingCategory category, int categoryNameIdx) {
        this.category = category;
        this.categoryNameIdx = categoryNameIdx;
    }

    public RatingCategory getCategory() {
        return category;
    }

    public void setCategory(RatingCategory category) {
        this.category = category;
    }

    public int getCategoryNameIdx() {
        return categoryNameIdx;
    }

    public void setCategoryNameIdx(int categoryNameIdx) {
        this.categoryNameIdx = categoryNameIdx;
    }
}
