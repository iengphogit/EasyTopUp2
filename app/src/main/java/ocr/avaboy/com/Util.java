package ocr.avaboy.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by iengpho on 9/29/18.
 */

public class Util {

    public static void setSharepreference(String PREF_NAME, String VALUE, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_NAME, VALUE);
        editor.apply();
    }

    public static String getSharepreference(String PREF_NAME, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_NAME, "phieDB2");
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmap.setHasAlpha(true);
        return stream.toByteArray();
    }

    public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap, int replaceThisColor) {
        if (bitmap != null) {
            int picw = bitmap.getWidth();
            int pich = bitmap.getHeight();
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);

            for (int y = 0; y < pich; y++) {
                // from left to right
                for (int x = 0; x < picw; x++) {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == replaceThisColor) {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }

                // from right to left
                for (int x = picw - 1; x >= 0; x--) {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == replaceThisColor) {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }
            }

            return Bitmap.createBitmap(pix, picw, pich, Bitmap.Config.ARGB_4444);
        }
        return null;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 10240;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] getBytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


    public static byte[] uriToByteArray(Context ctx, Uri uri) throws IOException {

        Bitmap scaledBitmap = null;
        InputStream fileInputStream = ctx.getApplicationContext().getContentResolver().openInputStream(uri);
        assert fileInputStream != null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);


        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 500.0f;
        float maxWidth = 500.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        InputStream fileInputStream1 = ctx.getApplicationContext().getContentResolver().openInputStream(uri);
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[32 * 1024];
        try {
            bitmap = BitmapFactory.decodeStream(fileInputStream1, null, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = null;
        if (scaledBitmap != null) {
            canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(new File(uri.getPath()).getAbsolutePath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            if (scaledBitmap != null) {
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(),
                        scaledBitmap.getHeight(),
                        matrix, true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (scaledBitmap != null) {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }
        return baos.toByteArray();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}

