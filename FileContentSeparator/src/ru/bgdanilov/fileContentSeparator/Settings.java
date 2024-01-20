package ru.bgdanilov.fileContentSeparator;

import java.util.*;

public class Settings {
    private final Messages messages; // Экземпляр класса сообщений.
    private String userPath = null; // -o указание пути для выходных файлов.
    private String userPrefix = null; // -p указание префикса имен выходных файлов.
    private boolean isAddMode = false; // -a режим добавления в существующие файлы.
    private char statisticType; // -f (полная), -s (краткая) тип статистики.
    private final String utilityHome; // Путь, где лежит утилита.
    private final String userHome = System.getProperty("user.home"); // Домашний каталог пользователя.

    public Settings(String utilityHome, Messages messages) {
        this.utilityHome = utilityHome;
        this.messages = messages;
    }

    public Messages getMessages() {
        return messages;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getUserPrefix() {
        return userPrefix;
    }

    public void setUserPrefix(String userPrefix) {
        this.userPrefix = userPrefix;
    }

    public boolean getIsAddMode() {
        return isAddMode;
    }

    public void setIsAddMode(boolean addMode) {
        this.isAddMode = addMode;
    }

    public char getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(char statisticType) {
        this.statisticType = statisticType;
    }

    public String getUtilityHome() {
        return utilityHome;
    }

    ArrayList<String> settingsDuplicates;

    public void loadSettings(String[] args) {
        List<String> settings = Arrays.asList(args);
        settingsDuplicates = collectSettingsDuplicates(args);

        if (settingsDuplicates.size() != 0) {
            messages.addRunMessage(composeMessageLine(settingsDuplicates) + ": команды повторяются.");
        }

        for (int i = 0; i < settings.size(); i++) {
            int nextIndex = settings.indexOf(args[i]) + 1; // Первое вхождение.

            if (settingsDuplicates.contains(args[i])) {
                continue;
            }

            switch (args[i]) {
                case "-o" -> {
                    if (nextIndex == settings.size() || settings.get(nextIndex).startsWith("-")) {
                        messages.addRunMessage("[" + args[i] + "]: не указан путь выходного файла.");
                    } else {
                        setUserPath(settings.get(settings.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-p" -> {
                    if (nextIndex == settings.size() || settings.get(nextIndex).startsWith("-")) {
                        messages.addRunMessage("[" + args[i] + "]: не указан префикс выходного файла.");
                    } else {
                        setUserPrefix(settings.get(settings.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-a" -> setIsAddMode(true);
                case "-s" -> setStatisticType('s');
                case "-f" -> setStatisticType('f');
                case "-help" -> {
                    System.out.println("Это раздел помощи.");
                    System.out.println("---------");
                    System.out.println("1. Задача утилиты записать разные типы данных в разные файлы.");
                    System.out.println("Целые числа в один выходной файл, дробные в другой, строки в третий.");
                    System.out.println("---------");
                    System.out.println("2. Утилита считывает txt-файлы, находящиеся в той же папке, что и сама утилита.");
                    System.out.println("Имена исходных файлов вводятся поочереди, каждое в новой строке.");
                    System.out.println("Последней строкой вводится команда для завершения ввода: end.");
                    System.out.println("---------");
                    System.out.println("3. По умолчанию файлы с результатами располагаются в текущей папке с именами integers.txt, floats.txt, strings.txt.");
                    System.out.println("-o - Опция задает путь для результатов относительно папки пользователя.");
                    System.out.println("Например: -o /some/path.");
                    System.out.println("-p - Опция задает префикс результирующих файлов.");
                    System.out.println("Например -p prefix - создаст файл prefix_integers.txt и т.д.");
                    System.out.println("По умолчанию файлы результатов перезаписываются.");
                    System.out.println("-a - Опция задает режим добавления в существующие файлы.");
                    System.out.println("-s и -f - Опции позволяют вывести краткую или полную статистику соответственно.");
                    System.out.println("Краткая статистика содержит только количество элементов записанных в исходящие файлы.");
                    System.out.println("Полная статистика для чисел дополнительно содержит минимальное и максимальное значения, сумму и среднее.");
                    System.out.println("Полная статистика для строк, помимо их количества, содержит также размер самой короткой строки и самой длинной.");
                    System.out.println("---------");
                    System.out.println("Пример: -o /some_path -p prefix -a -f");
                    System.out.println("Комбинации [-опция + параметр] можно вводить в любой последовательности.");
                    System.out.println("---------");
                    System.out.println("Путь к папке с утилитой: " + utilityHome);
                    System.out.println("Путь к папке пользователя: " + userHome);
                    System.out.println();
                }
                default -> messages.addRunMessage("[" + args[i] + "]: не является командой.");
            }
        }
    }

    public String composeResultFileName(String resultFilePrefix, String fileName) {
        if (resultFilePrefix != null) {
            return resultFilePrefix + "_" + fileName;
        } else {
            return fileName;
        }
    }

    public String composeResultFilesPath(String userPath, String utilityHome) {
        if (userPath != null) {
            return userHome + userPath + "/";
        } else {
            return utilityHome + "/";
        }
    }

    public ArrayList<String> collectSettingsDuplicates(String[] settings) {
        ArrayList<String> settingsDuplicates = new ArrayList<>();
        ArrayList<String> verificationSet = new ArrayList<>();

        for (String item : settings) {
            if (item.equals("-f") || item.equals("-s")) {
                item = "-f, -s";
            }

            if (item.charAt(0) == '-' && verificationSet.contains(item)) {
                settingsDuplicates.add(item);
                verificationSet.remove(item);
            } else {
                verificationSet.add(item);
            }
        }

        return settingsDuplicates;
    }

    public String composeMessageLine(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (String item : list) {
            sb.append(item).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append(']');

        return sb.toString();
    }
}