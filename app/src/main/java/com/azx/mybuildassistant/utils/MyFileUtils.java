package com.azx.mybuildassistant.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MyFileUtils {

    private static final String TAG = MyFileUtils.class.getSimpleName();

    public static File checkFileExist(String filePath) {
        if (MyTextUtils.isEmpty(filePath)) {
            MyLog.e(TAG, "checkFileExist -> 源文件路径是空的！");
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            MyLog.e(TAG, "checkFileExist -> 源文件不存在！");
            return null;
        }

        if (!file.isFile()) {
            MyLog.e(TAG, "checkFileExist -> 源文件的类型不是文件类型！");
            return null;
        }
        return file;
    }

    public static File checkFileDirExist(String filePath) {
        if (MyTextUtils.isEmpty(filePath)) {
            MyLog.e(TAG, "checkFileDirExist -> 源文件夹路径是空的！");
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            MyLog.e(TAG, "checkFileExist -> 源文件夹不存在！");
            return null;
        }

        if (!file.isDirectory()) {
            MyLog.e(TAG, "checkFileExist -> 源文件夹的类型不是文件夹类型！");
            return null;
        }
        return file;
    }

    public static boolean modifyFileContent(String filePath, FileContentListener fileContentListener) {
        File file = checkFileExist(filePath);
        if (file == null) {
            MyLog.e(TAG, "modifyFileContent -> 检测参数失败！");
            return false;
        }

        BufferedReader reader = null;
        BufferedWriter writer = null;
        String[] split = file.getName().split("\\.");
        String tempFileName = System.currentTimeMillis() + "." + split[1];
        File tempSaveFile = new File(file.getParent(), tempFileName);
        try {
            reader = new BufferedReader(new FileReader(file));
            writer = new BufferedWriter(new FileWriter(tempSaveFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String newLine = fileContentListener.lineTextContent(line);
                writer.write(newLine);
                writer.newLine();
                writer.flush();
            }
            file.delete();
            tempSaveFile.renameTo(file);
        } catch (Exception e) {
            MyLog.e(TAG, "modifyFileContent -> 读取文件流的过程中出现异常！");
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static boolean replaceContent(String filePath, String replaceContent, String searchTag) {
        return modifyFileContent(filePath, line -> {
            if (line.contains(searchTag)) {
                return replaceContent;
            }
            return line;
        });
    }

    public static String getStrFromVariable(String filePath, String variableName) {
        File file = checkFileExist(filePath);
        if (file == null) {
            return null;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(variableName)) {
                    String trim = line.split("=")[1].trim();
                    return trim.replaceAll(";", "").replace("\"", "");
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean moveFileDir(String srcFilePath, String desFilePath) {
        if (!copyOrMoveFileDirCheck(srcFilePath, desFilePath)) {
            MyLog.e(TAG, "moveFileDir -> 检测参数失败！");
            return false;
        }

        try {
            Files.move(Paths.get(srcFilePath), Paths.get(desFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MyLog.e(TAG, "moveFileDir -> 移动文件夹执行失败！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFileDir(String srcFilePath, String desFilePath) {
        if (!copyOrMoveFileDirCheck(srcFilePath, desFilePath)) {
            MyLog.e(TAG, "copyFileDir -> 检测参数失败！");
            return false;
        }

        try {
            Files.copy(Paths.get(srcFilePath), Paths.get(desFilePath), StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            MyLog.e(TAG, "copyFileDir -> 复制文件夹执行失败！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean moveFile(String srcFilePath, String desFilePath) {
        if (!copyOrMoveFileCheck(srcFilePath, desFilePath)) {
            MyLog.e(TAG, "moveFile -> 检测参数失败！");
            return false;
        }

        try {
            Files.move(Paths.get(srcFilePath), Paths.get(desFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MyLog.e(TAG, "moveFile -> 移动文件执行失败！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFile(String srcFilePath, String desFilePath) {
        if (!copyOrMoveFileCheck(srcFilePath, desFilePath)) {
            MyLog.e(TAG, "copyFile -> 检测参数失败！");
            return false;
        }

        try {
            Files.copy(Paths.get(srcFilePath), Paths.get(desFilePath), StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            MyLog.e(TAG, "copyFile -> 复制文件执行失败！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean copyOrMoveFileCheck(String srcFilePath, String desFilePath) {
        File srcFile = checkFileExist(srcFilePath);
        if (srcFile == null) {
            MyLog.e(TAG, "copyOrMoveFileCheck -> 检测源文件不存在，拷贝或移动文件执行失败！");
            return false;
        }

        if (!srcFile.isFile()) {
            MyLog.e(TAG, "copyOrMoveFileCheck -> 源文件的类型不对，不是文件类型，拷贝或移动文件执行失败！");
            return false;
        }

        File desFile = new File(desFilePath);
        if (desFile.exists()) {
            boolean delete = desFile.delete();
            if (!delete) {
                MyLog.e(TAG, "copyOrMoveFileCheck -> 删除老的目的文件失败，拷贝或移动文件执行失败！");
                return false;
            }
        }

        String parent = desFile.getParent();
        File parentDir = new File(parent);
        if (!parentDir.exists()) {
            boolean mkdirs = parentDir.mkdirs();
            if (!mkdirs) {
                MyLog.e(TAG, "copyOrMoveFileCheck -> 创建目的多层文件夹失败！拷贝或移动文件执行失败！");
                return false;
            }
        }
        return true;
    }

    private static boolean copyOrMoveFileDirCheck(String srcFilePath, String desFilePath) {
        File srcFile = checkFileDirExist(srcFilePath);
        if (srcFile == null) {
            MyLog.e(TAG, "copyOrMoveFileDirCheck -> 检测源文件夹不存在，拷贝或移动文件执行失败！");
            return false;
        }

        File desDir = new File(desFilePath);
        if (desDir.exists()) {
            boolean delete = deleteFileDir(desDir);
            if (!delete) {
                MyLog.e(TAG, "copyOrMoveFileDirCheck -> 删除老的目的文件夹失败，拷贝或移动文件执行失败！");
                return false;
            }
        }
        return true;
    }

    private static boolean deleteFileDir(File desDir) {
        if (desDir == null) {
            return false;
        }

        String[] list = desDir.list();
        if (list != null && list.length > 0) {
            for (String name : list) {
                String tarPath = desDir.getAbsolutePath() + File.separator + name;
                File file = new File(tarPath);
                if (file.isDirectory()) {
                    deleteFileDir(file);
                } else {
                    boolean delete = file.delete();
                    if (!delete) {
                        MyLog.e(TAG, "deleteFileDir -> 删除文件夹中的文件失败！" + tarPath);
                        return false;
                    }
                }
            }
        }

        boolean delete = desDir.delete();
        if (!delete) {
            MyLog.e(TAG, "deleteFileDir -> 删除文件夹失败！" + desDir.getAbsolutePath());
            return false;
        }
        return true;
    }

    private static void log(String msg) {
        MyLog.d(TAG, msg);
    }

    public interface FileContentListener {

        String lineTextContent(String line);
    }
}
