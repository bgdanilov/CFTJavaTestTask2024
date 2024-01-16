package ru.bgdanilov.fileContentFiltration_main;

import ru.bgdanilov.fileContentFiltration.Filter;
import ru.bgdanilov.fileContentFiltration.Settings;
import ru.bgdanilov.fileContentFiltration.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMain {
    public static void main(String[] args) throws IOException {
        try {
            // Получаем аргументы.
            String[] array = new String[]{"-o", "boris", "-p", "result", "-f"};
            String currentDirectory = System.getProperty("user.dir") + "/";
            //System.out.println("Текущая директория: " + currentDirectory);
            System.out.println();

            // Парсим аргументы, делаем настройки.
            Settings settings = new Settings(currentDirectory);
            settings.parseArgs(array);

            // Вводим имена исходных файлов.
            ArrayList<String> inputFilesNames = getInputsFilesNames();

            // Запускаем фильтрацию.
            Filter filter = new Filter(inputFilesNames, settings);
            filter.filterFile();

            // Статистика если надо.
            if (settings.getStatisticType() != 'n') {
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
            String line = scanner.nextLine();

            //TODO: Добавть невключение пробела "" в список файдов.

            if (line.equals("end")) {
                break;
            }

            files.add(line);
        }

        return files;
    }

    public static String getJarPath(Class aclass) {
        try {
            return new File(aclass.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
