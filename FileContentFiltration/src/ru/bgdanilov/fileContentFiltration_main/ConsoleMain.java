package ru.bgdanilov.fileContentFiltration_main;

import ru.bgdanilov.fileContentFiltration.Filter;
import ru.bgdanilov.fileContentFiltration.Settings;
import ru.bgdanilov.fileContentFiltration.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) throws IOException {
        try {
            // Вводим имена исходных файлов.
            ArrayList<String> inputFilesNames = getInputsFilesNames();

            // Получаем аргументы.
            String[] array = new String[]{"-o", "/boris", "-p", "result", "-f"};

            // Парсим аргументы, делаем настройки.
            Settings settings = new Settings();
            settings.parseArgs(array);

            // Запускаем фильтрацию.
            Filter filter = new Filter(inputFilesNames, settings);
            filter.filterFile();

            // Статистика если надо.
            if (settings.getStatisticType() != 'n') {
                Statistics statistics = new Statistics(settings);
                statistics.countResultFilesItemsAmount();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка! " + e.getMessage());
        }
    }

    public static ArrayList<String> getInputsFilesNames() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имена исходных файлов.");

        ArrayList<String> files = new ArrayList<>();

        while (true) {
            String line = scanner.nextLine();

            //TODO: Добавть невключение пробела "" в список файдов.

            if (line.equals("end")) {
                break;
            }

            files.add(line);
        }

        return files;
    }
}
