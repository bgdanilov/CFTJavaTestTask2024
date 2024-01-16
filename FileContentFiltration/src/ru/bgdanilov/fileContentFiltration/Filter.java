package ru.bgdanilov.fileContentFiltration;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Filter {
    private final ArrayList<String> inputFilesNames;
    private static final String newLine = System.getProperty("line.separator");
    private final Settings settings; // Настройки. Экземпляр класса настроек.
    private final String resultFilesPath;
    private final String resultFilesPrefix;
    private static final ArrayList<String> exceptionsMessages = new ArrayList<>();
    private final String currentDir;

    public Filter(ArrayList<String> inputFilesNames, Settings settings) {
        this.inputFilesNames = inputFilesNames;
        this.settings = settings;
        currentDir = settings.getCurrentDir();
        resultFilesPath = settings.getResultFilesPath();
        resultFilesPrefix = settings.getResultFilesPrefix();
    }

    public void filterFile() {
        String integersFileName = getResultFileName(resultFilesPrefix, "integers.txt");
        String doublesFileName = getResultFileName(resultFilesPrefix, "doubles.txt");
        String linesFileName = getResultFileName(resultFilesPrefix, "lines.txt");

        String resultFilesPath = getResultFilesPath(settings.getResultFilesPath(), currentDir);

        deleteFiles(settings.isAddModeInExistingFiles(), resultFilesPath, integersFileName);
        deleteFiles(settings.isAddModeInExistingFiles(), resultFilesPath, doublesFileName);
        deleteFiles(settings.isAddModeInExistingFiles(), resultFilesPath, linesFileName);

        boolean writingsError = false;

        for (String inputFileName : inputFilesNames) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(currentDir + inputFileName));
                String line;
                DecimalFormat integerFormat = new DecimalFormat("0");

                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        double d = Double.parseDouble(line.replace(',', '.'));

                        if (d % 1 == 0) {
                            String ddd = integerFormat.format(d);
                            //System.out.println("Целое: " + ddd);
                            writingsError = writeFilteredParts(ddd, resultFilesPath, integersFileName);
                        } else {
                            //System.out.println("Дробное: " + d);
                            writingsError = writeFilteredParts(String.valueOf(d), resultFilesPath, doublesFileName);
                        }
                    } catch (NumberFormatException e) {
                        //System.out.println("Строка: " + line);
                        writingsError = writeFilteredParts(line, resultFilesPath, linesFileName);
                    }
                }

                bufferedReader.close();

                if (!writingsError) {
                    System.out.println("Файл: " + inputFileName + " успешно обработан!");
                }

                //TODO: Сюда статистику добавить?

            } catch (IOException e) {
                exceptionsMessages.add("Ошибка чтения исходных файлов! Файл: " + currentDir + inputFileName + " не найден!");
                //System.out.println("Ошибка чтения исходных файлов! Файл: " + inputFileName + " не найден!");
            }
        }

        if (writingsError) {
            exceptionsMessages.add("Ошибка записи результирующих файлов! Проверьте данные или указанный путь: " + resultFilesPath);
            //System.out.println("Ошибка записи результирующих файлов! Проверьте данные или указанный путь: " + resultFilesPath);
        }

        if (exceptionsMessages.size() != 0) {
            //throw new IOException(getEMessageLine(exceptionsMessages));
            System.out.println();
            System.out.println(getEMessageLine(exceptionsMessages));
            System.out.println();
        }
    }

    public static boolean writeFilteredParts(String line, String resultFilesPath, String resultFileName) {
        boolean writingsError = false;

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFilesPath + resultFileName, true));
            bufferedWriter.write(line + newLine);
            bufferedWriter.close();
        } catch (IOException e) {
            writingsError = true;
        }

        return writingsError;
    }

    public static void deleteFiles(boolean aaa, String resultFilesPath, String resultFileName) {
        if (!aaa) {
            File myFile = new File(resultFilesPath + resultFileName);
            if (myFile.exists()) myFile.delete();
        }
    }

    public static String getResultFileName(String resultFilePrefix, String fileName) {
        if (resultFilePrefix != null) {
            return resultFilePrefix + "_" + fileName;
        } else {
            return fileName;
        }
    }

    public static String getResultFilesPath(String resultFilesPath, String currentDir) {
        if (resultFilesPath != null) {
            return  currentDir + resultFilesPath + "/";
        } else {
            return currentDir;
        }
    }

//    public static String getResultFilesPath(String resultFilesPath) {
//        if (resultFilesPath != null) {
//            return "FileContentFiltration/src/files" + resultFilesPath + "/";
//        } else {
//            return "FileContentFiltration/src/files/";
//        }
//    }

    public String getResultFilesPath() {
        return resultFilesPath;
    }

    public String getResultFilesPrefix() {
        return resultFilesPrefix;
    }

    public static String getEMessageLine(ArrayList<String> exceptionsMessages) {
        return exceptionsMessages.toString();
    }
}