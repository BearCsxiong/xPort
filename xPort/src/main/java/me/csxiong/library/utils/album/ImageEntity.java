package me.csxiong.library.utils.album;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageEntity implements Parcelable {

    private static final long serialVersionUID = -422977773784519140L;

    private String displayPath;

    private String displayName;

    private long size;

    public ImageEntity(String displayPath, String displayName, long size) {
        this.displayPath = displayPath;
        this.displayName = displayName;
        this.size = size;
    }

    protected ImageEntity(Parcel in) {
        displayPath = in.readString();
        displayName = in.readString();
        size = in.readLong();
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            return new ImageEntity(in);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    public String getDisplayPath() {
        return displayPath;
    }

    public void setDisplayPath(String displayPath) {
        this.displayPath = displayPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayPath);
        dest.writeString(displayName);
        dest.writeLong(size);
    }
}
