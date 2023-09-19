package com.akimatBot.command.impl;

import com.akimatBot.command.Command;
import com.akimatBot.utils.reports.FoodsReport;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id006_FoodsList extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        try {

            FoodsReport foodsReport = new FoodsReport();
            foodsReport.sendCompReport();
            sendMessage("Отправлено!");
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("Произошла не приведенная ошибка!");
        }
        return EXIT;
    }

}
