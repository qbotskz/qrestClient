package com.akimatBot.command.impl;


import com.akimatBot.command.Command;
import com.akimatBot.entity.standart.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class id031_OpenLink extends Command {

    int deleteId;
    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException {
        deleteMessage(updateMessageId);
        switch (waitingType){
            case START:
                updateMessageText = updateMessageText.replaceAll("/start", "");
                updateMessageText = updateMessageText.replaceAll(" ", "");
                Long id = Long.parseLong(updateMessageText);
                User user = userRepository.findByChatId(chatId);
                if ( user.getCurrentGuest() == null
                        ||
                        user.getCurrentGuest().getFoodOrder().getDesk().getId() == id){
                    sendWebApp(id);
                }
                else {
                    sendMessage("Вы сидите за столом №" +
                            user.getCurrentGuest().getFoodOrder().getDesk().getId()
                            + ", вам нельзя сканировать QR Code другого стола!");
                }

                return COMEBACK;
        }
        return EXIT;
    }

    private void deleteMessages() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteId);
    }
    private void sendWrongData() throws TelegramApiException {
        toDeleteMessage(sendMessage(18));
    }


    private List<String> getNames(User user) {
        List<String> names = new ArrayList<>();
                names.add(user.getFullName());
        return names;
    }



    private void sendWebApp(long deskId) throws TelegramApiException {

        System.out.println("desk of client = " + deskId);
        WebAppData webAppData = new WebAppData();
        webAppData.setData("asd");



        WebAppInfo webAppInfo = new WebAppInfo();

        webAppInfo.setUrl("https://109.233.108.126:450?deskId=" + deskId);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setWebApp(webAppInfo);
        inlineKeyboardButton.setText("QRestoran");

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(Arrays.asList(Arrays.asList(inlineKeyboardButton)));

        sendMessageWithKeyboard("Нажмите кнопку", replyKeyboardMarkup);
    }





}
