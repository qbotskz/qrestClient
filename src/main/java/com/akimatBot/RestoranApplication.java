package com.akimatBot;

import com.akimatBot.config.Bot;
import com.akimatBot.utils.timerTasks.SendDailyReport;
import com.akimatBot.web.websocets.timerTasks.CheckCancelOrderItem;
import com.akimatBot.web.websocets.timerTasks.CheckPrintKitchen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
//@EnableJpaRepositories
//@Component
@Slf4j
public class RestoranApplication implements CommandLineRunner {

    public static Bot bot;
    @Value("${server.port}")
    static String port;
    @Autowired
    CheckPrintKitchen checkPrintKitchen;
    @Autowired
    CheckCancelOrderItem checkCancelOrderItem;

    @Autowired
    SendDailyReport sendDailyReport;

    public static void main(String[] args) {
        SpringApplication.run(RestoranApplication.class, args);
    }

    public static String getPort() {
        return port;
    }

    @Override
    public void run(String... args) throws Exception {

//        FTPConnectionService ftpConnectionService = new FTPConnectionService();
//        ftpConnectionService.connectInit();
//        ftpConnectionService.uploadFile();
//        ApiContextInitializer.init();
        log.info("ApiContextInitializer.InitNormal()");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        bot = new Bot();

        try {
            telegramBotsApi.registerBot(bot);
            log.info("Bot was registered: " + bot.getBotUsername());
        } catch (Exception e) {
            log.error("Error in main class", e);
        }


//        OrderReportDaily orderReportDaily = new OrderReportDaily();
//        orderReportDaily.sendCompReport();

        initTimers();
    }

    private void initTimers() {
//        Timer timer = new Timer(true);
//        timer.scheduleAtFixedRate(checkPrintKitchen, 0, 6000);


//        Timer timer1 = new Timer(true);
//        timer1.scheduleAtFixedRate(checkCancelOrderItem, 0, 6000);

//        Timer timerDailyReport = new Timer(true);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        timerDailyReport.scheduleAtFixedRate(sendDailyReport, calendar.getTime(), 1000*60*60*24);
    }


}
