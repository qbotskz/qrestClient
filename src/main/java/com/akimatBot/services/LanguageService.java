package com.akimatBot.services;

import com.akimatBot.entity.custom.AboutRestaurant;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.TelegramBotRepositoryProvider;
import com.akimatBot.repository.repos.AboutRestaurantRepo;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.web.dto.AboutRestDTO;
import org.springframework.stereotype.Component;


@Component
public class LanguageService {

    private static final UserRepository usersRepo = TelegramBotRepositoryProvider.getUserRepository();
    private static final AboutRestaurantRepo aboutRestaurantRepo = TelegramBotRepositoryProvider.getAboutRestaurantRepo();

    public static Language getLanguage(long chatId) {

        Language language = usersRepo.getLanguageByChatId(chatId);
        if (language != null) {
            return language;
        } else {
            return Language.ru;
        }
    }

    public static AboutRestDTO getAboutRest(long chatId) {
        Language language = usersRepo.getLanguageByChatId(chatId);
        AboutRestaurant a = aboutRestaurantRepo.findById(1);
        AboutRestDTO aboutRestDTO = new AboutRestDTO();
        aboutRestDTO.setChatId(chatId);
        if (language != null) {
            aboutRestDTO.setLanguage(language);
        } else {
            aboutRestDTO.setLanguage(Language.ru);
        }
        aboutRestDTO.setAddressru(a.getAddressru());
        aboutRestDTO.setAddresskz(a.getAddresskz());
        aboutRestDTO.setMonSt(a.getMonSt());
        aboutRestDTO.setMonEnd(a.getMonEnd());
        aboutRestDTO.setTueSt(a.getTueSt());
        aboutRestDTO.setTueEnd(a.getTueEnd());
        aboutRestDTO.setWedSt(a.getWedSt());
        aboutRestDTO.setWedEnd(a.getWedEnd());
        aboutRestDTO.setThurSt(a.getThurSt());
        aboutRestDTO.setThurEnd(a.getThurEnd());
        aboutRestDTO.setFriSt(a.getFriSt());
        aboutRestDTO.setFriEnd(a.getFriEnd());
        aboutRestDTO.setSatSt(a.getSatSt());
        aboutRestDTO.setSatEnd(a.getSatEnd());
        aboutRestDTO.setSunSt(a.getSunSt());
        aboutRestDTO.setSunEnd(a.getSunEnd());
        aboutRestDTO.setContact(a.getContact());
        aboutRestDTO.setUrlInst(a.getUrlInst());
        aboutRestDTO.setUrlWhats(a.getUrlWhats());

        return aboutRestDTO;
    }


    public static void setLanguage(long chatId, Language language) {
        User user = usersRepo.findByChatId(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
        }
        user.setLanguage(language);
        usersRepo.save(user);

    }
}
