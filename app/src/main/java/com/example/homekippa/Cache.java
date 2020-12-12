package com.example.homekippa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache {
    Context context;

    public Cache(Context context) {
        this.context = context;
    }

    public Cache() {

    }

    public Bitmap getBitmapFromCacheDir(String key) {
        String found = null;
        Bitmap bitmap = null;

        File file = new File(context.getCacheDir().toString());
        File[] files = file.listFiles();

        for (File tempFile : files) {
            if (tempFile.getName().contains(key)) {
                found = (tempFile.getName());
                String path = context.getCacheDir() + "/" + found;
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        return bitmap;
    }

    public void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = (context).getCacheDir();
        //저장할 파일 이름
        String fileName = name;

        try {
            File tempFile = new File(storage, fileName);
            tempFile.createNewFile();

            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Yes", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("Yes", "IOException : " + e.getMessage());
        }
    }

    public void saveBitmapToPNG(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = (context).getCacheDir();
        //저장할 파일 이름
        String fileName = name;

        try {
            File tempFile = new File(storage, fileName);
            tempFile.createNewFile();

            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Yes", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("Yes", "IOException : " + e.getMessage());
        }
    }
}
