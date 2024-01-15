package ru.bgdanilov.fileContentFiltration;

import java.io.*;
import java.util.ArrayList;

public class Statistics {
    private final Settings settings;
    public static ArrayList<String> exceptionsMessages = new ArrayList<>();

    public Statistics(Settings settings) {
        this.settings = settings;
    }

    public void countResultFilesItemsAmount() {
        String integersFileName = getResultFileName(settings.getResultFilesPrefix(), "integers.txt");
        String doublesFileName = getResultFileName(settings.getResultFilesPrefix(), "doubles.txt");
        String linesFileName = getResultFileName(settings.getResultFilesPrefix(), "lines.txt");

        String resultFilesPath = getResultFilesPath(settings.getResultFilesPath());

        System.out.println("Целых: " + getFileItemsAmount(integersFileName, resultFilesPath));
        System.out.println("Дробных: " + getFileItemsAmount(doublesFileName, resultFilesPath));
        System.out.println("Строк: " + getFileItemsAmount(linesFileName, resultFilesPath));

        System.out.println("Статистика целых: " + getFileNumbersStatistics(integersFileName, resultFilesPath));
        System.out.println("Статистика дробных: " + getFileNumbersStatistics(doublesFileName, resultFilesPath));

        System.out.println("Статистика строк: " + getFileLinesStatistics(linesFileName, resultFilesPath));
    }

    public static int getFileItemsAmount(String resultFileName, String resultFilesPath) {
        int itemsAmount = 0;

        File file = new File(resultFilesPath + resultFileName);

        if (file.exists()) {

            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                while ((bufferedReader.readLine()) != null) {
                    itemsAmount++;
                }

                bufferedReader.close();
            } catch (IOException e) {
                exceptionsMessages.add("Ошибка статистики! " + resultFileName + " не найден.");
            }
        }

        if (exceptionsMessages.size() != 0) {
            //throw new FileNotFoundException(getEMessageLine(exceptionsMessages));
            System.out.println(getEMessageLine(exceptionsMessages));
        }

        return itemsAmount;
    }

    public static String getFileNumbersStatistics(String resultFileName, String resultFilesPath) {
        double sum = 0;
        double avg = 0;
        double min = 0;
        double max = 0;

        File file = new File(resultFilesPath + resultFileName);

        if (file.exists()) {

            try {
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
        }

        if (exceptionsMessages.size() != 0) {
            //throw new FileNotFoundException(getEMessageLine(exceptionsMessages));
            System.out.println(getEMessageLine(exceptionsMessages));
        }

        return sum + "  " + avg + "  " + min + "  " + max;
    }

    public static String getFileLinesStatistics(String resultFileName, String resultFilesPath) {
        double min = 0;
        double max = 0;
        double lineLength = 0;

        File file = new File(resultFilesPath + resultFileName);

        if (file.exists()) {
            try {
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
                //throw new FileNotFoundException(getEMessageLine(exceptionsMessages));
                System.out.println(getEMessageLine(exceptionsMessages));
            }
        }

        return min + "  " + max;
    }

    public static String getResultFileName(String resultFilePrefix, String fileName) {
        if (resultFilePrefix != null) {
            return resultFilePrefix + "_" + fileName;
        } else {
            return fileName;
        }
    }

    public static String getResultFilesPath(String resultFilesPath) {
        if (resultFilesPath != null) {
            return "FileContentFiltration/src/files" + resultFilesPath + "/";
        } else {
            return "FileContentFiltration/src/files/";
        }
    }

    public static String getEMessageLine(ArrayList<String> exceptionsMessages) {
        return exceptionsMessages.toString();
    }
}
