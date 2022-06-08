package me.duncanruns.duncafker;

public class DuncAFKerOptionsJson {
    public Integer interval;
    public Boolean doUse;

    public void ensure() {
        if (interval == null || interval < 1) {
            interval = 30;
        }
        if (doUse == null) {
            doUse = false;
        }
    }
}
