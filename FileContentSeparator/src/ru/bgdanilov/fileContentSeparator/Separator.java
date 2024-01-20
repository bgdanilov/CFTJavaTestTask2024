package ru.bgdanilov.fileContentSeparator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Separator {
    private final Settings settings; // Настройки. Экземпляр класса настроек.
    private final Messages messages; // Экземпляр класса сообщений.
    private final ArrayList<String> sourceFilesNames;
    private final String lineSeparator;
    private final String userPath;
    private final String userPrefix;
    private final String utilityHome;

    public Separator(ArrayList<String> sourceFilesNames, Settings settings, String lineSeparator) {
        this.sourceFilesNames = sourceFilesNames;
        this.settings = settings;
        this.lineSeparator = lineSeparator;

        messages = settings.getMessages();
        userPath = settings.getUserPath();
        userPrefix = settings.getUserPrefix();
        utilityHome = settings.getUtilityHome();
        //throw new RuntimeException(); // проверка на неучтенную ошибку.
    }

    public void createSeparatedFiles() {
        String integersFileName = settings.composeResultFileName(userPrefix, "integers.txt");
        String floatsFileName = settings.composeResultFileName(userPrefix, "floats.txt");
        String stringsFileName = settings.composeResultFileName(userPrefix, "strings.txt");

        String resultFilesPath = settings.composeResultFilesPath(userPath, utilityHome);

        deleteFiles(settings.getIsAddMode(), resultFilesPath, integersFileName);
        deleteFiles(settings.getIsAddMode(), resultFilesPath, floatsFileName);
        deleteFiles(settings.getIsAddMode(), resultFilesPath, stringsFileName);

        boolean isWritingError = false;

        if (sourceFilesNames.size() == 0) {
            messages.addRunMessage("Ошибка чтения исходных файлов! Файлы не выбраны.");
        }

        for (String sourceFileName : sourceFilesNames) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(utilityHome + "/" + sourceFileName));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        double number = Double.parseDouble(line.replace(',', '.'));

                        if (number % 1 == 0) {
                            DecimalFormat integerFormat = new DecimalFormat("0");
                            isWritingError = isWriteLine(integerFormat.format(number), resultFilesPath, integersFileName);
                        } else {
                            isWritingError = isWriteLine(String.valueOf(number), resultFilesPath, floatsFileName);
                        }
                    } catch (NumberFormatException e) {
                        isWritingError = isWriteLine(line, resultFilesPath, stringsFileName);
                    }
                }

                bufferedReader.close();

                if (!isWritingError) {
                    messages.addRunMessage("Файл: " + sourceFileName + " успешно обработан!");
                }
            } catch (IOException e) {
                messages.addRunMessage("Ошибка чтения исходных файлов! Файл: " + utilityHome + "/" + sourceFileName + " не найден!");
            }
        }

        if (isWritingError) {
            messages.addRunMessage("Ошибка записи результирующих файлов! Проверьте данные или указанный путь: " + resultFilesPath);
        }
    }

    public boolean isWriteLine(String line, String resultFilesPath, String resultFileName) {
        boolean writingError = false;

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFilesPath + resultFileName, true));
            bufferedWriter.write(line + lineSeparator);
            bufferedWriter.close();
        } catch (IOException e) {
            writingError = true;
        }

        return writingError;
    }

    public void deleteFiles(boolean isAddMode, String resultFilesPath, String resultFileName) {
        if (!isAddMode) {
            File oldResultFile = new File(resultFilesPath + resultFileName);

            if (oldResultFile.exists()) {
                if (oldResultFile.delete()) {
                    messages.addRunMessage("Файл: " + oldResultFile + " удален.");
                }
            }
        }
    }
}