package com.example.bullet_journal.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ImagesDisplayAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.DeleteDiaryImageAsyncTask;
import com.example.bullet_journal.async.GetDiaryImagesAsyncTask;
import com.example.bullet_journal.async.InsertDiaryImageAsyncTask;
import com.example.bullet_journal.helpClasses.AlbumItem;
import com.example.bullet_journal.model.DiaryImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AlbumActivity extends RootActivity {
    private Activity context= this;

    private GridView gridView;
    private FloatingActionButton addPicture;

    private MenuItem deleteButton;
    private MenuItem cancelSelection;

    private ImagesDisplayAdapter imagesAdapter;

    static final Integer CAMERA = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer READ_MULTIPLE = 0x4;

    private String currentPhotoPath;
    private Uri photo_uri;

    private List<AlbumItem> items = new ArrayList<>();;
    private List<AlbumItem> selectedItems= new ArrayList<AlbumItem>();
    private boolean isSelectionMode;

    private long dayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dayId= getIntent().getLongExtra("dayId", dayId);

        addPicture = (FloatingActionButton) findViewById(R.id.attach_picture);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence options[] = new CharSequence[]{getString(R.string.take_a_pic), getString(R.string.choose_img), getString(R.string.choose_multiple_img)};

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.choose_option));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent innerIntent;
                        switch (which){
                            case 0:{
                                if(checkPermission(Manifest.permission.CAMERA, CAMERA)) {
                                    dispatchTakePictureIntent();
                                }
                                else{
                                    askPermission(Manifest.permission.CAMERA, CAMERA);
                                }
                                break;
                            }
                            case 1: {
                                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST)) {
                                    innerIntent = new Intent();
                                    innerIntent.setType("image/*");
                                    innerIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(innerIntent, "Select Picture"),READ_EXST);
                                }else{
                                    askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                                }
                                break;
                            }
                            case 2: {
                                innerIntent= new Intent(Intent.ACTION_GET_CONTENT );
                                innerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                innerIntent.setType("image/*");
                                startActivityForResult(Intent.createChooser(innerIntent, "Select Pictures"), READ_MULTIPLE);
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
            }
        });
        initData();

        gridView= (GridView)findViewById(R.id.imagesGridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), FullScreenImageViewActivity.class);
                intent.putExtra("selected", position);
                intent.putExtra("dayId", dayId);
                startActivity(intent);

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.get(position).setSelected(!items.get(position).isSelected());
                imagesAdapter.notifyDataSetChanged();
                if(items.get(position).isSelected()){
                    selectedItems.add(items.get(position));
                }else{
                    selectedItems.remove(items.get(position));
                }
                 if(selectedItems.size()!=0){
                     isSelectionMode=true;
                 }
                 else{
                     isSelectionMode=false;
                 }
                cancelSelection.setVisible(isSelectionMode);
                deleteButton.setVisible(isSelectionMode);
                return true;
            }
        });


        setResult(Activity.RESULT_OK);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        deleteButton= menu.findItem(R.id.delete_pics);
        cancelSelection= menu.findItem(R.id.cancel_operation);
        return super.onPrepareOptionsMenu(menu);
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

    private boolean checkPermission(String permission, Integer requestCode){
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }

    private void askPermission(String permission, Integer requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            //if user has denied permission before
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent;
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //take a shot
                case 2:
                    dispatchTakePictureIntent();
                    break;

                //read one image from external storage
                case 3:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),READ_EXST);
                    break;

                //read multiple images from storage
                case 4:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_multiple_img)),READ_MULTIPLE);
                    break;
            }
        }else{
            Toast.makeText(this, "We need permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode){
                    //take a picture
                    case 2: {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo_uri);
                        String uri=saveToInternalStorage(photo);
                        insertDiaryImage(uri);
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
                            String uri= saveToInternalStorage(imgBitmap);
                            inputStream.close();
                            insertDiaryImage(uri);
                        }catch(FileNotFoundException ex)
                        {
                        }catch(IOException ex)
                        {
                        }
                        break;
                    }

                    //select multiple photos
                    case 4: {
                        ClipData clipData= data.getClipData();
                        ClipData.Item item;
                        Uri uri;
                        for(int i=0; i<clipData.getItemCount(); i++){
                            item= clipData.getItemAt(i);
                            uri= item.getUri();
                            ContentResolver contentResolver = getContentResolver();

                            try {
                                // Open the file input stream by the uri.
                                InputStream inputStream = contentResolver.openInputStream(uri);

                                // Get the bitmap.
                                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                                String storageuri= saveToInternalStorage(imgBitmap);
                                inputStream.close();
                                insertDiaryImage(storageuri);
                            }catch(FileNotFoundException ex)
                            {
                            }catch(IOException ex)
                            {
                            }
                        }
                        break;
                    }
                }

            }
//            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        Calendar cal = Calendar.getInstance();

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,cal.getTimeInMillis()+".jpg");
        Toast.makeText(getApplicationContext(), String.valueOf(mypath), Toast.LENGTH_LONG);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 5, fos);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean deleted= false;
        switch (id) {
            case R.id.delete_pics: {
                for (AlbumItem selected: selectedItems
                     ) {
                    deleteDiaryImage(selected.getId());
                    items.remove(selected);
                    File file = new File(selected.getImageUri().toString());
                    if(file!=null)
                        deleted= file.delete();
                }
                selectedItems.clear();
                imagesAdapter.notifyDataSetChanged();
                cancelSelection.setVisible(isSelectionMode);
                deleteButton.setVisible(isSelectionMode);
                break;
            }
            case R.id.cancel_operation: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData(){
        AsyncTask<Long, Void, List<DiaryImage>> getImagesTask = new GetDiaryImagesAsyncTask(AlbumActivity.this, new AsyncResponse<List<DiaryImage>>(){

            @Override
            public void taskFinished(List<DiaryImage> retVal) {
                items= new ArrayList<AlbumItem>();
                if(retVal!=null){
                    for (DiaryImage item: retVal
                    ) {
                        items.add(new AlbumItem(Uri.parse(item.getPath()), false, item.getId()));

                    }
                }
                imagesAdapter= new ImagesDisplayAdapter(getBaseContext(), items);
                gridView.setAdapter(imagesAdapter);
            }
        }).execute(dayId);
    }

    private void insertDiaryImage(String path){
        DiaryImage newImg= new DiaryImage(null, null, path, dayId, false);
        AsyncTask<DiaryImage, Void, DiaryImage> insertImageTask = new InsertDiaryImageAsyncTask(AlbumActivity.this, new AsyncResponse<DiaryImage>(){

            @Override
            public void taskFinished(DiaryImage addedImg) {
                items.add(new AlbumItem(Uri.parse(addedImg.getPath()), false, addedImg.getId()));
                imagesAdapter.notifyDataSetChanged();
            }
        }).execute(newImg);
    }

    private void deleteDiaryImage(long id){
        AsyncTask<Long, Void, Boolean> deleteImageTask = new DeleteDiaryImageAsyncTask(AlbumActivity.this, new AsyncResponse<Boolean>(){
            @Override
            public void taskFinished(Boolean result) {
                isSelectionMode= !result;
                cancelSelection.setVisible(isSelectionMode);
                deleteButton.setVisible(isSelectionMode);
            }
        }).execute(id);


    }

    @Override
    protected void onPause() {
        super.onPause();
//        diary.setAlbumItems(items);
//        MockupData.updateDate(diary);
    }

    @Override
    public void onBackPressed() {
        if(isSelectionMode){
            isSelectionMode=false;
            for (AlbumItem item: items
                 ) {
                item.setSelected(false);
                cancelSelection.setVisible(isSelectionMode);
                deleteButton.setVisible(isSelectionMode);
            }
            if(imagesAdapter!=null)
                imagesAdapter.notifyDataSetChanged();
        }
        else{
            super.onBackPressed();

        }
    }
}
