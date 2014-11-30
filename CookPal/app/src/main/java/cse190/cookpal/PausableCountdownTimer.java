package cse190.cookpal;


import android.os.CountDownTimer;

public abstract class PausableCountdownTimer {
    private long timeremaining;
    private long mcountDownInterval;
    private CountDownTimer timer;

    public PausableCountdownTimer(long millisInFuture, long countDownInterval) {
        timeremaining = millisInFuture;
        mcountDownInterval = countDownInterval;
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
            timer =  new CountDownTimer(timeremaining, mcountDownInterval) {
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

    public long getTimeRemaining(){
        return timeremaining;
    }

    public abstract void onTick(long millisUntilFinished);
    public abstract void onFinish();
}
