package cse190.cookpal;


import android.os.CountDownTimer;

public class PausableCountdownTimer {
    private long timeremaining;
    private long countDownInterval;
    private CountDownTimer timer;
    private TimerHandler handler;
    private TimerState state;

    public PausableCountdownTimer(long millisInFuture, long countDownInterval) {
        timeremaining = millisInFuture;
        this.countDownInterval = countDownInterval;
        state = TimerState.PAUSED;
        timer =  new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                PausableCountdownTimer.this.onTick(millisUntilFinished);
            }

            public void onFinish() {
                PausableCountdownTimer.this.onFinish();
            }
        };
    }

    public synchronized void pause() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            state = TimerState.PAUSED;
        }
    }

    public synchronized void resume() {
        if (timeremaining != 0){
            timer =  new CountDownTimer(timeremaining, countDownInterval) {
                public void onTick(long millisUntilFinished) {
                    PausableCountdownTimer.this.onTick(millisUntilFinished);
                }

                public void onFinish() {
                    PausableCountdownTimer.this.onFinish();
                }
            };
            timer.start();
            state = TimerState.RUNNING;
        }
    }

    public synchronized void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            timeremaining = 0;
            state = TimerState.FINISHED;
        }
    }

    public synchronized void start() {
        if (timer != null) {
            timer.start();
        }
    }

    public synchronized void addTime(long millis) {
        timeremaining += millis;
        timer =  new CountDownTimer(timeremaining, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                PausableCountdownTimer.this.onTick(millisUntilFinished);
            }

            public void onFinish() {
                PausableCountdownTimer.this.onFinish();
            }
        };
        if (state == TimerState.RUNNING) {
            timer.start();
        }
    }

    public synchronized long getTimeRemaining() {
        return timeremaining;
    }

    public synchronized TimerState getState() {
        return state;
    }

    public void onTick(long millisUntilFinished) {
        timeremaining = millisUntilFinished;
        if (handler != null)
            handler.onTick(millisUntilFinished);
    }

    public void onFinish() {
        timeremaining = 0;
        timer = null;
        state = TimerState.FINISHED;
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

    public static enum TimerState {
        PAUSED, RUNNING, FINISHED
    }
}
