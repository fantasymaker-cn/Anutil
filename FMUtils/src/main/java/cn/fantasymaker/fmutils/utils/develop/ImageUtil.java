/*
 *  Copyright 2016 Lixplor
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.fantasymaker.fmutils.utils.develop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created :  2016-07-25
 * Author  :  Lixplor
 * Web     :  http://blog.lixplor.com
 * Email   :  me@lixplor.com
 */
public class ImageUtil {

    /*
    todo
    图像压缩算法?
     */

    private ImageUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Instatiation is not allowed! Use static methods only!");
    }

    /**
     * convert Bitmap to byte array
     *
     * @param bitmap bitmap to be converted
     * @return a byte array
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //compress bitmap to an output stream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        //convert the output stream to a byte array and return
        return out.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b byte array to be converted
     * @return a bitmap object
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to byte array
     *
     * @param drawable drawable to be converted
     * @return a byte array
     */
    public static byte[] drawableToByte(Drawable drawable) {
        return drawable == null ? null : bitmapToByte(drawableToBitmap(drawable));
    }

    /**
     * convert byte array to Drawable
     *
     * @param b byte array to be converted
     * @return a drawable object
     */
    public static Drawable byteToDrawable(byte[] b) {
        return (b == null || b.length == 0) ? null : bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * convert a Drawable to a Bitmap
     *
     * @param drawable the Drawable object to be converted
     * @return a Bitmap object
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     *
     * @param bitmap bitmap to be converted
     * @return a drawable object
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * resize image to a target size
     *
     * @param oriBitmap    bitmap to be resized
     * @param targetWidth  target width
     * @param targetHeight target height
     * @return a scaled new bitmap
     */
    public static Bitmap resizeImage(Bitmap oriBitmap, int targetWidth, int targetHeight) {
        return oriBitmap == null ? null : resizeImageByScale(oriBitmap, (float) targetWidth / oriBitmap.getWidth(), (float) targetHeight / oriBitmap.getHeight());
    }

    /**
     * resize image by specified scale of width and height
     *
     * @param oriBitmap     bitmap to be resized
     * @param scaleOfWidth  sacle of width
     * @param scaleOfHeight scale of height
     * @return a scaled new bitmap
     */
    public static Bitmap resizeImageByScale(Bitmap oriBitmap, float scaleOfWidth, float scaleOfHeight) {
        if (oriBitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleOfWidth, scaleOfHeight);
        return Bitmap.createBitmap(oriBitmap, 0, 0, oriBitmap.getWidth(), oriBitmap.getHeight(), matrix, true);
    }

    /**
     * Rotate a bitmap to a specific degree
     *
     * @param bitmap bitmap to be rotated
     * @param degree target degree
     * @return rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * Get exif rotate degree of a JPEG file
     *
     * @param path JPEG file path
     * @return rotate degree
     */
    public static int getExifRotation(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Compress bitmap
     *
     * @param bitmap         bitmap to be compressed
     * @param compressedFile compressed file
     * @param quality        0-100 for JPEG
     * @return true if compressed success; Otherwise false
     */
    public static boolean compressBitmap(Bitmap bitmap, File compressedFile, int quality) {
        boolean compressSuccess = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(compressedFile);
            compressSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return compressSuccess;
    }
}