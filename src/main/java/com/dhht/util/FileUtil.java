package com.dhht.util;

import com.dhht.model.FileInfo;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }


    public static String saveArrayFile(int[][] imgArr){
        try {
            String fileName = UUIDUtil.generate() + ".txt";
            String path = generatePathByDate() + fileName;
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.mkdir();
            }
            FileWriter fileWriter = new FileWriter(file);
            for (int a = 0; a < imgArr.length; a++) {
                for (int b = 0; b < imgArr[a].length; b++) {
                    fileWriter.write(imgArr[a][b]+" ");
                }
            }
            return path;
        }catch (IOException ioe){
            return "";
        }catch (Exception e){
            return "";
        }
    }

    public  static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static int[][] readArrayFile(String path){
        try {
            int[][] arrayData = new int[500][500];
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            int row = 0;
            while (true){
                line = in.readLine();
                if(line == null||line==""){
                    break;
                }
                String str[] = line.split("\n");
                String[] temp = str.toString().split(" ");
                for (int i = 0;i<temp.length;i++){
                    if(temp[i]!=" ") {
                        arrayData[row][i] = Integer.parseInt(temp[i]);
                    }
                }
                row++;
            }
            return arrayData;
        }catch (Exception e){
           System.out.println(e.toString());
           return null;
        }
    }

    public static String generatePathByDate(){
        String date =new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String path = "C:/temp/" + date+"/";
        return path;
    }
}
