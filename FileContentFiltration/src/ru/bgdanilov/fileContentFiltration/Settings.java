package ru.bgdanilov.fileContentFiltration;

import java.util.ArrayList;
import java.util.Collections;


public class Settings {
    private String resultFilesPath = null; // указание пути для выходных файлов.
    private String resultFilesPrefix = null; // указание префикса имен выходных файлов.
    private boolean addModeInExistingFiles = false; // режим добавления в существующие файлы.
    private char statisticType = 'n'; // тип статистики.
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

    public boolean isAddModeInExistingFiles() {
        return addModeInExistingFiles;
    }

    public void setAddModeInExistingFiles(boolean addModeInExistingFiles) {
        this.addModeInExistingFiles = addModeInExistingFiles;
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
        // TODO: Сделать обрезку списка до шести аргументов, если в конце понаписали лишнего.

        for (int i = 0; i < list.size(); i++) {
            int nextIndex = list.indexOf(args[i]) + 1;

            switch (args[i]) {
                case "-o" -> {
                    if (list.get(nextIndex).startsWith("-")) {
                        exceptionsMessages.add("Не указан путь выходного файла.");
                    } else {
                        setResultFilesPath(list.get(list.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-p" -> {
                    if (list.get(nextIndex).startsWith("-")) {
                        exceptionsMessages.add("Не указан префикс выходного файла.");
                    } else {
                        setResultFilesPrefix(list.get(list.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-a" -> setAddModeInExistingFiles(true);
                case "-s" -> setStatisticType('s');
                case "-f" -> setStatisticType('f');
                default -> exceptionsMessages.add(args[i] + " Не верная команда");
            }
        }

        if (exceptionsMessages.size() != 0) {
            throw new IllegalArgumentException(getEMessageLine(exceptionsMessages));
        }
    }

    public static String getEMessageLine(ArrayList<String> exceptionsMessages) {
        return exceptionsMessages.toString();
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }
}
