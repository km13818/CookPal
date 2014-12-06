package cse190.cookpal;


import android.os.CountDownTimer;

public class PausableCountdownTimer {
    private long timeRemaining;
    private long countDownInterval;
    private CountDownTimer timer;
    private TimerHandler handler;
    private TimerState state;

    public PausableCountdownTimer(long millisInFuture, long countDownInterval) {
        timeRemaining = millisInFuture;
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
        if (timeRemaining != 0){
            timer =  new CountDownTimer(timeRemaining, countDownInterval) {
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
            timeRemaining = 0;
            state = TimerState.FINISHED;
        }
    }

    public synchronized void start() {
        if (timer != null) {
            timer.start();
        }
    }

    public synchronized void addTime(long millis) {
        timeRemaining += millis;
        timer =  new CountDownTimer(timeRemaining, countDownInterval) {
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
        return timeRemaining;
    }

    public synchronized TimerState getState() {
        return state;
    }

    public void onTick(long millisUntilFinished) {
        timeRemaining = millisUntilFinished;
        if (handler != null)
            handler.onTick(millisUntilFinished);
    }

    public void onFinish() {
        timeRemaining = 0;
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

    public String formatTimeRemaining() {
        long hours = (timeRemaining/1000)/3600;
        long minutes = ((timeRemaining/1000)/60)%60;
        long seconds = (timeRemaining/1000)%60;

        String hourStr, minStr, secStr;

        if(hours < 10) {
            hourStr = "0" + hours;
        } else {
            hourStr = String.valueOf(hours);
        }

        if(minutes < 10) {
            minStr = "0" + minutes;
        } else {
            minStr = String.valueOf(minutes);
        }

        if(seconds < 10) {
            secStr = "0" + seconds;
        } else {
            secStr = String.valueOf(seconds);
        }

        return hourStr + ":" + minStr + ":" + secStr;
    }
}
