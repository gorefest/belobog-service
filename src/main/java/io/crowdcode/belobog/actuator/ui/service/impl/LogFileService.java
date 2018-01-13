package io.crowdcode.belobog.actuator.ui.service.impl;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class LogFileService {

    private static final Logger logger = LoggerFactory.getLogger(LogFileService.class);

    @Value("${logging.file}")
    private String logFile;

    Tailer tailer;

    Thread tailerThread;

    Queue<String> queue = new LinkedBlockingQueue();

    @PostConstruct
    public void postConstruct(){
        logger.info("LOG FILE {}", logFile);

        LogFileTailerListener logFileTailerListener = new LogFileTailerListener(this);
        tailer = new Tailer(new File(logFile), logFileTailerListener);
        tailerThread = new Thread(() -> tailer.run());

        tailerThread.start();

    }

    @Autowired
    private SimpMessagingTemplate template;

    public synchronized void pushLogs(String text) {
        queue.add(text);
    }

    @Scheduled(cron = "* * * * * *")
    public void scheduleTaskUsingCronExpression() {
        if (!queue.isEmpty()) {
            String[] entries = new String[queue.size()];
            int i = 0;
            while (!queue.isEmpty()) {
                entries[i++] =  queue.poll();
            }
            Arrays.sort(entries);

            for (String entry : entries) {
                this.template.convertAndSend("/topic/log", entry);
            }
        }
    }

}

class LogFileTailerListener implements TailerListener {

    private final LogFileService logFileService;

    LogFileTailerListener(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Override
    public void init(Tailer tailer) {

    }

    @Override
    public void fileNotFound() {

    }

    @Override
    public void fileRotated() {

    }

    @Override
    public void handle(String s) {
        logFileService.pushLogs(s);
    }

    @Override
    public void handle(Exception e) {

    }
}