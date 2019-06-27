package com.example.bullet_journal.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.AlbumItem;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.helpClasses.MockupData;
import com.example.bullet_journal.helpClasses.Diary;
import com.example.bullet_journal.predefinedClasses.CustomAppBarLayoutBehavior;
import com.example.bullet_journal.predefinedClasses.LinedEditText;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    final Context context = this;
    private MaterialCalendarView calendarView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TextView dayDisplay;
    private TextView dateDisplay;
    private EditText diaryTitle;
    private LinedEditText diaryContent;
    private RelativeLayout editTextToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout.LayoutParams layoutParams;
    private CarouselView carouselView;
    private ImageView buttonDone;

    private ArrayList<Uri> listOfImages = new ArrayList<Uri>();

    private String choosenDate = "";
    private int dayNum = 6;
    private String textContent;
    private String title = "Diary";

    static final Integer LOCATION = 0x1;
    static final Integer CAMERA = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer READ_MULTIPLE = 0x4;
    static final Integer ALBUM_RESULT = 0x5;

    private Uri photo_uri;
    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("Diary");

        layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior) layoutParams.getBehavior()).setScrollBehavior(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar_collapse);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.gallery);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra("date", diary.getDiaryDate().getTime());
                startActivityForResult(intent, ALBUM_RESULT);
            }
        });
        diaryTitle = findViewById(R.id.diary_title);
        diaryContent = findViewById(R.id.diary_text);

        dayDisplay = findViewById(R.id.day_display_only);
        dateDisplay = findViewById(R.id.date_display_only);

        choosenDate = CalendarCalculationsUtils.dateMillisToString(System.currentTimeMillis());
        dateDisplay.setText(choosenDate);
        dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(System.currentTimeMillis())); //+" "+choosenDate);

        buttonDone = findViewById(R.id.diary_submit);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textContent = diaryTitle.getText().toString();
                textContent += diaryContent.getText().toString();
//                diaryTitle.clearFocus();
//                diaryContent.clearFocus();
                hideSoftKeyboard(DiaryActivity.this, v);
                diaryContent.clearFocus();

                editTextToolbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), textContent, Toast.LENGTH_LONG).show();
            }
        });

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
                saveData();
                Date newDate = CalendarCalculationsUtils.convertCalendarDialogDate(day, month + 1, year);
                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                choosenDate = targetFormat.format(newDate);

                dayDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(newDate.getTime()));//+" "+choosenDate);
                dateDisplay.setText(choosenDate);

                reLoadData(newDate);
            }
        };

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(listOfImages.size());
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                if (listOfImages.size() > 0) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.get().load(Uri.fromFile(new File(listOfImages.get(position).toString()))).into(imageView);
                }
            }
        });

        editTextToolbar = findViewById(R.id.edit_text_toolbar);

        diaryContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (diaryContent.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
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
                    ((CustomAppBarLayoutBehavior) layoutParams.getBehavior()).setScrollBehavior(false);
                    editTextToolbar.setVisibility(View.VISIBLE);
                } else {
                    ((CustomAppBarLayoutBehavior) layoutParams.getBehavior()).setScrollBehavior(true);
                    editTextToolbar.setVisibility(View.GONE);
                }
            }
        });

        //*************************************//
        reLoadData(Calendar.getInstance().getTime());

    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.add_pic).setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void askPermission(String permission, Integer requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //if user has denied permission before
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
//            ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photo_uri = FileProvider.getUriForFile(this,
                        "com.example.bullet_journal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
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
                final CharSequence options[] = new CharSequence[]{"Take photo", "Select photo", "Select multiple photos"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Select your option:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent innerIntent;
                        switch (which) {
                            case 0: {
                                if (checkPermission(Manifest.permission.CAMERA)) {
                                    dispatchTakePictureIntent();
                                } else {
                                    askPermission(Manifest.permission.CAMERA, CAMERA);
                                }
                                break;
                            }
                            case 1: {
                                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                    innerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                    startActivityForResult(innerIntent, READ_EXST);
                                    innerIntent = new Intent();
                                    innerIntent.setType("image/*");
                                    innerIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(innerIntent, "Select Picture"), READ_EXST);

//                                    innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PICK
//                                    innerIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                                    innerIntent.setType("image/*");
                                } else {
                                    askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                                }
                                break;
                            }
                            case 2: {
                                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    innerIntent = new Intent(Intent.ACTION_GET_CONTENT);//, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    innerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    innerIntent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(innerIntent, "Select Pictures"), READ_MULTIPLE);
                                } else {
                                    askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_MULTIPLE);
                                }
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
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {

                //location
                case 1:
                    break;

                //take a shot
                case 2:
                    dispatchTakePictureIntent();
                    break;

                //read one image from external storage
                case 3:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), READ_EXST);
                    break;

                //read multiple images from storage
                case 4:
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Select Pictures"), READ_MULTIPLE);
                    break;

            }
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "We need permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {

                    //location
                    case 1: {
                        break;
                    }

                    //take a picture
                    case 2: {
//                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo_uri);
                        String uri = saveToInternalStorage(photo);
                        listOfImages.add(Uri.parse(uri));
                        carouselView.setPageCount(listOfImages.size());
//                        imageView.setImageBitmap(photo);
                        break;
                    }

                    //choose photo
                    case 3: {
                        Uri selectedImageUri = data.getData();
                        ContentResolver contentResolver = getContentResolver();

                        try {
                            // Open the file input stream by the uri.
                            InputStream inputStream = contentResolver.openInputStream(selectedImageUri);

                            // Get the bitmap.
                            Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                            String uri = saveToInternalStorage(imgBitmap);
                            inputStream.close();
                            listOfImages.add(Uri.parse(uri));
                            carouselView.setPageCount(listOfImages.size());
                        } catch (FileNotFoundException ex) {
                        } catch (IOException ex) {
                        }
                        break;
                    }

                    //select multiple photos
                    case 4: {
                        ClipData clipData = data.getClipData();
                        ClipData.Item item;
                        Uri uri;
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            item = clipData.getItemAt(i);
                            uri = item.getUri();
                            ContentResolver contentResolver = getContentResolver();

                            try {
                                // Open the file input stream by the uri.
                                InputStream inputStream = contentResolver.openInputStream(uri);

                                // Get the bitmap.
                                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                                String storageuri = saveToInternalStorage(imgBitmap);
                                inputStream.close();
                                listOfImages.add(Uri.parse(storageuri));
                                carouselView.setPageCount(listOfImages.size());
                            } catch (FileNotFoundException ex) {
                            } catch (IOException ex) {
                            }
                        }
                        break;
                    }
                    case 5: {
                        if (diary != null)
                            reLoadData(diary.getDiaryDate());
                        break;
                    }

                    // Set the image in ImageView
//                    imageView.setImageURI(selectedImageUri);
                }
                if (listOfImages.size() > 0) {
                    carouselView.setIndicatorVisibility(View.VISIBLE);
                }
            }
//            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public String getPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
//        }
        return res;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Calendar cal = Calendar.getInstance();

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, cal.getTimeInMillis() + ".jpg");
        Toast.makeText(getApplicationContext(), String.valueOf(mypath), Toast.LENGTH_LONG);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 20, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getPath();
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void reLoadData(Date date) {
        diary = MockupData.getDiary(date);
        if (diary == null) {
            diary = new Diary();
            diary.setDiaryDate(date);
        }
        diaryTitle.setText(diary.getTitle());
        diaryContent.setText(diary.getThoughts());
        if (diary.getAlbumItems() != null && diary.getAlbumItems().size() > 0) {
            listOfImages = new ArrayList<>();
            for (AlbumItem item :
                    diary.getAlbumItems()) {
                listOfImages.add(item.getImageUri());
            }
            carouselView.setPageCount(listOfImages.size());
            carouselView.setIndicatorVisibility(View.VISIBLE);
        } else {
            listOfImages = new ArrayList<>();
            carouselView.setPageCount(1);
            carouselView.setIndicatorVisibility(View.INVISIBLE);
        }

    }

    private void saveData() {
        diary.setThoughts(diaryContent.getText().toString());
        diary.setTitle(diaryTitle.getText().toString());
        List<AlbumItem> albumItems = new ArrayList<>();
        for (Uri uri :
                listOfImages) {
            AlbumItem item = new AlbumItem(uri, false);
            albumItems.add(item);
        }
        diary.setAlbumItems(albumItems);

        MockupData.updateDate(diary);
    }
}