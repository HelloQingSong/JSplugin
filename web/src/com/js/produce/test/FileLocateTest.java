package com.js.produce.test;

import java.io.File;

/**
 * Created by 余青松 on 2017/8/3.
 */
public class FileLocateTest {
    public static void whereIsFile(String filepath){
        try {
            File file = new File(filepath);
            if (!file.exists()) file.createNewFile();
            System.out.println(file.getAbsolutePath());
            file.deleteOnExit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
