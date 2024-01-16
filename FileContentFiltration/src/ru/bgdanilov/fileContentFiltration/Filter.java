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
        String integersFileName = settings.generateResultFileName(resultFilesPrefix, "integers.txt");
        String doublesFileName = settings.generateResultFileName(resultFilesPrefix, "doubles.txt");
        String linesFileName = settings.generateResultFileName(resultFilesPrefix, "lines.txt");

        String resultFilesPath = settings.generateResultFilesPath(this.resultFilesPath, currentDir);

        deleteFiles(settings.isAddMode(), resultFilesPath, integersFileName);
        deleteFiles(settings.isAddMode(), resultFilesPath, doublesFileName);
        deleteFiles(settings.isAddMode(), resultFilesPath, linesFileName);

        boolean writingsError = false;

        if (inputFilesNames.size() == 0) {
            exceptionsMessages.add("Ошибка чтения исходных файлов! Файлы не найдены!");
        }

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
                            writingsError = writeFilteredParts(ddd, resultFilesPath, integersFileName);
                        } else {
                            writingsError = writeFilteredParts(String.valueOf(d), resultFilesPath, doublesFileName);
                        }
                    } catch (NumberFormatException e) {
                        writingsError = writeFilteredParts(line, resultFilesPath, linesFileName);
                    }
                }

                bufferedReader.close();

                if (!writingsError) {
                    System.out.println("Файл: " + inputFileName + " успешно обработан!");
                }
            } catch (IOException e) {
                exceptionsMessages.add("Ошибка чтения исходных файлов! Файл: " + currentDir + inputFileName + " не найден!");
            }
        }

        if (writingsError) {
            exceptionsMessages.add("Ошибка записи результирующих файлов! Проверьте данные или указанный путь: " + resultFilesPath);
        }

        if (exceptionsMessages.size() != 0) {
            System.out.println();
            System.out.println(settings.getEMessageLine(exceptionsMessages));
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
}