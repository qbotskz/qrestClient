package com.akimatBot.utils.timerTasks;

import com.akimatBot.utils.reports.FTPConnectionService;
import com.akimatBot.utils.reports.OrderReportDaily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
public class SendDailyReport extends TimerTask {

    @Autowired
    OrderReportDaily orderReportDaily;

    @Override
    public void run() {
        orderReportDaily.sendCompReport();
    }
}
