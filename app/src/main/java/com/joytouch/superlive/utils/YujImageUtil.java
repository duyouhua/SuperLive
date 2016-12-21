package com.joytouch.superlive.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class YujImageUtil {
	/**
	 * 单张图片的按比例缩放 图片不会超出所设置的最大宽和高
	 * 
	 * @param path
	 * @param bitmap
	 * @param maxW
	 *            图片允许的最大宽度
	 * @param maxH
	 *            图片允许的最大高度
	 * @return
	 */
	public static Bitmap yujCropBitmap(String path, Bitmap bitmap, int maxW,
			int maxH) {
		boolean debug = false;
		if (debug) {
			Log.i("yujCropBitmap-max--", maxW + "--" + maxH);
		}
		if (path != null && bitmap != null) {
			return null;
		}
		if (path == null && bitmap == null) {
			return null;
		}
		if (maxW == 0 || maxH == 0) {
			return null;
		}
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (path != null) {
			bm = BitmapFactory.decodeFile(path, options);
		}
		options.inJustDecodeBounds = false;
		int width = options.outWidth;
		int height = options.outHeight;
		if (debug) {
			Log.i("yujCropBitmap-原图尺寸", width + "--" + height);
		}
		if (width <= maxW && height <= maxH) {
			if (debug) {
				Log.i("yujCropBitmap-result--", width + "--" + height);
			}
			return BitmapFactory.decodeFile(path);
		} else if (width > maxW || height > maxH) {
			// 图片过大进行缩放
			int beW = 0;
			int beH = 0;
			int be = 1;
			if (maxH != 0 && maxW != 0) {
				beW = width / maxW;
				beH = height / maxH;
			}
			be = Math.max(beH, beW);
			if (be < 1) {
				be = 1;
			}
			options.inSampleSize = be;
			bm = BitmapFactory.decodeFile(path, options);
			// 重新测量长度，修改绝对的长和宽
			int scaleW = bm.getWidth();
			int scaleH = bm.getHeight();
			if (debug) {
				Log.i("yujCropBitmap-options-scale_after", "be-" + be + "--"
						+ scaleW + "--" + scaleH);
			}
			Matrix matrix = new Matrix();
			if (scaleH <= maxH && scaleW <= maxW) {
				return bm;
			}
			if (scaleH > maxH || scaleW > maxW) {
				float x = (float) maxW / (float) scaleW;
				float y = (float) maxH / (float) scaleH;
				if (x < y) {
					matrix.postScale(x, x);
				} else {
					matrix.postScale(y, y);
				}
				bm = Bitmap
						.createBitmap(bm, 0, 0, scaleW, scaleH, matrix, true);
				if (debug) {
					Log.i("yujCropBitmap-result--",
							bm.getWidth() + "--" + bm.getHeight());
				}
				return bm;
			}

		}
		return null;
	}

	public static Bitmap yujCropBitmap(String path, Bitmap bitmap, int minW,
			int minH, int maxW, int maxH) {
		if (path != null && bitmap != null) {
			return null;
		}
		if (path == null && bitmap == null) {
			return null;
		}
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (path != null) {
			bm = BitmapFactory.decodeFile(path, options);
		}
		options.inJustDecodeBounds = false;
		int width = options.outWidth;
		int height = options.outHeight;
		if (width >= minW && width <= maxW && height >= minH && height <= maxH) {
			return BitmapFactory.decodeFile(path);
		} else if (width > maxW || height > maxH) {
			// 图片过大进行压缩
			int beW = 0;
			int beH = 0;
			int be = 1;
			if (maxH != 0 && maxW != 0) {
				beW = width / maxW;
				beH = height / maxH;
			}
			be = Math.max(beH, beW);
			if (be < 1) {
				be = 1;
			}
			options.inSampleSize = be;
			bm = BitmapFactory.decodeFile(path, options);
			// 重新测量长度，修改绝对的长和宽
			int scaleW = bm.getWidth();
			int scaleH = bm.getHeight();
			Matrix matrix = new Matrix();
			if (scaleH <= maxH && scaleW <= maxW) {
				return bm;
			}
			if (scaleH > maxH && scaleW < maxW) {

				float y = (float) maxH / (float) scaleH;
				matrix.postScale(y, y);
				// return Bitmap.createBitmap(bm, 0, 0, scaleW, maxH);

			}
			if (scaleW > maxW && scaleH < maxH) {
				float x = (float) maxW / (float) scaleW;
				matrix.postScale(x, x);
				// return Bitmap.createBitmap(bm, 0, 0, maxW, scaleH);
			}
			if (scaleH > maxH && scaleW > maxW) {
				float x = (float) maxW / (float) scaleW;
				float y = (float) maxH / (float) scaleH;

				float min = Math.min(x, y);
				matrix.postScale(min, min);
			}
			return Bitmap.createBitmap(bm, 0, 0, scaleW, scaleH, matrix, true);

		} else if (width <= minW || height <= minH) {
			// 图片过小 进行放大
			bm = BitmapFactory.decodeFile(path);
			Matrix matrix = new Matrix();
			if (width < minW && height > minH) {
				float x = (float) minW / (float) width;
				if (x * height > maxH) {
					x = (float) maxH / (float) height;
				}
				matrix.postScale(x, x);
			}
			if (height < minH && width > minW) {
				float y = (float) minH / (float) height;
				if (y * width > maxW) {
					y = (float) maxW / (float) width;
				}
				matrix.postScale(y, y);
			}
			if (width <= minW && height <= minH) {
				float x = (float) minW / (float) width;
				float y = (float) minH / (float) height;
				if (x > y) {
					matrix.postScale(x, x);
				} else {
					matrix.postScale(y, y);
				}
			}
			return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		}
		return null;
	}

	/**
	 * 得到图片的空间大小
	 */
	@SuppressLint("NewApi")
	public static long getBitmapsize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}

	/**
	 * 压缩图片 图片不会小于设置的宽和高
	 * 
	 * @param path
	 * @param bm
	 * @param width
	 *            按设置的宽和高放大和缩小图片
	 * @param height
	 * @return
	 */
	public static Bitmap yujThumbnailBmByOptions(String path, Bitmap bm,
			int width, int height) {
		if (path != null && bm != null) {
			return null;
		}
		Bitmap bitmap = null;
		ByteArrayOutputStream bos = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (path != null) {
			bitmap = BitmapFactory.decodeFile(path, options);
		} else {
			bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.PNG, 80, bos);
			bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0,
					bos.toByteArray().length, options);

		}
		options.inJustDecodeBounds = false;
		int getWidth = options.outWidth;
		int getHeight = options.outHeight;
		if (getWidth == 0 || getHeight == 0) {
			return null;
		}
		float be = 1.0f;
		if (getWidth > width || getHeight > height) {
			if (width == 0) {
				be = getHeight / height;
			} else if (height == 0) {
				be = getWidth / width;
			} else {
				be = Math.min(getHeight / height, getWidth / width);
			}
			options.inSampleSize = (int) be;
			if (bm != null) {
				bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0,
						bos.toByteArray().length, options);
			} else if (path != null) {
				bitmap = BitmapFactory.decodeFile(path, options);
			}

		} else {
			Matrix matrix = new Matrix();
			bitmap = BitmapFactory.decodeFile(path);
			if (getWidth < width && getHeight > height || height == 0) {
				be = (float) width / (float) getWidth;
				matrix.postScale(be, be);
			}
			if (getWidth > width && getHeight < height || width == 0) {
				be = (float) height / (float) getHeight;
				matrix.postScale(be, be);
			}
			if (getWidth <= width && getHeight <= height) {
				float beW = (float) width / (float) getWidth;
				float beH = (float) height / (float) getHeight;
				matrix.postScale(beW, beH);
			}

			bitmap = Bitmap.createBitmap(bitmap, 0, 0, getWidth, getHeight,
					matrix, true);

		}
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return bitmap;
	}

	/**
	 * 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
	 * 
	 * @return
	 */
	public static int getMaxMemorySize() {
		return (int) Runtime.getRuntime().maxMemory();
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */

	public static Bitmap getBitmapThumbail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = 10000;
		if (height != 0) {
			beHeight = h / height;
		}
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		if (height == 0) {
			return bitmap;
			// = ThumbnailUtils.extractThumbnail(bitmap, width, h/be,
			// ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		} else {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */

	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 
	 * 将Bitmap转化为字节数组
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @return
	 */

	public static byte[] bitmap2byte(Bitmap bitmap) {

		ByteArrayOutputStream baos = null;

		try {

			baos = new ByteArrayOutputStream();

			bitmap.compress(CompressFormat.PNG, 100, baos);

			byte[] array = baos.toByteArray();

			baos.flush();

			baos.close();

			return array;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 将byte数组转化为bitmap
	 * 
	 * 
	 * 
	 * @param data
	 * 
	 * @return
	 */

	public static Bitmap byte2bitmap(byte[] data) {

		if (null == data) {

			return null;

		}

		return BitmapFactory.decodeByteArray(data, 0, data.length);

	}

	/**
	 * 
	 * 将Drawable转化为Bitmap
	 * 
	 * 
	 * 
	 * @param drawable
	 * 
	 * @return
	 */

	public static Bitmap drawable2bitmap(Drawable drawable) {

		if (null == drawable) {

			return null;

		}

		int width = drawable.getIntrinsicWidth();

		int height = drawable.getIntrinsicHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable

		.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888

		: Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, width, height);

		drawable.draw(canvas);// 重点

		return bitmap;

	}

	/**
	 * 
	 * 将bitmap转化为drawable
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @return
	 */

	public static Drawable bitmap2Drawable(Bitmap bitmap) {

		if (bitmap == null) {

			return null;

		}

		return new BitmapDrawable(bitmap);

	}

	/**
	 * 
	 * 按指定宽度和高度缩放图片,不保证宽高比例
	 * 
	 * 
	 * 
	 * @param bitmap
	 * 
	 * @param w
	 * 
	 * @param h
	 * 
	 * @return
	 */

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {

		if (bitmap == null) {

			return null;

		}

		int width = bitmap.getWidth();

		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();

		float scaleWidht = ((float) w / width);

		float scaleHeight = ((float) h / height);

		matrix.postScale(scaleWidht, scaleHeight);

		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,

		matrix, true);

		return newbmp;

	}

	/**
	 * 
	 * 将bitmap位图保存到path路径下，图片格式为Bitmap.CompressFormat.PNG，质量为100
	 * 
	 * @param bitmap
	 * 
	 * @param path
	 */

	public static boolean saveBitmap(Bitmap bitmap, String path) {

		try {

			File file = new File(path);

			File parent = file.getParentFile();

			if (!parent.exists()) {

				parent.mkdirs();

			}

			FileOutputStream fos = new FileOutputStream(file);

			boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);

			fos.flush();

			fos.close();

			return b;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}

	/**
	 * 
	 * 将bitmap位图保存到path路径下
	 * 
	 * @param bitmap
	 * @param path
	 *            保存路径-Bitmap.CompressFormat.PNG或Bitmap.CompressFormat.JPEG.PNG
	 * @param format
	 *            格式
	 * @param quality
	 *            质量
	 * @return
	 */

	public static boolean saveBitmap(Bitmap bitmap, String path,

	CompressFormat format, int quality) {

		try {

			File file = new File(path);

			File parent = file.getParentFile();

			if (!parent.exists()) {

				parent.mkdirs();

			}

			FileOutputStream fos = new FileOutputStream(file);

			boolean b = bitmap.compress(format, quality, fos);

			fos.flush();

			fos.close();

			return b;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}

	/**
	 * 
	 * 获得带倒影的图片
	 */

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {

		if (bitmap == null) {

			return null;

		}

		final int reflectionGap = 4;

		int width = bitmap.getWidth();

		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();

		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,

		width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,

		(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(bitmap, 0, 0, null);

		Paint deafalutPaint = new Paint();

		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();

		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,

		bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,

		0x00ffffff, TileMode.CLAMP);

		paint.setShader(shader);

		// Set the Transfer mode to be porter duff and destination in

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		// Draw a rectangle using the paint with our linear gradient

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()

		+ reflectionGap, paint);

		return bitmapWithReflection;

	}

	/**
	 * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @throws IOException
	 */
	public static void saveImage(Context context, String fileName, Bitmap bitmap)
			throws IOException {
		saveImage(context, fileName, bitmap, 100);
	}

	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, int quality) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(Context ctx, String filePath,
			Bitmap bitmap, int quality) throws IOException {
		if (bitmap != null) {
			File file = new File(filePath.substring(0,
					filePath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			bitmap.compress(CompressFormat.JPEG, quality, bos);
			bos.flush();
			bos.close();
			if (ctx != null) {
				scanPhoto(ctx, filePath);
			}
		}
	}

	/**
	 * 让Gallery上能马上看到该图片
	 */
	private static void scanPhoto(Context ctx, String imgFileName) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(imgFileName);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		ctx.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 使用当前时间戳拼接一个唯一的文件名
	 * 
	 * @param format
	 * @return
	 */
	public static String getTempFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new java.sql.Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	/**
	 * 获取照相机使用的目录
	 * 
	 * @return
	 */
	public static String getCamerPath() {
		return Environment.getExternalStorageDirectory() + File.separator
				+ "DCIM" + File.separator + "Camera" + File.separator;
	}

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 * 
	 * @param uri
	 * @return
	 */

	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + "/sdcard" + File.separator;
		String pre2 = "file://" + "/mnt/sdcard" + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";

		String[] proj = { MediaColumns.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 计算缩放图片的宽高
	 * 
	 * @param img_size
	 * @param square_size
	 * @return
	 */
	public static int[] scaleImageSize(int[] img_size, int square_size) {
		if (img_size[0] <= square_size && img_size[1] <= square_size)
			return img_size;
		double ratio = square_size
				/ (double) Math.max(img_size[0], img_size[1]);
		return new int[] { (int) (img_size[0] * ratio),
				(int) (img_size[1] * ratio) };
	}

	/**
	 * 创建缩略图
	 * 
	 * @param context
	 * @param largeImagePath
	 *            原始大图路径
	 * @param thumbfilePath
	 *            输出缩略图路径
	 * @param square_size
	 *            输出图片宽度
	 * @param quality
	 *            输出图片质量
	 * @throws IOException
	 */
	public static void createImageThumbnail(Context context,
			String largeImagePath, String thumbfilePath, int square_size,
			int quality) throws IOException {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		// 原始图片bitmap
		Bitmap cur_bitmap = BitmapFactory.decodeFile(largeImagePath, opts);

		if (cur_bitmap == null)
			return;

		// 原始图片的高宽
		int[] cur_img_size = new int[] { cur_bitmap.getWidth(),
				cur_bitmap.getHeight() };
		// 计算原始图片缩放后的宽高
		int[] new_img_size = scaleImageSize(cur_img_size, square_size);
		// 生成缩放后的bitmap
		Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
				new_img_size[1]);
		// 生成缩放后的图片文件
		saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
	}

	public static Bitmap scaleBitmap(Bitmap bitmap) {
		// 获取这个图片的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 定义预转换成的图片的宽度和高度
		int newWidth = 200;
		int newHeight = 200;
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		// 旋转图片 动作
		// matrix.postRotate(45);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	/**
	 * (缩放)重绘图片
	 * 
	 * @param context
	 *            Activity
	 * @param bitmap
	 * @return
	 */
	public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int rHeight = dm.heightPixels;
		int rWidth = dm.widthPixels;
		// float rHeight=dm.heightPixels/dm.density+0.5f;
		// float rWidth=dm.widthPixels/dm.density+0.5f;
		// int height=bitmap.getScaledHeight(dm);
		// int width = bitmap.getScaledWidth(dm);
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		float zoomScale;
		/** 方式1 **/
		// if(rWidth/rHeight>width/height){//以高为准
		// zoomScale=((float) rHeight) / height;
		// }else{
		// //if(rWidth/rHeight<width if(width*1.5="" **="" 方式2="" }="" width;=""
		// rwidth)="" zoomscale="((float)" 以宽为准="" height)="">= height) {//以宽为准
		// if(width >= rWidth)
		// zoomScale = ((float) rWidth) / width;
		// else
		// zoomScale = 1.0f;
		// }else {//以高为准
		// if(height >= rHeight)
		// zoomScale = ((float) rHeight) / height;
		// else
		// zoomScale = 1.0f;
		// }
		/** 方式3 **/
		if (width >= rWidth)
			zoomScale = ((float) rWidth) / width;
		else
			zoomScale = 1.0f;
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(zoomScale, zoomScale);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 获取图片类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getImageType(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String type = getImageType(in);
			return type;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 获取图片的类型信息
	 * 
	 * @param in
	 * @return
	 * @see #getImageType(byte[])
	 */
	public static String getImageType(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);
			return getImageType(bytes);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获取图片的类型信息
	 * 
	 * @param bytes
	 *            2~8 byte at beginning of the image file
	 * @return image mimetype or null_ if the file is not image
	 */
	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return "image/jpeg";
		}
		if (isGIF(bytes)) {
			return "image/gif";
		}
		if (isPNG(bytes)) {
			return "image/png";
		}
		if (isBMP(bytes)) {
			return "application/x-bmp";
		}
		return null;
	}

	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
				&& (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
				&& b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
				&& b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 0x42) && (b[1] == 0x4d);
	}

	/**
	 * 获取bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 获取bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath,
			BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 获取bitmap
	 * 
	 * @param file
	 * @return
	 */
	public static Bitmap getBitmapByFile(File file) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		/* 加边框 */
		canvas = new Canvas(output);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5f);
		paint.setColor(0xffffffff);
		canvas.drawCircle(roundPx, roundPx, roundPx, paint);
		canvas.drawBitmap(output, 0, 0, paint);

		return output;
	}

	/**
	 * 获得圆角图片的方法
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            一般设成14
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 快速模糊
	 * 
	 * @param sentBitmap
	 * @param radius
	 *            模糊半径
	 * @param canReuseInBitmap
	 *            是否重用bitmap
	 * @return
	 */
	public static Bitmap fastBlurBitmap(Bitmap sentBitmap, int radius,
			boolean canReuseInBitmap) {

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap;
		if (canReuseInBitmap) {
			bitmap = sentBitmap;
		} else {
			bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		}

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		/* 加遮罩 */
		Canvas canvas = new Canvas(bitmap); // 要加边框的图片
		Paint paint = new Paint();
		paint.setStyle(Style.FILL); // 遮罩风格，透明；fill为填充
		paint.setColor(0x66000000); // 边框颜色
		canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);// 长方形边框
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return (bitmap);
	}
}
