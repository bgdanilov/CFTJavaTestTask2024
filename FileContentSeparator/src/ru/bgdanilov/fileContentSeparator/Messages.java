package ru.bgdanilov.fileContentSeparator;

import java.util.ArrayList;

public class Messages {
    private final ArrayList<String> runMessages = new ArrayList<>();
    private final ArrayList<String> statisticsMessages = new ArrayList<>();
    private final String lineSeparator;

    public Messages(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public ArrayList<String> getRunMessages() {
        return runMessages;
    }

    public void addRunMessage(String message) {
        runMessages.add(message);
    }

    public ArrayList<String> getStatisticsMessages() {
        return statisticsMessages;
    }

    public void addStatisticsMessage(String message) {
        statisticsMessages.add(message);
    }

    public String composeMessage(ArrayList<String> messagesSet) {
        StringBuilder sb = new StringBuilder();
        int maxIndex = messagesSet.size() - 1;

        for (int i = 0; i < maxIndex; i++) {
            sb.append(messagesSet.get(i)).append(lineSeparator);
        }

        return sb.append(messagesSet.get(maxIndex)).toString();
    }
}