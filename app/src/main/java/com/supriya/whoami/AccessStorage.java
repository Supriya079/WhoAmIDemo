package com.supriya.whoami;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

public class AccessStorage extends Activity {

    public static Uri uri;
    public static String galleryImagePath,filesPath;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri createCameraImage(Context context){
        ContentResolver resolver = context.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName = String.valueOf(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"Forget/");
        Uri finalUri = resolver.insert(uri,contentValues);
        return finalUri;
    }

    public static String getImgPath(Uri uri,Activity context) {
        String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA };
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        Cursor myCursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                largeFileProjection, null, null, largeFileSort);
        String largeImagePath;
        try {
            myCursor.moveToFirst();
            largeImagePath = myCursor
                    .getString(myCursor
                            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        } finally {
            myCursor.close();
        }
        return largeImagePath;
    }

    public static String saveImageToGallery(Bitmap finalBitmap,Context context) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        context.sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                .parse("file://" + root)));
        File myDir = new File(root + "/Forget");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fName = "Image-" + n + ".jpg";
        File file = new File(myDir, fName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            galleryImagePath = file.getPath();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return galleryImagePath;
    }

    public static String getSourceFilePath(Context context, Uri contentUri) {
        if (contentUri.toString().contains("raw:/")) {
            return contentUri.getPath();
        }
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void copyFile(File source, File destination) throws IOException {

        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(destination).getChannel();

        try {
            in.transferTo(0, in.size(), out);
        } catch(Exception e){
            Log.d("Exception", e.toString());
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    public static String saveFileToStorage(Context context,String pathFileSource){

        String sourcePath = pathFileSource;
        Log.d("sourcePath: ",sourcePath);
        File source = new File(sourcePath);

//                String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TongueTwister/sup_1A.pdf";
        String destinationPath = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(destinationPath + "/ForgetFiles");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "File-" + n + ".pdf";
        File destination = new File(myDir, fname);
        if (destination.exists())
            destination.delete();
//                File destination = new File(destinationPath);
        try
        {
            copyFile(source, destination);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        filesPath = destination.getPath();
        return filesPath;
    }

    public static Bitmap pdfToBitmap(File pdfFile) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if(pageCount>0){
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

}
