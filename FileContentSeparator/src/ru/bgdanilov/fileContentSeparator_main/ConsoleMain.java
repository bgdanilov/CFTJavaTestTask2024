package ru.bgdanilov.fileContentSeparator_main;

import ru.bgdanilov.fileContentSeparator.Messages;
import ru.bgdanilov.fileContentSeparator.Separator;
import ru.bgdanilov.fileContentSeparator.Settings;
import ru.bgdanilov.fileContentSeparator.Statistics;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) {
        try {
            //String[] settingsArray = new String[]{"-o", "/_aaa", "-p", "prefix", "-f"};
            String utilityHome = System.getProperty("user.dir");
            String lineSeparator = System.lineSeparator();

            // Создаем экземпляр для хранения сообщений (логи).
            Messages messages = new Messages(lineSeparator);

            // Загружаем аргументы, хранилище сообщений - делаем настройки.
            Settings settings = new Settings(utilityHome, messages);
            settings.loadSettings(args);

            // Получаем ссылку на хранение сообщений программы.
            ArrayList<String> runMessages = messages.getRunMessages();

            if (runMessages.size() != 0) {
                System.out.println(messages.composeMessage(runMessages));
                System.out.println();
            }

            // Вводим имена исходных файлов.
            ArrayList<String> sourceFilesNames = collectSourceFilesNames();

            // Запускаем разделение файлов.
            Separator separator = new Separator(sourceFilesNames, settings, lineSeparator);
            separator.createSeparatedFiles();

            // Выводим сообщения по итогам разделения файлов.
            if (runMessages.size() != 0) {
                System.out.println("Запрос выполнен:");
                System.out.println(messages.composeMessage(runMessages));
                System.out.println();
            }

            // Статистика, если необходимо.
            if (settings.getStatisticType() == 's' || settings.getStatisticType() == 'f') {
                Statistics statistics = new Statistics(settings);
                statistics.composeStatistics();
            }

            if (messages.getStatisticsMessages().size() != 0) {
                System.out.println("Статистика:");
                System.out.println(messages.composeMessage(messages.getStatisticsMessages()));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Программа выполнила недопустимую ошибку!");
        }
    }

    public static ArrayList<String> collectSourceFilesNames() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имена исходных файлов:");

        ArrayList<String> sourceFilesNames = new ArrayList<>();

        while (true) {
            String consoleLine = scanner.nextLine().trim();

            if (consoleLine.equals("end")) {
                break;
            }

            if (!consoleLine.isBlank()) {
                sourceFilesNames.add(consoleLine);
            }
        }

        return sourceFilesNames;
    }
}