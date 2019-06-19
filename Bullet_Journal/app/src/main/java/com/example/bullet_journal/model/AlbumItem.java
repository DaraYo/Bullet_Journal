package com.example.bullet_journal.model;

import android.net.Uri;

public class AlbumItem {
    private Uri imageUri;
    private boolean isSelected;

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

    public AlbumItem(Uri imageUri, boolean isSelected) {
        this.imageUri = imageUri;
        this.isSelected = isSelected;
    }
}
