package com.enhanced.sustenance.databases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.enhanced.sustenance.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JSON {
    private static final String TAG = "JSON";


    private static String imageName(String name) {
        return name + "_image.png";
    }

    public static void saveImage(Context context, BitmapDrawable bitmapDrawable, String name) {
        Runnable runnable = () -> {
            File file = new File(context.getFilesDir(), imageName(name));
            try (FileOutputStream out = context.openFileOutput(imageName(name), Context.MODE_PRIVATE)) {
                bitmapDrawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        MainActivity.getHandler().post(runnable);
    }

    public static BitmapDrawable getImage(Context context, String name) {
        BitmapDrawable bmp = null;
        if (new File(context.getFilesDir(), imageName(name)).exists()) {
            File file = new File(context.getFilesDir(), imageName(name));
            bmp = new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
        return bmp;
    }

    public static void deleteImage(Context context, String name) {
        if (new File(context.getFilesDir(), imageName(name)).exists()) {
            Runnable runnable = () -> {
                File file = new File(context.getFilesDir(), imageName(name));
                file.delete();
            };
            MainActivity.getHandler().post(runnable);
        }
    }


}
