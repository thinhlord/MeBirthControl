package com.meboo.birthcontrol;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by ngocbeo1121 on 3/10/16.
 */
public class BirthControlPill {

    public static final String TAG = "BirthControlPill";

    static final long DAY = 86400000;
    static final long CYCLE = 28 * DAY;

    public String name;

    // Remind time in day, in milliseconds since the beginning of the day
    public int remindTimeInDay;

    // Timestamp at the beginning of pill start date
    public long startDate;

    // The count of pills needed to take during a cycle
    public int pillDays;

    // The count of break days
    public int breakDays;

    // Whether placebo pills should be taken on irregular days
    public boolean shouldTakePlaceboPillsOnBreakDays;

    public BirthControlPill init(String name,
            int remindTimeInDay, int startDayInMonth, int startMonthInYear, int startYear,
            int pillDays, int breakDays, boolean shouldTakePlaceboPillsOnBreakDays){

        this.remindTimeInDay = remindTimeInDay;
        this.name = name;

        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.YEAR, startYear);
        startCal.set(Calendar.MONTH, startMonthInYear - 1);
        startCal.set(Calendar.DAY_OF_MONTH, startDayInMonth);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        startDate = startCal.getTimeInMillis();

        this.pillDays = pillDays;
        this.breakDays = breakDays;
        this.shouldTakePlaceboPillsOnBreakDays = shouldTakePlaceboPillsOnBreakDays;

        return this;
    }

    public NextRemind getNextRemindTime(){
        long now = System.currentTimeMillis();
        long pillCycleStart = startDate + remindTimeInDay;
        long durationSincePillCycleStart = now - pillCycleStart;

        long timeSinceLastPill = durationSincePillCycleStart % DAY;
        long timeLeft = DAY - timeSinceLastPill;
        long nextRemind = now + timeLeft;

        boolean inCycle = durationSincePillCycleStart <= CYCLE;

        if (inCycle){
            boolean isInPillDays = durationSincePillCycleStart <= pillDays * DAY;
            if (isInPillDays){
                return new NextRemind(nextRemind, false);
            }
            else {
                if (shouldTakePlaceboPillsOnBreakDays){
                    return new NextRemind(nextRemind, true);
                }
                else {
                    Log.d(TAG, "Over " + pillDays + " days of pills, and no placebo pills");
                    return null;
                }
            }
        }
        else {
            Log.d(TAG, "Over 28 days of normal cycle, no need to take pill until next cycle");
            return null;
        }
    }

}
