package cse190.cookpal;


import android.os.CountDownTimer;

public class PausableCountdownTimer {
    private long timeremaining;
    private long countDownInterval;
    private CountDownTimer timer;
    private TimerHandler handler;

    public PausableCountdownTimer(long millisInFuture, long countDownInterval) {
        timeremaining = millisInFuture;
        this.countDownInterval = countDownInterval;
        timer =  new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                timeremaining = millisUntilFinished;
                PausableCountdownTimer.this.onTick(millisUntilFinished);
            }

            public void onFinish() {
                timeremaining = 0;
                PausableCountdownTimer.this.onFinish();
            }
        };
    }

    public synchronized void pause() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    public synchronized void resume() {
        if (timer == null){
            timer =  new CountDownTimer(timeremaining, countDownInterval) {
                public void onTick(long millisUntilFinished) {
                    timeremaining = millisUntilFinished;
                    PausableCountdownTimer.this.onTick(millisUntilFinished);
                }

                public void onFinish() {
                    timeremaining = 0;
                    PausableCountdownTimer.this.onFinish();
                }
            };
        }
    }

    public synchronized void cancel() {
        timer.cancel();
    }

    public synchronized void start() {
        timer.start();
    }

    public long getTimeRemaining() {
        return timeremaining;
    }

    public void onTick(long millisUntilFinished) {
        if (handler != null)
            handler.onTick(millisUntilFinished);
    }

    public void onFinish() {
        if (handler != null)
            handler.onFinish();
    }

    public void setHandler(TimerHandler handler) {
        this.handler = handler;
    }

    public static interface TimerHandler {
        public void onTick(long millisUntilFinished);
        public void onFinish();
    }
}
