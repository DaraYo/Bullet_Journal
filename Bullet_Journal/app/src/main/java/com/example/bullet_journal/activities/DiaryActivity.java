package com.example.bullet_journal.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.predefinedClasses.CustomAppBarLayoutBehavior;
import com.example.bullet_journal.predefinedClasses.LinedEditText;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dayDisplay;
    private TextView dateDisplay;
    private LinedEditText diaryContent;
    private RelativeLayout editTextToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout.LayoutParams layoutParams;

    private String choosenDate = "";
    private int dayNum = 6;
    private String textContent;
    private String title = "Diary";

    static final Integer LOCATION= 0x1;
    static final Integer CAMERA = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer REQUEST_GET_SINGLE_FILE = 0x4;


//    private String pictureFilePath;
//    private FirebaseStorage firebaseStorage;
//    private String deviceIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("Diary");
        layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar_collapse);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_collapse_bar);

        floatingActionButton= findViewById(R.id.gallery);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AlbumActivity.class);
                startActivity(intent);
            }
        });

        diaryContent = findViewById(R.id.diary_text);
        textContent = diaryContent.getText().toString();

        dayDisplay = findViewById(R.id.day_display_only);
        dateDisplay = findViewById(R.id.date_display_only);

        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);
        dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis())); //+" "+choosenDate);

        RelativeLayout dateSwitchPannel = findViewById(R.id.current_date_layout_2);

        dateSwitchPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DiaryActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month+1, year);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                choosenDate = targetFormat.format(newDate);

                dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));//+" "+choosenDate);
                dateDisplay.setText(choosenDate);
            }
        };

//        ImageButton takePhotoBtn = (ImageButton) findViewById(R.id.take_a_picture);
//        ImageButton attachPicBtn = (ImageButton) findViewById(R.id.attach_picture);
//        ImageButton goToGallery= (ImageButton) findViewById(R.id.go_to_gallery);
//        goToGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(context, AlbumActivity.class);
//                startActivity(intent);
//            }
//        });
//        ImageButton attachLocationBtn = (ImageButton) findViewById(R.id.attach_location);

//        String lang= PreferencesHelper.getLanguage(this);
//        Toast.makeText(this, lang, Toast.LENGTH_LONG).show();

        editTextToolbar = findViewById(R.id.edit_text_toolbar);

        diaryContent.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(diaryContent.hasFocus()){
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        diaryContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    appBarLayout.setExpanded(false, true);
                    ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(false);
                    editTextToolbar.setVisibility(View.VISIBLE);
                } else {
                    ((CustomAppBarLayoutBehavior)layoutParams.getBehavior()).setScrollBehavior(true);
                    editTextToolbar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.add_pic).setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.add_pic).setVisible(true);
        return true;
    }

    public void disableCollapse() {
//        disableScroll();
//        appBarLayout.setEnabled(false);
//        appBarLayout.setActivated(false);

//        imageView.setVisibility(View.GONE);
//        collapsingToolbarLayout.setTitleEnabled(false);

//        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
//        layoutParams.setScrollFlags(0);
//        collapsingToolbarLayout.setLayoutParams(layoutParams);
//        collapsingToolbarLayout.setActivated(false);
//
//        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
//        appBarLayout.requestLayout();

//        toolbar.setTitle(title);
//        appBarLayout.setExpanded(false, false);
//        appBarLayout.setLiftable(false);
    }

    private boolean checkPermission(String permission, Integer requestCode){
        if (ContextCompat.checkSelfPermission(this, permission)//Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }

    private void askPermission(String permission, Integer requestCode){
//        if (ContextCompat.checkSelfPermission(this, permission)//Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            //if user has denied permission before
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);//{Manifest.permission.CAMERA}, CAMERA);
        }
//            return false;
//        } else {
////            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
//            return true;
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.add_pic: {
//                final CharSequence options[];
//                if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//                    options = new CharSequence[] {"Select photo", "Select multiple photos"};
//                }else {
                final CharSequence options[] = new CharSequence[]{"Take photo", "Select photo", "Select multiple photos"};
//                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Select your option:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent innerIntent;
                        switch (which){
                            case 0:{
                                if(checkPermission(Manifest.permission.CAMERA, CAMERA)) {
                                    innerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (innerIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(innerIntent, CAMERA);
                                    }
                                }
//                                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)//Manifest.permission.CAMERA)
//                                        != PackageManager.PERMISSION_GRANTED) {
//                                    innerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    if (innerIntent.resolveActivity(getPackageManager()) != null) {
//                                        startActivityForResult(innerIntent, 12);
//                                    }
//                                }
                                else{
                                    askPermission(Manifest.permission.CAMERA, CAMERA);
                                }
                                break;
                            }
                            case 1: {
                                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST)) {
                                    innerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(innerIntent, READ_EXST);
                                }else{
                                    askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                                }
                                break;
                            }
                            case 2: {
                                break;
                            }
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent;
//        Toast.makeText(getApplicationContext(), permissions[0], Toast.LENGTH_LONG).show();

        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //location
                case 1:
                    break;
                //take a shot
                case 2:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA);
                    }
//                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    startActivityForResult(intent, CAMERA);
                    break;
                //read external storage
                case 3:
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, READ_EXST);
                    break;
                //read one file
                case 4:
//                    intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE);
                    break;

            }
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "We need permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), String.valueOf(resultCode), Toast.LENGTH_LONG).show();
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode){
                    case 1:{
                        break;
                    }
                    case 2: {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(photo);
                        break;
                    }
                    case 3:
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    imageView.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    //add picture to firebase storage
//    private void addToCloudStorage() {
//        File f = new File(pictureFilePath);
//        Uri picUri = Uri.fromFile(f);
//        final String cloudFilePath = deviceIdentifier + picUri.getLastPathSegment();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageRef = firebaseStorage.getReference();
//        StorageReference uploadeRef = storageRef.child(cloudFilePath);
//
//        uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener(){
//            public void onFailure(@NonNull Exception exception){
//                Log.e(TAG,"Failed to upload picture to cloud storage");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
//                Toast.makeText(CapturePictureActivity.this,
//                        "Image has been uploaded to cloud storage",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}