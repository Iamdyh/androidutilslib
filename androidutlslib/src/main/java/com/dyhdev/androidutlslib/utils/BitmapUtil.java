package com.dyhdev.androidutlslib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 *
 * @Date 2018/11/22  15:22
 * @Author DYH
 */
public class BitmapUtil {
    /**
     * 将图片转换成byte数组
     * @param bmpSrc
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bmpSrc){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmpSrc.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 将byte数组转换成bitmap
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b){
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 把bitmap转换成Base64编码String类型
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap){
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);
    }

    /**
     * 将drawable转换成bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * 将bitmap转换成drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap){
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * 缩放图片
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleBitmapTo(Bitmap bitmap, int newWidth, int newHeight){
        return scaleBitmap(bitmap, (float)newWidth/bitmap.getWidth(), (float) newHeight/bitmap.getHeight());
    }

    /**
     * 对bitmap进行缩放
     * @param bitmap
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight){
        if(bitmap == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width/2, height/2, width/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 创建小bitmap
     * @param bitmap
     * @param needRecycle
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycle, int newWidth, int newHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //缩放比例
        float scaleWidth = ((float)newWidth)/width;
        float scaleHeight = ((float)newHeight)/height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if(needRecycle){
            bitmap.recycle();
        }
        return newBitmap;
    }

    /**
     * 保存bitmap
     * @param bitmap
     * @param file
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, File file){
        if(bitmap == null){
            return false;
        }
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 图像灰度化
     * @param bmpSrc
     * @return
     */
    public static Bitmap bitmapToGray(Bitmap bmpSrc){
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        //创建目标灰度图像
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //创建画布
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(cf);
        canvas.drawBitmap(bmpSrc, 0, 0, paint);
        return bitmap;
    }

    /**
     * 对图像进行线性灰度变化
     * @param bmpSrc
     * @return
     */
    public static Bitmap lineGrey(Bitmap bmpSrc){
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        //创建线性拉升灰度图像
        Bitmap lineGrayBmp = null;
        lineGrayBmp = bmpSrc.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环对图像的像素进行处理
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                //得到每点的像素值
                int col = bmpSrc.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                //增加了图像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                //对图像像素越界进行处理
                if(red >= 255){
                    red = 255;
                }
                if(green >= 255){
                    green = 255;
                }
                if(blue >= 255){
                    blue = 255;
                }
                //新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                lineGrayBmp.setPixel(i,j,newColor);
            }
        }
        return lineGrayBmp;
    }

    /**
     * 对图像进行二值化处理
     * @param grayBmp
     * @return
     */
    public static Bitmap grayToBinary(Bitmap grayBmp){
        int width = grayBmp.getWidth();
        int height = grayBmp.getHeight();
        Bitmap bitmap = null;
        bitmap = grayBmp.copy(Bitmap.Config.ARGB_8888, true);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int col = bitmap.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                int gray = (int)((float) red * 0.3 + (float)green * 0.59 + (float) blue * 0.11);
                //对图像进行二值化处理
                if(gray <= 95){
                    gray = 0;
                }else{
                    gray = 255;
                }
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                bitmap.setPixel(i, j, newColor);
            }
        }
        return bitmap;
    }

    /**
     * 将图片变成黑白
     * @param bmpSrc
     * @param w
     * @param h
     * @return 返回黑白图片
     */
    public static Bitmap converToBMW(Bitmap bmpSrc, int w, int h){
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        int[] pixels = new int[width * height];
        // 设定二值化的域值，默认值为100
        int tmp = 100;
        bmpSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int grey = pixels[width * i + j];
                //分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey * 0x000000FF);
                if(red > tmp){
                    red = 255;
                }else{
                    red = 0;
                }
                if(blue > tmp){
                    blue = 255;
                }else{
                    blue = 0;
                }
                if(green > tmp){
                    green = 255;
                }else{
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8 | blue;
                if(pixels[width * i + j] == -1){
                    pixels[width * i + j] = -1;
                }else{
                    pixels[width * i + j] = -16777216;
                }
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        //压缩图片
        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, w, h);
        return resizeBmp;

    }

    /**
     * 将图像变成黑白后返回byte数组类型，方便打印机打印
     * @param bmpSrc
     * @return
     */
    public static byte[] bmpToByte(Bitmap bmpSrc){
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();
        // 通过位图的大小创建像素点数组
        int[] pixels = new int[width * height];
        // 设定二值化的域值，默认值为100
        int tmp = 100;
        bmpSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        int n = 0;
        int buf = 0;
        int k = 0;
        byte[] bmpBuf = new byte[height * width / 8];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int grey = pixels[width * i + j];
                //分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey * 0x000000FF);
                int add = red + green + blue;
                buf = buf << 1;
                if(add > 0){

                }else{
                    buf = buf | 1;
                }
                n++;
                if(n == 8){
                    bmpBuf[k++] = (byte)(buf & 0xFF);
                    if(buf > 0){

                    }
                    buf = 0;
                    n = 0;
                }
            }
        }

        return bmpBuf;
    }


}
