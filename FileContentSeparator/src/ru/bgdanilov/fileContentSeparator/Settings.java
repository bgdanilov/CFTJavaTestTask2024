package ru.bgdanilov.fileContentSeparator;

import java.io.File;
import java.util.*;


public class Settings {
    private final Messages messages; // Экземпляр класса сообщений.
    private String userFilesPath = null; // -o указание пути для выходных файлов.
    private String filesPrefix = null; // -p указание префикса имен выходных файлов.
    private boolean isAddMode = false; // -a режим добавления в существующие файлы.
    private char statisticType; // -f (полная), -s (краткая) тип статистики.
    private final String utilityHome; // Путь, где лежит утилита.
    private final String userHome = System.getProperty("user.home"); // Домашний каталог пользователя.

    public Settings(String utilityHome, Messages messages) {
        this.utilityHome = utilityHome;
        this.messages = messages;
    }

    public String getUserFilesPath() {
        return userFilesPath;
    }
    public void setUserFilesPath(String userFilesPath) {
        this.userFilesPath = userFilesPath;
    }
    public String getFilesPrefix() {
        return filesPrefix;
    }
    public void setFilesPrefix(String filesPrefix) {
        this.filesPrefix = filesPrefix;
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
    public String getUserHome() {
        return userHome;
    }

    ArrayList<String> settingsDuplicates;

    public void loadSettings(String[] args) {
        List<String> settings = Arrays.asList(args);
        settingsDuplicates = composeSettingsDuplicates(args);

        if (settingsDuplicates.size() != 0) {
            messages.addRunMessage(settingsDuplicates + ": команды повторяются.");
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
                        setUserFilesPath(settings.get(settings.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-p" -> {
                    if (nextIndex == settings.size() || settings.get(nextIndex).startsWith("-")) {
                        messages.addRunMessage("[" + args[i] + "]: не указан префикс выходного файла.");
                    } else {
                        setFilesPrefix(settings.get(settings.indexOf(args[i]) + 1));
                        i++;
                    }
                }
                case "-a" -> setIsAddMode(true);
                case "-s" -> setStatisticType('s');
                case "-f" -> setStatisticType('f');
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

    public String composeResultFilesPath(String userFilesPath, String utilityHome) {
        if (userFilesPath != null) {
            File directory = new File(userHome + userFilesPath);

            if (!directory.exists()) {
                if (directory.mkdir()) {
                    messages.addRunMessage("Создана новая папка: " + userHome + userFilesPath);
                }
            }

            return directory + "/";
        } else {
            return utilityHome + "/";
        }
    }

    public ArrayList<String> composeSettingsDuplicates(String[] settings) {
        ArrayList<String> settingsDuplicates = new ArrayList<>();
        ArrayList<String> verificationSet = new ArrayList<>();

        for (String item : settings) {
            if (item.charAt(0) == '-' && verificationSet.contains(item)) {
                settingsDuplicates.add(item);
                verificationSet.remove(item);
            } else {
                verificationSet.add(item);
            }
        }

        return settingsDuplicates;
    }

    public Messages getMessages() {
        return messages;
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder().append('{');
//        int maxIndex = settingsDuplicates.size() - 1;
//
//        for (int i = 0; i < maxIndex; i++) {
//            sb.append(settingsDuplicates.get(i)).append(", ");
//        }
//
//        sb.append(settingsDuplicates.get(maxIndex)).append('}');
//
//        return sb.toString();
//    }
}