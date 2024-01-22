package ru.bgdanilov.fileContentSeparator;

import java.io.*;

public class Statistics {
    private final Settings settings;
    private final Messages messages;
    private final String userPath;
    private final String userPrefix;
    private final String utilityHome;

    public Statistics(Settings settings) {
        this.settings = settings;

        messages = settings.getMessages();
        userPath = settings.getUserPath();
        userPrefix = settings.getUserPrefix();
        utilityHome = settings.getUtilityHome();
    }

    public void collectStatistics() {
        String integersFileName = settings.composeResultFileName(userPrefix, "integers.txt");
        String floatsFileName = settings.composeResultFileName(userPrefix, "floats.txt");
        String stringsFileName = settings.composeResultFileName(userPrefix, "strings.txt");

        String resultFilesPath = settings.composeResultFilesPath(userPath, utilityHome);

        if (new File(resultFilesPath + integersFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Целых: " + calcAllLinesAmount(integersFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Целых: " + calcAllLinesAmount(integersFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика целых: " + calcNumbersStatistics(integersFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + floatsFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Дробных: " + calcAllLinesAmount(floatsFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Дробных: " + calcAllLinesAmount(floatsFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика дробных: " + calcNumbersStatistics(floatsFileName, resultFilesPath));
                }
            }
        }

        if (new File(resultFilesPath + stringsFileName).exists()) {
            switch (settings.getStatisticType()) {
                case 's' ->
                        messages.addStatisticsMessage("Строк: " + calcAllLinesAmount(stringsFileName, resultFilesPath) + ".");
                case 'f' -> {
                    messages.addStatisticsMessage("Строк: " + calcAllLinesAmount(stringsFileName, resultFilesPath) + ".");
                    messages.addStatisticsMessage("Статистика строк: " + calcLinesStatistics(stringsFileName, resultFilesPath));
                }
            }
        }
    }

    public String calcAllLinesAmount(String resultFileName, String resultFilesPath) {
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
            return String.valueOf(itemsAmount);
        }
    }

    public String calcNumbersStatistics(String resultFileName, String resultFilesPath) {
        double sum = 0;
        double average = 0;
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

            average = sum / numbersAmount;

        } catch (IOException e) {
            isReadError = true;
        }

        if (isReadError) {
            messages.addStatisticsMessage("Ошибка статистики! " + resultFileName + " невозможно прочитать.");
            return "нет данных";
        } else {
            return "Сумма: " + sum
                    + "; Среднее: " + average
                    + "; Минимум: " + min
                    + "; Максимум: " + max + ".";
        }
    }

    public String calcLinesStatistics(String resultFileName, String resultFilesPath) {
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
            return "Минимум символов: " + min
                    + "; Максимум символов: " + max + ".";
        }
    }
}