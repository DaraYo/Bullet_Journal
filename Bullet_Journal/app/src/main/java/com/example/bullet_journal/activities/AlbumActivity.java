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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.ImagesDisplayAdapter;
import com.example.bullet_journal.model.AlbumItem;

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
    Activity context= this;
    private static final int CONTENT_VIEW_ID = 10101010;

    FloatingActionButton addPicture;
    static final int PICK_IMAGE_MULTIPLE=1;
    static final Integer CAMERA = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer READ_MULTIPLE = 0x3;

    private String currentPhotoPath;
    private Uri photo_uri;

    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<Uri> listOfUris;


    GridView gridView;
    ImagesDisplayAdapter imagesAdapter;
    private List<AlbumItem> items;
    private boolean isSelectionMode;

    private GridView galleryGrid;
    private ImagesDisplayAdapter imagesDisplayAdapter;

    private MenuItem deleteButton;
    private MenuItem cancelSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPicture = (FloatingActionButton) findViewById(R.id.attach_picture);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence options[] = new CharSequence[]{"Take photo", "Select photo", "Select multiple photos"};
//                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Select your option:");
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
        // Inflate the layout for this fragment
        gridView= (GridView)findViewById(R.id.imagesGridview);
        imagesAdapter= new ImagesDisplayAdapter(this, items);
        gridView.setAdapter(imagesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "aaaaaaaaaa", Toast.LENGTH_LONG).show();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.get(position).setSelected(!items.get(position).isSelected());
                imagesAdapter.notifyDataSetChanged();
                for (AlbumItem item: items
                ) {
                    if(item.isSelected()){
                        isSelectionMode=true;
                        break;
                    }

                }
                cancelSelection.setVisible(isSelectionMode);
                deleteButton.setVisible(isSelectionMode);
                return true;
            }
        });
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("images", listOfUris);
//// set Fragmentclass Arguments
//        ImagesPreviewFragment fragobj = new ImagesPreviewFragment();
//        fragobj.setArguments(bundle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        deleteButton= menu.findItem(R.id.delete_pics);
        cancelSelection= menu.findItem(R.id.cancel_operation);
//        deleteButton.setVisible(true);

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
                    startActivityForResult(Intent.createChooser(intent, "Select Pictures"),READ_MULTIPLE);
                    break;
            }
        }else{
            Toast.makeText(this, "We need permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getApplicationContext(), String.valueOf(requestCode), Toast.LENGTH_LONG).show();
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode){
//                    case 1: {
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        imagesEncodedList = new ArrayList<String>();
//                        if(data.getData()!=null){
//
//                            Uri mImageUri=data.getData();
//
//                            // Get the cursor
//                            Cursor cursor = getContentResolver().query(mImageUri,
//                                    filePathColumn, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            imageEncoded  = cursor.getString(columnIndex);
//                            cursor.close();
//
//                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                            mArrayUri.add(mImageUri);
//                            imagesDisplayAdapter = new ImagesDisplayAdapter(getApplicationContext(),mArrayUri);
//                            galleryGrid.setAdapter(imagesDisplayAdapter);
//                            galleryGrid.setVerticalSpacing(galleryGrid.getHorizontalSpacing());
//                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) galleryGrid
//                                    .getLayoutParams();
//                            mlp.setMargins(0, galleryGrid.getHorizontalSpacing(), 0, 0);
//
//                        } else {
//                            if (data.getClipData() != null) {
//                                ClipData mClipData = data.getClipData();
//                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                                for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                                    ClipData.Item item = mClipData.getItemAt(i);
//                                    Uri uri = item.getUri();
//                                    mArrayUri.add(uri);
//                                    // Get the cursor
//                                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                                    // Move to first row
//                                    cursor.moveToFirst();
//
//                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                    imageEncoded  = cursor.getString(columnIndex);
//                                    imagesEncodedList.add(imageEncoded);
//                                    cursor.close();
//                                    imagesDisplayAdapter = new ImagesDisplayAdapter(getApplicationContext(),mArrayUri);
//                                    galleryGrid.setAdapter(imagesDisplayAdapter);
//                                    galleryGrid.setVerticalSpacing(galleryGrid.getHorizontalSpacing());
//                                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) galleryGrid
//                                            .getLayoutParams();
//                                    mlp.setMargins(0, galleryGrid.getHorizontalSpacing(), 0, 0);
//
//                                }
//                                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                            }
//                        }
//                    }
                    //take a picture
                    case 2: {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo_uri);
                        String uri=saveToInternalStorage(photo);
//                        listOfImages.add(Uri.parse(uri));
                        addNewAlbumItem(uri);
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
//                            listOfImages.add(Uri.parse(uri));
                            addNewAlbumItem(uri);
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
//                                listOfImages.add(Uri.parse(storageuri));
                                addNewAlbumItem(storageuri);
                            }catch(FileNotFoundException ex)
                            {
                            }catch(IOException ex)
                            {
                            }
                        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete_pics: {
                Toast.makeText(context, "brisi brisi suze s' lica", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent();
////                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
//                return true;
            }
            case R.id.cancel_operation: {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData(){
        items= new ArrayList<AlbumItem>(){
            {
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), true));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
                add(new AlbumItem(Uri.parse("http://images.math.cnrs.fr/IMG/png/section8-image.png"), false));
            }
        };
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

    private void addNewAlbumItem(String path){
        items.add(new AlbumItem(Uri.parse(path),false));
        imagesAdapter.notifyDataSetChanged();
    }
}
