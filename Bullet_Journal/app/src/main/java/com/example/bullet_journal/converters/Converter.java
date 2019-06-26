package com.example.bullet_journal.converters;

import androidx.room.TypeConverter;

import com.example.bullet_journal.enums.RatingCategory;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.enums.WalletItemType;

public class Converter {

    @TypeConverter
    public String fromRatingCategoryToString(RatingCategory category){
        switch (category) {
            case ACTIVITY: return "ACTIVITY";
            case BOOK: return "BOOK";
            case MOVIE: return "MOVIE";
            case MUSIC: return "MUSIC";
        }
         return null;
    }

    @TypeConverter
    public RatingCategory fromIntToRatingCategory(String category){
        switch (category) {
            case "ACTIVITY": return RatingCategory.ACTIVITY;
            case "BOOK": return RatingCategory.BOOK;
            case "MOVIE": return RatingCategory.MOVIE;
            case "MUSIC": return RatingCategory.MUSIC;
        }
        return null;
    }

    @TypeConverter
    public String fromTaskTypeToString(TaskType taskType){
        switch (taskType) {
            case EVENT: return "EVENT";
            case TASK: return "TASK";
        }
        return null;
    }

    @TypeConverter
    public TaskType fromStringToTaskType(String taskType){
        switch (taskType) {
            case "EVENT": return TaskType.EVENT;
            case "TASK": return TaskType.TASK;
        }
        return null;
    }

    @TypeConverter
    public String fromWalletItemTypeToString(WalletItemType walletItemType){
        switch (walletItemType) {
            case INCOME: return "INCOME";
            case SPENDING: return "SPENDING";
        }
        return null;
    }

    @TypeConverter
    public WalletItemType fromStringToWalletItemType(String walletItemType){
        switch (walletItemType) {
            case "INCOME": return WalletItemType.INCOME;
            case "SPENDING": return WalletItemType.SPENDING;
        }
        return null;
    }

}
