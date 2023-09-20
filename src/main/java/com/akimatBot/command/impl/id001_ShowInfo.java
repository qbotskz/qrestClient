package com.akimatBot.command.impl;

import com.akimatBot.command.Command;
import com.akimatBot.config.AppProperties;
import com.akimatBot.entity.enums.Language;
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
    private final UserRepository userRepository = TelegramBotRepositoryProvider.getUserRepository();
    private final List<Role> roles = new ArrayList<>(Collections.singletonList(new Role(2)));
    private User user;

    // This command is executed when /start was clicked
    @Override
    public boolean execute() throws TelegramApiException {
        sendWebApp();
        deleteMessage(updateMessageId);
        LanguageService.setLanguage(chatId, Language.ru);
        return EXIT;
    }

    private void sendWebApp() throws TelegramApiException {
        WebAppData webAppData = new WebAppData();
        webAppData.setData("asd");

//        AnswerWebAppQuery answerWebAppQuery = new AnswerWebAppQuery();
//        answerWebAppQuery.setQueryResult();

        WebAppInfo webAppInfo = new WebAppInfo();
        webAppInfo.setUrl(
                "https://" + AppProperties.properties.getProperty("server.address")
                        + ":" + AppProperties.properties.getProperty("customPort"));

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setWebApp(webAppInfo);
        inlineKeyboardButton.setText(AppProperties.properties.getProperty("restoranName"));

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(List.of(List.of(inlineKeyboardButton)));

        sendMessageWithKeyboard("Нажмите кнопку", replyKeyboardMarkup);
    }
}
