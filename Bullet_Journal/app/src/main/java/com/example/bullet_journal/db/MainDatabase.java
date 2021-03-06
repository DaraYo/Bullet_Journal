package com.example.bullet_journal.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bullet_journal.converters.Converter;
import com.example.bullet_journal.db.dao.DayDao;
import com.example.bullet_journal.db.dao.DiaryImageDao;
import com.example.bullet_journal.db.dao.HabitDao;
import com.example.bullet_journal.db.dao.MonthlyBudgetDao;
import com.example.bullet_journal.db.dao.HabitDayDao;
import com.example.bullet_journal.db.dao.MoodDao;
import com.example.bullet_journal.db.dao.RatingDao;
import com.example.bullet_journal.db.dao.ReminderDao;
import com.example.bullet_journal.db.dao.TaskEventDao;
import com.example.bullet_journal.db.dao.UserDao;
import com.example.bullet_journal.db.dao.WalletItemDao;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.DiaryImage;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.model.User;
import com.example.bullet_journal.model.WalletItem;

@Database(entities = {
        User.class,
        Day.class,
        DiaryImage.class,
        Mood.class,
        Rating.class,
        MonthlyBudget.class,
        WalletItem.class,
        Task.class,
        Habit.class,
        HabitDay.class,
        Reminder.class
}, version = 11)
@TypeConverters({Converter.class})
public abstract class MainDatabase extends RoomDatabase {

    public abstract MoodDao getMoodDao();

    public abstract DayDao getDayDao();

    public abstract HabitDao getHabitDao();

    public abstract ReminderDao getReminderDao();

    public abstract TaskEventDao getTaskEventDao();

    public abstract RatingDao getRatingDao();

    public abstract DiaryImageDao getDiaryImageDao();

    public abstract HabitDayDao getHabitDayDao();

    public abstract WalletItemDao getWalletItemDao();

    public abstract MonthlyBudgetDao getMonthlyBudgetDao();

    public abstract UserDao getUserDao();

}
