//package com.akimatBot.command.impl.admin;
//
//import com.akimatBot.command.Command;
//import com.akimatBot.entity.standart.Message;
//import com.akimatBot.utils.Const;
//import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//public class id022_admin extends Command {
//    @Override
//    public boolean execute() throws TelegramApiException {
//        if (!isAdmin()) {
//            sendMessage(Const.NO_ACCESS);
//            return EXIT;
//        }
//
//        deleteMessage(updateMessageId);
//        Message message = messageRepository.findByIdAndLangId(messageId, getLanguage(chatId).getId());
//        sendMessage(messageId, chatId, null, message.getPhoto());
//
//        if (message.getFile() != null) {
//            switch (message.getFileType()) {
//                case audio:
//                    bot.execute(new SendAudio().setAudio(message.getFile()).setChatId(chatId));
//                case video:
//                    bot.execute(new SendVideo().setVideo(message.getFile()).setChatId(chatId));
//                case document:
//                    bot.execute(new SendDocument().setDocument(message.getFile()).setChatId(chatId));
//            }
//        }
//
//
//        return EXIT;
//    }
//}
