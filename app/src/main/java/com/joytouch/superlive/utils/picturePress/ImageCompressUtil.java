package com.joytouch.superlive.utils.picturePress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片压缩工具类
 */
public class ImageCompressUtil {

    private static final String TAG = "ImageCompressUtil";

    private static Quality defauQuality = Quality.QUALITY_80;

    public enum Quality {
        QUALITY_ORIGINAL, QUALITY_90, QUALITY_80, QUALITY_70, QUALITY_60,QUALITY_50, QUALITY_30
    }

    ;

    /**
     * 通过压缩图片的尺寸来压缩图片大小，通过读入流的方式，可以有效防止网络图片数据流形成位图对象时内存过大的问题；
     *
     * @param \InputStream 要压缩图片，以流的形式传入
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     * @throws IOException 读输入流的时候发生异常
     */
    public static Bitmap compressBySize(InputStream is, int targetWidth, int targetHeight) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }

        byte[] data = baos.toByteArray();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 && widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内存；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        return bitmap;
    }

    /**
     * 压缩图片，自动计算要压缩的宽和高，默认压缩质量为原图的60%
     *
     * @param fromPath 原始图片路径
     * @param toPath   压缩后的图片的保存路径
     * @return
     */
    public static File compressImage(String fromPath, String toPath) {
        try {
            FileInputStream is = new FileInputStream(fromPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            byte[] data = baos.toByteArray();

            int degree = BitmapUtils.readPictureDegree(fromPath);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
            Bitmap bitmap = BitmapFactory.decodeFile(fromPath, opts);

            // Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
            // data.length,
            // opts);

            // int bitmapWidth = bitmap.getWidth();
            // int bitmapHeight = bitmap.getHeight();

            int bitmapWidth = opts.outWidth;
            int bitmapHeight = opts.outHeight;

            int[] params = ImageCompressUtil.calculateWidhtHeight(bitmapWidth, bitmapHeight);
            int width = params[0];
            int height = params[1];
            opts.inJustDecodeBounds = false;

            opts.inDither = false;
            opts.inPurgeable = true;
            opts.inTempStorage = new byte[12 * 1024];

            // bitmap = BitmapFactory.decodeFile(fromPath,opts);

            // File file2 = new File(fromPath);
            // bitmap = BitmapFactory.decodeStream(fis, null_, opts);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);

            File file = scaleImage(toPath, width, height, defauQuality, bitmap, bitmapWidth, bitmapHeight, degree);

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩图片，自动计算要压缩的宽和高
     *
     * @param fromPath 原始图片路径
     * @param toPath   压缩后的图片路径
     * @param quality  压缩质量
     * @return
     */
    public static File compressImage(String fromPath, String toPath, Quality quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromPath);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int[] params = ImageCompressUtil.calculateWidhtHeight(bitmapWidth, bitmapHeight);
            int width = params[0];
            int height = params[1];
            int degree = BitmapUtils.readPictureDegree(fromPath);
            File file = scaleImage(toPath, width, height, quality, bitmap, bitmapWidth, bitmapHeight, degree);

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按照指定的分辨率和质量压缩图片到指定目录
     *
     * @param fromPath
     * @param toPath
     * @param width
     * @param height
     * @param quality
     */
    public static File compressImage(String fromPath, String toPath, int width, int height, Quality quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromPath);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int degree = BitmapUtils.readPictureDegree(fromPath);
            File file = scaleImage(toPath, width, height, quality, bitmap, bitmapWidth, bitmapHeight, degree);

            return file;
        } catch (Exception e) {
            e.printStackTrace();
//            MLog.v(TAG, "compressImage : " + e);
            return null;
        }
    }

    private static File scaleImage(String toPath, int width, int height, Quality quality, Bitmap bitmap, int bitmapWidth, int bitmapHeight, int degree) throws FileNotFoundException, IOException, NullPointerException {
        // 缩放图片的尺寸
        float scaleWidth = (float) width / bitmapWidth;
        float scaleHeight = (float) height / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

        if (degree > 0) {
            resizeBitmap = BitmapUtils.rotaingImageView(degree, resizeBitmap);
        }

        // 保存图片到指定的目录
        File myCaptureFile = new File(toPath);
        FileOutputStream out = new FileOutputStream(myCaptureFile);
        int qualityValue = getImageQuality(quality);

        if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, qualityValue, out)) {
            out.flush();
            out.close();
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();// 释放资源，否则容易内存溢出 fuck
        }
        if (!resizeBitmap.isRecycled()) {
            resizeBitmap.recycle();
        }

        return myCaptureFile;
    }

    private static int getImageQuality(Quality quality) {
        int qualityValue = 100;
        switch (quality) {
            case QUALITY_ORIGINAL:
                qualityValue = 100;
                break;
            case QUALITY_90:
                qualityValue = 90;
                break;
            case QUALITY_80:
                qualityValue = 80;
                break;
            case QUALITY_70:
                qualityValue = 80;
                break;
            case QUALITY_60:
                qualityValue = 80;
                break;
            default:
                break;
        }
        return qualityValue;
    }

    private static final int DEFAULT_IMAGE_MAX_HEIGHT = 800;
    private static final int DEFAULT_IMAGE_MAX_WIDTH = 480;

    /**
     * 计算出合适的图片分辨率
     *
     * @param width
     * @param height
     * @return
     */
    public static int[] calculateWidhtHeight(int width, int height) {
        int[] params = new int[2];
        // 得到图片比例
        double percent = (((double) width) / height);
        int mWidth = 0;
        int mHeight = 0;
        if (width > height) {
//            MLog.v(TAG, "宽图");
            int ratio = width / height;
            if (ratio > 1.5) {
                // 超级大宽图
                if (height >= DEFAULT_IMAGE_MAX_HEIGHT) {
                    // 计算应该缩放的宽
                    mWidth = (int) (DEFAULT_IMAGE_MAX_HEIGHT * percent);
                    mHeight = DEFAULT_IMAGE_MAX_HEIGHT;
                } else {
                    mWidth = width;
                    mHeight = height;
                }
            } else {
                // 小宽图
                if (width >= DEFAULT_IMAGE_MAX_WIDTH) {
                    mWidth = DEFAULT_IMAGE_MAX_WIDTH;
                    mHeight = (int) (DEFAULT_IMAGE_MAX_WIDTH / percent);
                } else {
                    mWidth = width;
                    mHeight = height;
                }
            }
        } else if (height > width) {
//            MLog.v(TAG, "长图");
            if (width >= DEFAULT_IMAGE_MAX_WIDTH) {
                mWidth = DEFAULT_IMAGE_MAX_WIDTH;
                // 计算应该缩放的高
                mHeight = (int) (DEFAULT_IMAGE_MAX_WIDTH / percent);
                params[0] = mWidth;
                params[1] = mHeight;
            } else {
                mWidth = width;
                mHeight = height;
            }
        } else {// 宽高一致

//            MLog.v(TAG, "正方形图");

            if (width >= DEFAULT_IMAGE_MAX_WIDTH) {
                mWidth = DEFAULT_IMAGE_MAX_WIDTH;
                mHeight = DEFAULT_IMAGE_MAX_WIDTH;
            } else {
                mWidth = width;
                mHeight = height;
            }
        }
        params[0] = mWidth;
        params[1] = mHeight;
        return params;
    }

    /**
     * 图片的质量压缩
     * @param image
     * @return
     */
    public static Bitmap compressImage_1(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {
//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -=10;//每次都减少10
            Log.e("执行质量压缩",options+""+"/"+baos.toByteArray().length);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
//-------------------------------------------------------------------------------------------
    /**
     * 按比例压缩方法，先图片大小再图片质量压缩（根据路径获取图片并压缩）
     * @param srcPath
     * @return
     */
    private Bitmap getimage_1(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage_1(bitmap);//压缩好比例大小后再进行质量压缩
    }



    /**
     * 获得bitmap大小
     *
     * @param bitmap
     * @return 2015-9-21
     * @author 李天富
     */
    public static int getBitmapSize(Bitmap bitmap) {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //API 19
        // return bitmap.getAllocationByteCount();
        // }
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API
        // 12
        // return bitmap.getByteCount();
        // }
        return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
    }

    public static File compressImageToTargetSize(File inputFile, int limitKB) {
        String inputFilePath = inputFile.getAbsolutePath();
        String outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf(".")) + "_comp" + inputFilePath.substring(inputFilePath.lastIndexOf("."));

        int degree = BitmapUtils.readPictureDegree(inputFilePath);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inputFilePath, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 读取图片失败时直接返回
        if (picWidth == 0 || picHeight == 0) {
//            return inputFile;
            // 初始压缩比例
            opt.inSampleSize = 2;
        } else {
            while (opt.inSampleSize * opt.outHeight > 4000 || opt.inSampleSize * opt.outWidth > 800) {
                opt.inSampleSize = BitmapUtils.calculateInSampleSize(opt, 800, 4000);
            }
        }
        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(inputFilePath, opt);
        bitmap = BitmapUtils.compressJpgBmp(bitmap, limitKB);
//        Bitmap bitmap = compressJpgBmp(inputFilePath, limitKB);
        if (bitmap == null) {
            bitmap = BitmapUtils.compressJpgBmp(bitmap, limitKB);
        }
        if (degree > 0) {
            bitmap = BitmapUtils.rotaingImageView(degree, bitmap);
        }

        File file = new File(outputFilePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            bitmap.recycle();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return inputFile;
        }
        return file;
    }

    public static Bitmap compressJpgBmp(String filepath, int limitKB) {
        try {
            Bitmap bmp = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > limitKB) {
                baos.reset();
                if (options > 10) {
                    options -= 10;
                } else {
                    options -= 2;
                }
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 按指定大小压缩图片
     *
     * @param fromPath 原始图片路径
     * @param toPath   压缩后图片路径
     * @param limitKB  限制大小，单位kb 2015-9-21
     * @author 李天富
     */
    public static void compressImageToTargetSize(String fromPath, String toPath, int limitKB) {
        int degree = BitmapUtils.readPictureDegree(fromPath);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fromPath, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 读取图片失败时直接返回
        if (picWidth == 0 || picHeight == 0) {
            return;
        }
        int screenW = DisplayUtils.getWidthPixels();
        int screenH = DisplayUtils.getHeightPixels();
        // 初始压缩比例
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > DisplayUtils.getWidthPixels())
                opt.inSampleSize *= picWidth / screenW;
        } else {
            if (picHeight > screenH)
                opt.inSampleSize *= picHeight / screenH;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(fromPath, opt);
        if (degree == 90) {
            bmp = BitmapUtils.rotaingImageView(90, bmp);
        }

        File file = new File(toPath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            bmp.recycle();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // BitmapFactory.Options opt = new Options();
        // opt.inJustDecodeBounds = false;
        // opt.inSampleSize = 2;
        // Bitmap fromBitmap = BitmapFactory.decodeFile(fromPath, opt);
        //
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // int options = 100;
        // fromBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // while (baos.toByteArray().length / 1024 > limitKB && options > 10) {
        // baos.reset();
        // options -= 10;
        // fromBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // }
        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());//
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null_, null_);//
        // 把ByteArrayInputStream数据生成图片
        // File file = new File(toPath);
        // if (file.exists()) {
        // file.delete();
        // }
        // try {
        // FileOutputStream out = new FileOutputStream(file);
        // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        // bitmap.recycle();
        // out.flush();
        // out.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

    }

}
