package ru.bgdanilov.fileContentSeparator;

import java.io.*;
import java.text.DecimalFormat;

public class Statistics {
    private final Settings settings;
    private final Messages messages;
    private final String userFilesPath;
    private final String filesPrefix;
    private final String userHome;

    public Statistics(Settings settings) {
        this.settings = settings;

        messages = settings.getMessages();
        userFilesPath = settings.getUserFilesPath();
        filesPrefix = settings.getFilesPrefix();
        userHome = settings.getUserHome();
    }

    public void composeStatistics() {
        String integersFileName = settings.composeResultFileName(filesPrefix, "integers.txt");
        String doublesFileName = settings.composeResultFileName(filesPrefix, "doubles.txt");
        String linesFileName = settings.composeResultFileName(filesPrefix, "lines.txt");

        String resultFilesPath = settings.composeResultFilesPath(userFilesPath, userHome);

        if (new File(resultFilesPath + integersFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Целых: " + getFileItemsAmount(integersFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Целых: " + getFileItemsAmount(integersFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика целых: " + getFileNumbersStatistics(integersFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + doublesFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Дробных: " + getFileItemsAmount(doublesFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Дробных: " + getFileItemsAmount(doublesFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика дробных: " + getFileNumbersStatistics(doublesFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + linesFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Строк: " + getFileItemsAmount(linesFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Строк: " + getFileItemsAmount(linesFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика строк: " + getFileLinesStatistics(linesFileName, resultFilesPath));
                }
            }
        }
    }

    public String getFileItemsAmount(String resultFileName, String resultFilesPath) {
        double itemsAmount = 0;
        boolean isReadError = false;

        try {
            File file = new File(resultFilesPath + resultFileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((bufferedReader.readLine()) != null) {
                itemsAmount++;
            }

            bufferedReader.close();
        } catch (IOException e) {
            isReadError = true;
        }

        if (isReadError) {
            messages.addStatisticsMessage("Ошибка статистики! " + resultFileName + " невозможно прочитать.");
            return "нет данных.";
        } else {
            return calcRoundedNumberLine(itemsAmount);
        }
    }

    public String getFileNumbersStatistics(String resultFileName, String resultFilesPath) {
        double sum = 0;
        double avg = 0;
        double min = 0;
        double max = 0;

        boolean isReadError = false;

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
            isReadError = true;
        }

        if (isReadError) {
            messages.addStatisticsMessage("Ошибка статистики! " + resultFileName + " невозможно прочитать.");
            return "нет данных";
        } else {
            return "Сумма: " + calcRoundedNumberLine(sum)
                    + "; Среднее: " + calcRoundedNumberLine(avg)
                    + "; Минимум: " + calcRoundedNumberLine(min)
                    + "; Максимум: " + calcRoundedNumberLine(max) + ".";
        }
    }

    public String getFileLinesStatistics(String resultFileName, String resultFilesPath) {
        double min = 0;
        double max = 0;
        double lineLength;

        boolean isReadError = false;

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
            isReadError = true;
        }

        if (isReadError) {
            messages.addStatisticsMessage("Ошибка статистики! " + resultFileName + " невозможно прочитать.");
            return "нет данных";
        } else {
            return "Минимум символов: " + calcRoundedNumberLine(min)
                    + "; Максимум символов: " + calcRoundedNumberLine(max) + ".";
        }
    }

    private static String calcRoundedNumberLine(double number) {
        DecimalFormat exponentialFormat = new DecimalFormat("0.00E00");

        return number < 10000
                ? String.valueOf((double) Math.round(number * 100) / 100)
                : exponentialFormat.format(number);
    }
}