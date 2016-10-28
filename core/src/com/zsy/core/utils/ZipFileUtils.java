package com.zsy.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @description:  文件压缩解压
 * @date: 2015-7-3 上午10:30:24
 * @author: wangqing
 * @version 1.0.0
 */
public class ZipFileUtils {
	

	private static java.io.FileInputStream inputStream;
	 private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFilePath
	 *            要压缩的文件/文件夹名字
	 * @param zipFilePath
	 *            指定压缩的目的和名字
	 * @throws Exception
	 */
	public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
		// 创建Zip包
		java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));

		// 打开要输出的文件
		java.io.File file = new java.io.File(srcFilePath);

		// 压缩
		zipFiles(file.getParent() + java.io.File.separator, file.getName(), outZip);

		// 完成,关闭
		outZip.finish();
		outZip.close();

	}// end of func

	/**
	 * 压缩文件
	 * 
	 * @param folderPath
	 * @param filePath
	 * @param zipOut
	 * @throws Exception
	 */
	private static void zipFiles(String folderPath, String filePath, java.util.zip.ZipOutputStream zipOut) throws Exception {
		if (zipOut == null) {
			return;
		}

		java.io.File file = new java.io.File(folderPath + filePath);

		// 判断是不是文件
		if (file.isFile()) {
			java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath);
			inputStream = new java.io.FileInputStream(file);
			zipOut.putNextEntry(zipEntry);

			int len;
			byte[] buffer = new byte[4096];

			while ((len = inputStream.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}

			zipOut.closeEntry();
		} else {
			// 文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();

			// 如果没有子文件, 则添加进去即可
			if (fileList.length <= 0) {
				java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath + java.io.File.separator);
				zipOut.putNextEntry(zipEntry);
				zipOut.closeEntry();
			}

			// 如果有子文件, 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				zipFiles(folderPath, filePath + java.io.File.separator + fileList[i], zipOut);
			}// end of for

		}// end of if

	}// end of func
	
	/**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile 生成的压缩文件
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.close();
    }

    /**
     * 压缩文件
     *
     * @param resFile 需要压缩的文件（夹）
     * @param zipout 压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException 当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes(), "utf-8");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }
}
