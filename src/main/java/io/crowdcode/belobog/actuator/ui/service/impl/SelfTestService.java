package io.crowdcode.belobog.actuator.ui.service.impl;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SelfTestService {

    private final Logger logger = LoggerFactory.getLogger(SelfTestService.class);

    Queue<ScheduledSelfTest> scheduledJob = new ConcurrentLinkedQueue<>();


    ExecutorService executor = Executors.newFixedThreadPool(1);


    @Autowired
    GPIOService gpioService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    DefaultGPIOEnforcerService defaultGPIOEnforcerService;

    @Value("${application.selftest.onStartup}")
    Boolean selftest;

    @PostConstruct
    public void postConstruct(){
        if (Boolean.TRUE.equals(selftest)) {
            logger.info("Scheduling a Self Test");

            ScheduledSelfTest test = new ScheduledSelfTest(gpioService, configurationService, defaultGPIOEnforcerService);
            scheduledJob.add(test);

            logger.info("DONE: Scheduling a Self Test");
        }
    }

    public void scheduleSelfTest(){
        ScheduledSelfTest test = new ScheduledSelfTest(gpioService, configurationService, defaultGPIOEnforcerService);
        scheduledJob.add(test);
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskUsingCronExpression() {
        logger.debug("Look for Self Test");

        if (!scheduledJob.isEmpty()) {
            logger.info("Launching Self Test");

            ScheduledSelfTest poll = scheduledJob.poll();
            executor.execute(poll);

        }


        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);
    }
}

class ScheduledSelfTest implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(ScheduledSelfTest.class);

    private final GPIOService gpioService;
    private final ConfigurationService configurationService;
    private final DefaultGPIOEnforcerService defaultGPIOEnforcerService;

    ScheduledSelfTest(GPIOService gpioService, ConfigurationService configurationService, DefaultGPIOEnforcerService defaultGPIOEnforcerService) {
        this.gpioService = gpioService;
        this.configurationService = configurationService;
        this.defaultGPIOEnforcerService = defaultGPIOEnforcerService;
    }

    @Override
    public void run() {
        logger.warn("STARTING SELF TEST");
        boolean failed=false;
        try {
            logger.warn("I AM ABOUT TO USE CLASS {} ", gpioService.toString());

            logger.warn("STEP 1 - DISABLE ALL SWITCHES");
            if (gpioService.disableAll()){
                logger.warn("STEP 1 - SUCCESSFULLY SET. PROBING SWITCHES");
                failed |= checkAllSlots(false, 1);
            } else {
                failed = true;
                logger.error("STEP 1 - FAILED");
            }
            Thread.sleep(1000);

            logger.warn("STEP 2 - ENABLE ALL SWITCHES");
            if (gpioService.enableAll()){
                logger.warn("STEP 2 - SUCCESSFULLY SET. PROBING SWITCHES");
                failed |= checkAllSlots(true, 2);
            } else {
                failed = true;
                logger.error("STEP 2 - FAILED");
            }
            Thread.sleep(1000);

            logger.warn("STEP 3 - PROBE INDIVIDUALS");
            if (gpioService.disableAll()){
                logger.warn("STEP 3 - SUCCESSFULLY SET. PROBING INDIVIDUALS");

                boolean[] active = configurationService.getActiveSlots();
                Integer[] pins = configurationService.getSlot2Pin();

                for (int i = 0; i < configurationService.numberOfSlots; i++){
                    String step = "STEP 3/" + i;
                    if (active[i]) {
                        logger.warn(step + " MAPPED TO " + pins[i] + " IS ACTIVE AND WILL BE PROBED");
                        if (gpioService.setEnabled(pins[i])) {
                            logger.warn(step + " MAPPED TO " + pins[i] + " DID THE JOB.");
                            if (gpioService.isGPIOEnabled(pins[i])) {
                                logger.warn(step + " MAPPED TO " + pins[i] + " IS ENABLED. ALL GOOD.");
                            } else {
                                failed = true;
                                logger.error(step +" IS NOT ENABLED! THIS MUST BE CHECKED");
                            }
                        } else {
                            failed = true;
                            logger.error(step +" DID NOT ENABLE! THIS MUST BE CHECKED");
                        }

                        if (gpioService.setDisabled(pins[i])) {
                            logger.warn(step + " MAPPED TO " + pins[i] + " DID THE JOB.");
                            if (!gpioService.isGPIOEnabled(pins[i])) {
                                logger.warn(step + " MAPPED TO " + pins[i] + " IS DISABLED. ALL GOOD.");
                            } else {
                                failed = true;
                                logger.error(step +" IS ENABLED! THIS MUST BE CHECKED");
                            }
                        } else {
                            failed = true;
                            logger.error(step +" DID NOT DISABLE! THIS MUST BE CHECKED");
                        }

                    } else {
                        logger.warn("STEP 3/"+i+" HAS BEEN DEACTIVATED AND WILL BE SKIPPED");
                    }

                    Thread.sleep(500);
                }

                logger.warn("STEP 4 - FINALLY DISABLE ALL SWITCHES");
                if (gpioService.disableAll()){
                    logger.warn("STEP 4 - SUCCESSFULLY SET. PROBING SWITCHES");
                    failed |= checkAllSlots(false, 4);
                } else {
                    failed = true;
                    logger.error("STEP 4 - FAILED");
                }

                logger.warn("STEP 5 - ENABLING THOSE WHICH ARE ENABLED BY DEFAULT");
                defaultGPIOEnforcerService.setDefaults();


            } else {
                logger.error("STEP 3 - FAILED");
            }
            Thread.sleep(1000);


            logger.warn("SELF TEST OVERALL RESULT: "+(failed ? "FAILURE":"SUCCESSFUL"));

        } catch (Exception e) {
            logger.error("SELF TEST HAS CAUSED AN EXCEPTION",e);
        } finally {
            logger.warn("DONE : SELF TEST");
        }



    }

    private boolean checkAllSlots(boolean expectedState, int step) throws InterruptedException {
        boolean[] activeSlots = configurationService.getActiveSlots();
        Integer[] pins = configurationService.getSlot2Pin();

        boolean failed=false;

        for (int i = 0; i < configurationService.getNumberOfSlots(); i++) {
            logger.warn("STEP "+step+" VERFICATION "+i);
            String slot = "SLOT " + (i + 1);
            if (activeSlots[i]) {
                logger.warn("STEP "+step+" SLOT "+slot + " IS ACTIVE AN MAPPED TO PIN " + pins[i]);
                boolean gpioEnabled = gpioService.isGPIOEnabled(pins[i]);
                logger.warn("STEP "+step+" SLOT "+slot+" HAS STATE "+gpioEnabled);
                if (gpioEnabled==expectedState) {
                    logger.warn("STEP "+step+" SLOT "+slot+" STATE IS "+expectedState+". ALL GOOD.");
                } else {
                    failed = true;
                    logger.error("STEP "+step+" SLOT "+slot+" STATE IS NOT "+expectedState+". THIS MUST BE CHECKED!.");
                }
            } else {
                logger.warn("STEP "+step+" SLOT "+slot+" HAS BEEN DEACTIVATED AND WON'T BE PROBED");
            }
            logger.warn("STEP "+step+" VERFICATION "+i);
            Thread.sleep(500);
        }
        return failed;
    }
}
