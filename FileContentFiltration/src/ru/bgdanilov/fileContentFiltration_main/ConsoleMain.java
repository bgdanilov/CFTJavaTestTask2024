package ru.bgdanilov.fileContentFiltration_main;

import ru.bgdanilov.fileContentFiltration.Filter;
import ru.bgdanilov.fileContentFiltration.Settings;
import ru.bgdanilov.fileContentFiltration.Statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) {
        try {
            // Получаем аргументы.
            String[] array = new String[]{"-o", "boris", "-p", "prefix", "-a", "-s"};
            String utilityFileDirectory = System.getProperty("user.dir") + "/";
            System.out.println(Arrays.toString(array));

            // Загружаем аргументы, делаем настройки.
            Settings settings = new Settings(utilityFileDirectory);
            settings.parseArgs(array);

            // Вводим имена исходных файлов.
            ArrayList<String> inputFilesNames = getInputsFilesNames();

            // Запускаем фильтрацию.
            Filter filter = new Filter(inputFilesNames, settings);
            filter.filterFile();

            // Статистика, если необходимо.
            if (settings.getStatisticType() == 's' || settings.getStatisticType() == 'f') {
                Statistics statistics = new Statistics(settings);
                statistics.getStatistics();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка! " + e.getMessage());
        }
    }

    public static ArrayList<String> getInputsFilesNames() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имена исходных файлов:");

        ArrayList<String> files = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            if (!line.isBlank()) {
                files.add(line);
            }
        }

        return files;
    }
}