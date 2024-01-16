package ru.bgdanilov.fileContentFiltration;

import java.util.*;


public class Settings {
    private String resultFilesPath = null; // -o указание пути для выходных файлов.
    private String resultFilesPrefix = null; // -p указание префикса имен выходных файлов.
    private boolean isAddMode = false; // -a режим добавления в существующие файлы.
    private char statisticType; // -f (полная), -s (краткая) тип статистики.
    private static final ArrayList<String> exceptionsMessages = new ArrayList<>();
    private String currentDir;

    public Settings(String currentDir) {
        this.currentDir = currentDir;
    }

    public String getResultFilesPath() {
        return resultFilesPath;
    }

    public void setResultFilesPath(String resultFilesPath) {
        this.resultFilesPath = resultFilesPath;
    }

    public String getResultFilesPrefix() {
        return resultFilesPrefix;
    }

    public void setResultFilesPrefix(String resultFilesPrefix) {
        this.resultFilesPrefix = resultFilesPrefix;
    }

    public boolean isAddMode() {
        return isAddMode;
    }

    public void setAddMode(boolean addMode) {
        this.isAddMode = addMode;
    }

    public char getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(char statisticType) {
        this.statisticType = statisticType;
    }

    public void parseArgs(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, args);

        ArrayList<String> ddd = containsDuplicate(args);

        if (ddd.size() != 0) {
            exceptionsMessages.add(ddd + " команды повторяются");
        }

        for (int i = 0; i < list.size(); i++) {
            int nextIndex = list.indexOf(args[i]) + 1; // Первое вхождение.

            switch (args[i]) {
                case "-o" -> {
                    if (nextIndex == list.size() || list.get(nextIndex).startsWith("-")) {
                        exceptionsMessages.add("Не указан путь выходного файла.");
                    } else {
                        setResultFilesPath(list.get(list.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-p" -> {
                    if (nextIndex == list.size() || list.get(nextIndex).startsWith("-")) {
                        exceptionsMessages.add("Не указан префикс выходного файла.");
                    } else {
                        setResultFilesPrefix(list.get(list.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-a" -> setAddMode(true);
                case "-s" -> setStatisticType('s');
                case "-f" -> setStatisticType('f');
                default -> exceptionsMessages.add(args[i] + " Не верная команда");
            }
        }

        if (exceptionsMessages.size() != 0) {
            throw new IllegalArgumentException(getEMessageLine(exceptionsMessages));
        }
    }

    public String generateResultFileName(String resultFilePrefix, String fileName) {
        if (resultFilePrefix != null) {
            return resultFilePrefix + "_" + fileName;
        } else {
            return fileName;
        }
    }

    public String generateResultFilesPath(String resultFilesPath, String currentDir) {
        if (resultFilesPath != null) {
            return currentDir + resultFilesPath + "/";
        } else {
            return currentDir;
        }
    }

    public String getEMessageLine(ArrayList<String> exceptionsMessages) {
        return exceptionsMessages.toString();
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public ArrayList<String> containsDuplicate(String[] nums) {
        ArrayList<String> sss = new ArrayList<>();
        Set<String> set = new HashSet<>();

        for (String num : nums) {
            if (set.contains(num)) {
                sss.add(num);
                set.remove(num);
            } else {
                set.add(num);
            }
        }

        return sss;
    }
}