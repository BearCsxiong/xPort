package me.csxiong.library.utils.album;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*********************************************
 * Version : 9:58 AM
 * Author  : csxiong - 2019/5/17
 * Changes : init album
 *********************************************
 */
public class XAlbum extends AsyncTask<Void, Float, List<ImageEntity>> {

    private WeakReference<Context> context;

    public XAlbum(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ImageEntity> doInBackground(Void... voids) {
        //all photo
        List<ImageEntity> mediaBeen = new ArrayList<>();
        //album - photo
        HashMap<String, List<ImageEntity>> allPhotosTemp = new HashMap<>();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = {MediaStore.Images.Media._ID
                , MediaStore.Images.Media.DATA
                , MediaStore.Images.Media.SIZE
                , MediaStore.Images.Media.DISPLAY_NAME};

        //TODO 7.0 adapter contentprovider
        Cursor mCursor = context.get().getContentResolver().query(mImageUri,
                projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if (mCursor != null) {
            int maxCount = mCursor.getCount();
            float progress = 0f;
            while (mCursor.moveToNext()) {

                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                long size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024;
                String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                mediaBeen.add(new ImageEntity(path, displayName, size));
                String dirPath = new File(path).getParentFile().getAbsolutePath();
                if (allPhotosTemp.containsKey(dirPath)) {
                    List<ImageEntity> data = allPhotosTemp.get(dirPath);
                    data.add(new ImageEntity(path, displayName, size));
                    continue;
                } else {
                    List<ImageEntity> data = new ArrayList<>();
                    data.add(new ImageEntity(path, displayName, size));
                    allPhotosTemp.put(dirPath, data);
                }

                //progress update
                if (maxCount > 0) {
                    float newProgress = new BigDecimal(mediaBeen.size() / (float) maxCount).setScale(2, BigDecimal.ROUND_HALF_UP)
                            .floatValue();
                    if (newProgress != progress) {
                        progress = newProgress;
                        publishProgress(progress);
                    }
                }
            }
            mCursor.close();
        }
        return mediaBeen;
    }

    private OnProgressListener onProgressListener;

    private OnResultListener onResultListener;

    public XAlbum setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
        return this;
    }

    public XAlbum setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        return this;
    }

    @Override
    protected void onPostExecute(List<ImageEntity> imageEntities) {
        super.onPostExecute(imageEntities);
        if (onResultListener != null) {
            onResultListener.onResult(imageEntities);
        }
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
        if (onProgressListener != null && values.length != 0) {
            onProgressListener.onProgress(values[0]);
        }
    }
}
