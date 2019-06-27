package com.example.bullet_journal.helpClasses;

import android.net.Uri;

import java.io.Serializable;

public class AlbumItem implements Serializable {
    private Uri imageUri;
    private boolean isSelected;
    private long id;

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public AlbumItem(Uri imageUri, boolean isSelected, long id) {
        this.imageUri = imageUri;
        this.isSelected = isSelected;
        this.id= id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
