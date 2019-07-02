package com.example.bullet_journal.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bullet_journal.R;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MoodsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MoodsWidgetItemFactory(getApplicationContext(), intent);
    }

    class MoodsWidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private int taskEventWidgetId;
        private List<Task> tasks = new ArrayList<>();
        private CountDownLatch doneSignal = new CountDownLatch(1);
        private MainDatabase database;

        public MoodsWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.taskEventWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            Log.i("WIDGET", "ON CREATE");
            database = DatabaseClient.getInstance(context).getDatabase();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Day day = database.getDayDao().getByDate(CalendarCalculationsUtils.trimTimeFromDateMillis(System.currentTimeMillis()));
                        List<Task> tempTasks = database.getTaskEventDao().getFollowingTasksAndEventsForDay(day.getId(), System.currentTimeMillis());
                        tasks.addAll(tempTasks);
                        doneSignal.countDown();
                    }catch (Exception e){
                        tasks.clear();
                    }
                }
            }).start();

            try{
                doneSignal.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            return tasks.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mood_widget_box);

            Task taskObj = tasks.get(position);

            views.setTextViewText(R.id.task_widget_time, CalendarCalculationsUtils.dateMillisToStringTime(taskObj.getDate()));
            views.setTextViewText(R.id.task_event_widget_title, taskObj.getTitle());
            views.setTextViewText(R.id.task_event_widget_text, taskObj.getText());

            if(taskObj.getType().equals(TaskType.TASK)){
                views.setInt(R.id.task_event_widget_type, "setText", R.string.widget_task);
            }else{
                views.setInt(R.id.task_event_widget_type, "setText", R.string.widget_event);
            }

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
