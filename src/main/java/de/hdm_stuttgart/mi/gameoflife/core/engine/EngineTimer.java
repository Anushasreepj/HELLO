package de.hdm_stuttgart.mi.gameoflife.core.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EngineTimer {
    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> timer;

    /**
     * Starts or Restarts the Interval
     * @param speed How many ms between intervals?
     */
    public void  startInterval(long speed, Runnable timerTask){
        shutdown();

        ses = Executors.newScheduledThreadPool(1);
        timer = ses.scheduleAtFixedRate(timerTask,0, speed, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the Interval
     */
    public void stopInterval(){
        shutdown();
    }

    private void shutdown(){
        if(timer != null) timer.cancel(false);

        ses.shutdown();

        try {
            ses.awaitTermination(4, TimeUnit.SECONDS);
        } catch (InterruptedException e){

        }
    }
}
