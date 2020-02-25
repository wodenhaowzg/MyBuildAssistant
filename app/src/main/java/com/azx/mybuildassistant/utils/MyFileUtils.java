package com.azx.mybuildassistant.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileUtils {

    private static final String TAG = MyFileUtils.class.getSimpleName();

    public static boolean modifyFileContent(String filePath, FileContentListener fileContentListener) {
        if (MyTextUtils.isEmpty(filePath)) {
            log("filePath is empty!");
            return false;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            log("file not exist!");
            return false;
        }

        if (!file.isFile()) {
            log("filePath is not file");
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

    private static void log(String msg) {
        System.out.println(TAG + " : " + msg);
    }

    public interface FileContentListener {

        String lineTextContent(String line);
    }
}
