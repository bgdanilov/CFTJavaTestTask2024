package ru.bgdanilov.fileContentFiltration.model;

import java.io.*;

public class Statistics2 {

    public static int getFileItemsAmount(String resultFileName, String resultFilesPath) throws IOException {
        int itemsAmount = 0;
        File file = new File(resultFilesPath + resultFileName);

        if (file.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((bufferedReader.readLine()) != null) {
                itemsAmount++;
            }

            bufferedReader.close();
        } else {
            itemsAmount = -1;
        }

        return itemsAmount;
    }
}
