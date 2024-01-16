package ru.bgdanilov.fileContentFiltration;

import java.io.*;
import java.util.ArrayList;

public class Statistics {
    private static Settings settings;
    public static ArrayList<String> exceptionsMessages = new ArrayList<>();
    private final String resultFilesPath;
    private final String resultFilesPrefix;
    private final String currentDir;

    public Statistics(Settings settings) {
        Statistics.settings = settings; // Почему?
        this.currentDir = settings.getCurrentDir();
        resultFilesPath = settings.getResultFilesPath();
        resultFilesPrefix = settings.getResultFilesPrefix();
    }

    public void getStatistics() {
        String integersFileName = settings.generateResultFileName(resultFilesPrefix, "integers.txt");
        String doublesFileName = settings.generateResultFileName(resultFilesPrefix, "doubles.txt");
        String linesFileName = settings.generateResultFileName(resultFilesPrefix, "lines.txt");

        String resultFilesPath = settings.generateResultFilesPath(this.resultFilesPath, currentDir);

        if (new File(resultFilesPath + integersFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        System.out.println("Целых: " + getFileItemsAmount(integersFileName, resultFilesPath) + "штук.");
                case 'f' -> {
                    System.out.println("Целых: " + getFileItemsAmount(integersFileName, resultFilesPath) + "штук.");
                    System.out.println("Статистика целых: " + getFileNumbersStatistics(integersFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + doublesFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        System.out.println("Дробных: " + getFileItemsAmount(doublesFileName, resultFilesPath) + "штук.");
                case 'f' -> {
                    System.out.println("Дробных: " + getFileItemsAmount(doublesFileName, resultFilesPath) + "штук.");
                    System.out.println("Статистика дробных: " + getFileNumbersStatistics(doublesFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + linesFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        System.out.println("Строк: " + getFileItemsAmount(linesFileName, resultFilesPath) + "штук.");
                case 'f' -> {
                    System.out.println("Строк: " + getFileItemsAmount(linesFileName, resultFilesPath) + "штук.");
                    System.out.println("Статистика строк: " + getFileLinesStatistics(linesFileName, resultFilesPath));
                }
            }
        }
    }

    public static int getFileItemsAmount(String resultFileName, String resultFilesPath) {
        int itemsAmount = 0;

        try {
            File file = new File(resultFilesPath + resultFileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((bufferedReader.readLine()) != null) {
                itemsAmount++;
            }

            bufferedReader.close();
        } catch (IOException e) {
            exceptionsMessages.add("Ошибка статистики! " + resultFileName + " невозможно прочитать.");
        }

        if (exceptionsMessages.size() != 0) {
            System.out.println(settings.getEMessageLine(exceptionsMessages));
            return -1;
        } else {
            return itemsAmount;
        }
    }

    public static String getFileNumbersStatistics(String resultFileName, String resultFilesPath) {
        double sum = 0;
        double avg = 0;
        double min = 0;
        double max = 0;

        try {
            File file = new File(resultFilesPath + resultFileName);
            BufferedReader bufferedFirstLineReader = new BufferedReader(new FileReader(file));

            String firstLine = bufferedFirstLineReader.readLine();

            int numbersAmount = 0;
            min = Double.parseDouble(firstLine);
            max = Double.parseDouble(firstLine);
            bufferedFirstLineReader.close();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                double number = Double.parseDouble(line);
                sum += number;
                numbersAmount++;

                if (number < min) {
                    min = number;
                } else if (number > max) {
                    max = number;
                }
            }

            bufferedReader.close();

            avg = sum / numbersAmount;

        } catch (IOException e) {
            exceptionsMessages.add("Ошибка статистики! " + resultFileName + " не найден.");
        }

        if (exceptionsMessages.size() != 0) {
            System.out.println(settings.getEMessageLine(exceptionsMessages));
            return null;
        } else {
            return "Сумма: " + sum + "; Среднее: " + avg + "; Минимум: " + min + "; Максимум: " + max;
        }
    }

    public static String getFileLinesStatistics(String resultFileName, String resultFilesPath) {
        double min = 0;
        double max = 0;
        double lineLength = 0;

        try {
            File file = new File(resultFilesPath + resultFileName);
            BufferedReader bufferedFirstLineReader = new BufferedReader(new FileReader(file));
            String firstLine = bufferedFirstLineReader.readLine();

            min = firstLine.length();
            max = firstLine.length();
            bufferedFirstLineReader.close();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lineLength = line.length();
                if (lineLength < min) {
                    min = lineLength;
                } else if (lineLength > max) {
                    max = lineLength;
                }
            }

        } catch (IOException e) {
            exceptionsMessages.add("Ошибка статистики! " + resultFileName + " не найден.");
        }

        if (exceptionsMessages.size() != 0) {
            System.out.println(settings.getEMessageLine(exceptionsMessages));
            return null;
        } else {
            return "Минимум символов: " + min + "; Максимум символов: " + max;
        }
    }
}