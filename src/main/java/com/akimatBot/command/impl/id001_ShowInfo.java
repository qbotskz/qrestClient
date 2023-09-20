package com.akimatBot.command.impl;

import com.akimatBot.command.Command;
import com.akimatBot.config.AppProperties;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.enums.WaitingType;
import com.akimatBot.entity.standart.Role;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.TelegramBotRepositoryProvider;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.services.LanguageService;
import com.akimatBot.utils.Const;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class id001_ShowInfo extends Command {
    // This command is executed when /start was clicked
    @Override
    public boolean execute() throws TelegramApiException {
        sendWebApp();
        deleteMessage(updateMessageId);
        LanguageService.setLanguage(chatId, Language.ru);
        return EXIT;
    }

    private void sendWebApp() throws TelegramApiException {
        sendMessage("Құрметті қонақ!\n" +
                "\n" +
                "Тапсырыс беру үшін QR код сканерлеңіз.\n" +
                "\n" +
                "\n" +
                "Уважаемый гость!\n" +
                "\n" +
                "Для оформления заказа, пожалуйста отсканируйте QR код.");
    }
}
