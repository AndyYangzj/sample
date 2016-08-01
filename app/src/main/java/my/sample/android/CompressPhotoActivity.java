package my.sample.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CompressPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView photo;
    private TextView photoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_photo);
        Button takePhoto = (Button) findViewById(R.id.take_photo);
        photo = (ImageView) findViewById(R.id.photo);
        photoSize = (TextView) findViewById(R.id.photo_size);
        takePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                openCamera();
                break;
        }
    }

    private File mChosedImage;

    private void openCamera() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mChosedImage = new File(getImageFileNameHasDir(this));
            Uri u = Uri.fromFile(mChosedImage);
            intent.putExtra("orientation", 0);
            intent.putExtra("output", u);
            startActivityForResult(intent, 0x01);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x01) {
                choseImage();
            }
        }
    }

    private void choseImage() {
        if (mChosedImage.exists()) {
            photo.setImageBitmap(BitmapFactory.decodeFile(mChosedImage.getAbsolutePath()));
            Bitmap photoCopy = createThumbImage(mChosedImage.getAbsolutePath(), 800, 800);
            saveBitmap(mChosedImage.getPath(), photoCopy);
            try {
                photoSize.setText("\n压缩后大小->" + convertFileSize(getFileSize(mChosedImage)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CompressPhotoActivity.this, "获取照片失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public long getFileSize(File file) throws IOException {
        if (file != null && file.exists() && file.isFile()) {
            FileChannel fileChannel = null;
            FileInputStream fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();
            return fileChannel.size();
        } else {
            return 0;
        }
    }

    static public boolean saveBitmap(String filePath, Bitmap bm) {
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (folder.exists()) {
            folder.delete();
        }
        try {
            folder.createNewFile();
        } catch (IOException e1) {
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(folder);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            return true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 创建目录
     *
     * @return 如果目录已经存在，或者目录创建成功，返回true；如果目录创建失败，返回false
     */
    public static boolean createFolder(String folderPath) {
        boolean success = false;
        try {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                success = true;
            } else {
                success = folder.mkdirs();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static final String XXY_PATH = File.separator + "app_sample";
    /**
     * app 所有用户文件目录
     * <FILE_SDCARD||FILE_ANDROID>/xxy/user/
     */
    public static final String USER_DATA_PATH = XXY_PATH + File.separator + "user";
    /**
     * app 所有用户图片文件目录
     * * <FILE_SDCARD||FILE_ANDROID>/xxy/user/image/
     */
    public static final String USER_DATA_PATH_IMG = USER_DATA_PATH + File.separator + "image";

    /**
     * 不创建文件
     *n
     * @param ctx
     * @return
     */
    public static String getImageFileNameHasDir(Context ctx) {
        StringBuilder sBuilder = null;
        boolean isCreateFolder = createFolder(getDiskCacheDir(ctx) + USER_DATA_PATH_IMG);
        if (isCreateFolder) {
            sBuilder = new StringBuilder(getDiskCacheDir(ctx) + USER_DATA_PATH_IMG);
            sBuilder.append(File.separator);
            sBuilder.append(System.currentTimeMillis());//文件时间戳唯一标识
            sBuilder.append(".jpg");
        }
        return sBuilder != null ? sBuilder.toString() : null;
    }

    /**
     * 获取缓存文件目录，如果sd卡存在则缓存在sd卡中，否则缓存在内置内存中
     *
     * @param context 上下文
     * @return File 缓存文件目录
     */
    public static String getDiskCacheDir(Context context) {
        String cacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) {
            File file = Environment.getExternalStorageDirectory();
            if (file != null) {
                cacheDir = file.getAbsolutePath();
            } else {
                cacheDir = context.getCacheDir().getAbsolutePath();
            }
        } else {
            cacheDir = context.getCacheDir().getAbsolutePath();
        }

        return new File(cacheDir + File.separator).getAbsolutePath();
    }

    public Bitmap createThumbImage(String path, int w, int h) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int srcWidth = opts.outWidth; // 获取图片的原始宽度
        int srcHeight = opts.outHeight; // 获取图片原始高度

        float ratOrig = srcWidth / (float) srcHeight;
        float ratNew = w / (float) h;
        int wh = Math.min(w, h);
        int destW;
        int destH;
        if (srcWidth < wh || srcHeight < wh) {
            destW = srcWidth;
            destH = srcHeight;
        } else if (ratOrig < ratNew) {
            // 原图高多一些
            destH = h;
            destW = (int) Math.ceil(destH * srcWidth / (float) srcHeight);
        } else {
            destW = w;
            destH = (int) Math.ceil(destW * srcHeight / (float) srcWidth);
        }

        int r = srcWidth / destW;
        // 找到缩小最合适的倍数，缩放只能用2的倍数(1/2,1/4,1/8...)，找到个比预期稍大的
        // 直接使用按照sample采样获取的图，不再缩放了，减少缩放开销
        int sampleSize = 1;
        while (sampleSize <= r) {
            sampleSize *= 2;
        }

        sampleSize = Math.max(1, sampleSize / 2);

        int sampleW = (int) Math.ceil(srcWidth / (float) sampleSize);
        int sampleH = (int) Math.ceil(srcHeight / (float) sampleSize);
        while (sampleW > 800 || sampleH > 800) {
            // 最后缩放出来的的图一定要小于3000
            // 否则可能oom
            sampleSize *= 2;
            sampleW = (int) Math.ceil(srcWidth / (float) sampleSize);
            sampleH = (int) Math.ceil(srcHeight / (float) sampleSize);
        }

        opts = new BitmapFactory.Options();
        // 缩放的比例
        opts.inSampleSize = sampleSize;
        Bitmap decodeFile = BitmapFactory.decodeFile(path, opts);

        return decodeFile;
    }

}
