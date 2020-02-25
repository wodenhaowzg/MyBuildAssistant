package com.azx.mybuildassistant.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {

    public void getFileContent(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
