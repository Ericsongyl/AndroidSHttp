package com.zsy.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * 
 * @description: 文件处理工具类
 * @date: 2015-7-3 上午10:31:16
 * @author: wangqing
 * @version 1.0.0
 */
public class FileUtils {

	private static final String tag = FileUtils.class.getSimpleName();

	public static boolean saveFile(String content, String filePath, String fileName) {
		if (!TextUtils.isEmpty(content)) {
			return saveFile(content.getBytes(), filePath, fileName);
		}
		return false;
	}

	/**
	 * 指定文件名称保存文本内容到SD卡
	 * saveFile
	 * 
	 * @param bytes
	 * @param filePath
	 * @param fileName
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean saveFile(byte[] bytes, String filePath, String fileName) {
		FileOutputStream output = null;
		try {
			if (bytes != null) {
				File filesDir = new File(filePath);
				filesDir.mkdirs();
				File file = new File(filePath, fileName);
				file.createNewFile();
				output = new FileOutputStream(file);
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取本地图片
	 * getLoacalBitmap
	 * 
	 * @param url
	 * @return
	 *
	 * Bitmap
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取assets中的图片
	 * getImageFromAssetsFile
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 *
	 * Bitmap
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static Bitmap getImageFromAssetsFile(Context context,String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	
	/**
	 * 判断文件是否存在， true表示存在，false表示
	 * isFileExits
	 * 
	 * @param filePath
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean isFileExits(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 保存图片到本地
	 * saveFile
	 * 
	 * @param bitmap
	 * @param filePath
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean saveFile(Bitmap bitmap, String filePath) {

		if (bitmap == null)
			return false;
		OutputStream output = null;

		try {
			File file = new File(filePath);
			if (file.exists()) {
				output = new FileOutputStream(file);

				CompressFormat format = Bitmap.CompressFormat.PNG;
				String tempFileName = filePath.toLowerCase(Locale.getDefault());
				if (".jpg".endsWith(tempFileName)) {
					format = Bitmap.CompressFormat.JPEG;
				} else if (".png".endsWith(tempFileName)) {
					format = Bitmap.CompressFormat.PNG;
				}

				bitmap.compress(format, 100, output);
				output.flush();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存字符串到本地
	 * saveFile
	 * 
	 * @param content
	 * @param filePath
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean saveFile(String content, String filePath) {
		if (!TextUtils.isEmpty(content)) {
			return saveFile(content.getBytes(), filePath);
		}
		return false;
	}

	/**
	 * 保存字符串到本地
	 * 
	 * @param content
	 *            字符串内容
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static boolean saveFile(byte[] bytes, String filePath) {
		FileOutputStream output = null;
		try {
			if (bytes != null) {
				File file = new File(filePath);
				output = new FileOutputStream(file);
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean saveFile(InputStream instream, String fileName, String filePath) {
		File files = new File(filePath);
		FileOutputStream buffer = null;
		File file = new File(files.getAbsoluteFile(), fileName);
		try {
			file.createNewFile();
			if (instream != null) {
				buffer = new FileOutputStream(file);
				byte[] tmp = new byte[1024];
				int length = 0;
				while ((length = instream.read(tmp)) != -1) {
					buffer.write(tmp, 0, length);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null)
					instream.close();
				if (buffer != null) {
					buffer.flush();
					buffer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存数据流到本地
	 * saveFile
	 * 
	 * @param instream
	 * @param filePath
	 * @return
	 *
	 * boolean
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static boolean saveFile(InputStream instream, String filePath) {

		File file = new File(filePath);
		FileOutputStream buffer = null;

		try {
			if (instream != null) {
				buffer = new FileOutputStream(file);
				byte[] tmp = new byte[1024];
				int length = 0;
				while ((length = instream.read(tmp)) != -1) {
					buffer.write(tmp, 0, length);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null)
					instream.close();
				if (buffer != null) {
					buffer.flush();
					buffer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 读取文件
	 * readFile
	 * 
	 * @param filePath
	 * @return
	 *
	 * String
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static String readFile(String filePath) {

		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 按照指定文本读取文件
	 * readFile
	 * 
	 * @param filePath
	 * @param charset
	 * @return
	 *
	 * String
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static String readFile(String filePath, String charset) {

		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 按照指定文本读取文件 第一行增加换行符 读取车型信息用，第一行和后面的行数区分开
	 * readFileAddNewLine
	 * 
	 * @param filePath
	 * @param charset
	 * @return
	 *
	 * String
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static String readFileAddNewLine(String filePath, String charset) {

		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, charset));
			String line = null;
			String firstLine = "\n";
			while ((line = br.readLine()) != null) {
				sb.append(line + firstLine);
				firstLine = "";
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 读取数据流生成的文本
	 * readFileSdcard
	 * 
	 * @param fileName
	 * @return
	 *
	 * String
	 *
	 * 841306 
	 *
	 * @since  1.0.0
	 */
	public static String readFileSdcard(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 从resource的raw中读取文件数据
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static InputStream getRawStream(Context context, int resId) {
		try {
			return context.getResources().openRawResource(resId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从resource的asset中读取文件字符串
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getAssetsString(Context context, String fileName) {
		try {
			return inputSteamToString(context.getResources().getAssets().open(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从resource的asset中读取文件数据
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static InputStream getAssetsStream(Context context, String fileName) {
		try {
			return context.getResources().getAssets().open(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将byte[]转换成InputStream
	 * 
	 * @param b
	 * @return
	 */
	public static InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	/**
	 * InputStream转换成byte[]
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] InputStream2Bytes(InputStream instream) {
		byte bytes[] = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] tmp = new byte[1024];
			int length = 0;
			while ((length = instream.read(tmp)) != -1) {
				output.write(tmp, 0, length);
			}
			bytes = output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null)
					instream.close();
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;

	}

	/**
	 * 数据流转字符串
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputSteamToString(InputStream instream) {
		String result = null;
		try {
			byte bytes[] = InputStream2Bytes(instream);
			result = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解压文件
	 * 
	 * @param srcFile
	 * @param dest
	 * @param deleteFile
	 * @return
	 */
	public synchronized static String unZip(String srcFile, String dest, boolean deleteFile) {
		String message = "success";

		File file = new File(srcFile);
		if (!file.exists()) {
			message = "File does not exist";
			return message;
		}

		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipFile.entries();
			while (e.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(dest + name);
					f.mkdirs();
				} else {
					File f = new File(dest + zipEntry.getName());
					f.getParentFile().mkdirs();
					f.createNewFile();
					InputStream is = zipFile.getInputStream(zipEntry);
					FileOutputStream fos = new FileOutputStream(f);
					int length = 0;
					byte[] b = new byte[1024];
					while ((length = is.read(b, 0, 1024)) != -1) {
						fos.write(b, 0, length);
					}
					is.close();
					fos.close();
				}
			}

			if (zipFile != null) {
				zipFile.close();
			}

			if (deleteFile) {
				file.deleteOnExit();
			}
		} catch (ZipException e1) {
			message = "fail";
		} catch (IOException e1) {
			message = "fail";
		} catch (Exception e) {
			e.printStackTrace();
			message = "fail";
		}
		return message;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到文件名
	 * 
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public static String getFilePath(String fileName, String... filePath) {
		StringBuilder filebuilder = new StringBuilder();
		if (filePath != null) {
			for (String path : filePath) {
				filebuilder.append(path).append(File.separator);
			}
			File file = new File(filebuilder.toString());
			if (!file.exists()) {
				file.mkdirs();
			}
			filebuilder.append(fileName);
		}
		return filebuilder.toString();
	}

	/**
	 * 从assets中拷贝文件到SD卡
	 * 
	 * @param context
	 * @param fileName
	 * @param filePath
	 */
	public static void assetsDataToSD(Context context, String fileName, String filePath) {
		try {
			InputStream myInput = context.getAssets().open(fileName);
			if (myInput != null) {
				saveFile(myInput, fileName, filePath);
			}
			myInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String convertStreamToString(BufferedReader reader) {

		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 删除文件夹
	 * 
	 * @autor zhangshengda
	 * @data 2014-8-6
	 */
	public static void deleteDirectory(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		File[] childFiles = file.listFiles();
		if (childFiles == null || childFiles.length == 0) {
			file.delete();
			return;
		}

		for (int i = 0; i < childFiles.length; i++) {
			deleteDirectory(childFiles[i]);
		}
		file.delete();
	}

	public static void deleteDirectory(String path) {
		File file = new File(path);
		deleteDirectory(file);
	}

	public static void savaToJson(JSONObject o, String filePath) {
		File file = new File(filePath);
		PrintStream out = null;
		try {
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			out = new PrintStream(new FileOutputStream(file));
			out.print(o.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static JSONObject parseJson(String data) {
		JSONObject o = null;
		try {
			o = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}

	public static String getContent(String filePath) {
		FileInputStream in;
		try {
			in = new FileInputStream(filePath);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();
			byte[] bytes = out.toByteArray();
			String result = new String(bytes, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取sd卡剩余空间大小
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		@SuppressWarnings("deprecation")
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		@SuppressWarnings("deprecation")
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 根据路径，文件名生成文件路径，路径不包含文件分隔开符号，文件名不包含路径
	 * 
	 * @return File
	 */
	public static File CreateFile(String path, String filename) {
		// 判断文件夹是否存在
		// 如果不存在、则创建一个新的文件夹(一次创建多层目录，使用mkdirs()，不能使用mkdir())
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(path + File.separator + filename);
		// 如果目标文件已经存在
		if (file.exists() == false) {
			try {
				file.createNewFile(); // 创建新文件
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}

	/**
	 * 拷贝assets下的指定文件夹内容
	 * 
	 * @param context
	 * @param dirname
	 * @throws IOException
	 */
	public static void copyAssetDirToFiles(Context context, String dirname) throws IOException {
		File dir = new File(context.getFilesDir() + "/" + dirname);
		dir.mkdir();
		AssetManager assetManager = context.getAssets();
		String[] children = assetManager.list(dirname);
		for (String child : children) {
			child = dirname + '/' + child;
			String[] grandChildren = assetManager.list(child);
			if (0 == grandChildren.length)
				copyAssetFileToFiles(context, child);
			else
				copyAssetDirToFiles(context, child);
		}
	}

	public static void copyAssetFileToFiles(Context context, String filename) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = context.getAssets().open(filename);
			File of = new File(context.getFilesDir() + "/" + filename);
			of.createNewFile();
			out = new FileOutputStream(of);
			byte[] by = new byte[1024*4];
			int len = 0;
			while((len=in.read(by, 0, by.length))>0){
				out.write(by,0,len);
				out.flush();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			
		}
		
	}
}
