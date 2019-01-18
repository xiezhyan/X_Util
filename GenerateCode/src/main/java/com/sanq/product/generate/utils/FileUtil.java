package com.sanq.product.generate.utils;

import com.sanq.product.config.utils.web.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

	private FileUtil() {}
	
	private static FileUtil instance = null;
	
	public static FileUtil getInstance() {
		if(instance == null) {
			synchronized (FileUtil.class) {
				if(instance == null)
					instance = new FileUtil();
			}
		}
		
		return instance;
	}
	
	/**
	 * 
	 *	version:对文件重新命名
	 *	@param fileName
	 *	@return
	 *----------------------
	 * 	author:xiezhyan
	 *	date:2017-5-31
	 */
	public static String renameFileName(String fileName) {
		StringBuffer sb = new StringBuffer(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
		if(fileName.indexOf(".") != -1) {
			sb.append(fileName.substring(fileName.lastIndexOf(".")));
		}
		return sb.toString();
	}
	
	public static void uploadFile() {
		
	}
	
	private static final int BUFFER = 8192;    
    
    /** 
     * 执行压缩操作 
     * @param srcPathName 被压缩的文件/文件夹 
     * @param pathName 压缩文件路径和名称
     */  
    public void compressExe(String srcPathName , String pathName) {    
        File file = new File(srcPathName);    
        if (!file.exists()){  
            throw new RuntimeException(srcPathName + "不存在！");    
        }  
        try {
        	File zipFile = new File(pathName);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);    
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            ZipOutputStream out = new ZipOutputStream(cos);    
            String basedir = "";    
            compressByType(file, out, basedir);    
            out.close();    
        } catch (Exception e) {   
            e.printStackTrace();  
            LogUtil.getInstance(FileUtil.class).i("执行压缩操作时发生异常:" + e);
            throw new RuntimeException(e);    
        }
    }    
    
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    private void compressByType(File file, ZipOutputStream out, String basedir) {    
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {    
        	LogUtil.getInstance(FileUtil.class).i("压缩：" + basedir + file.getName());    
            this.compressDirectory(file, out, basedir);    
        } else {    
        	LogUtil.getInstance(FileUtil.class).i("压缩：" + basedir + file.getName());    
            this.compressFile(file, out, basedir);    
        }    
    }    
    
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {    
        if (!dir.exists()){  
             return;    
        }  
             
        File[] files = dir.listFiles();    
        for (int i = 0; i < files.length; i++) {    
            /* 递归 */    
            compressByType(files[i], out, basedir + dir.getName() + "/");    
        }    
    }    
    
    /** 
     * 压缩一个文件 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    private void compressFile(File file, ZipOutputStream out, String basedir) {    
        if (!file.exists()) {    
            return;    
        }    
        try {    
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            ZipEntry entry = new ZipEntry(basedir + file.getName());    
            out.putNextEntry(entry);    
            int count;    
            byte data[] = new byte[BUFFER];    
            while ((count = bis.read(data, 0, BUFFER)) != -1) {    
                out.write(data, 0, count);    
            }    
            bis.close();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }
}
