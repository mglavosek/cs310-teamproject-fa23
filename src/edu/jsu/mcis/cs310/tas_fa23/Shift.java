package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Shift {
    private LocalTime shiftStart;
    private LocalTime shiftStop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private int roundInterval;
    private int gracePeriod;
    private int dockPenalty;
    private int id;
    private String description;
    private int lunchThreshhold;
 

    // Constructors, getters, and setters..
    public Shift(HashMap shiftHash) {
        
        this.id = Integer.parseInt((String)shiftHash.get("id"));
        this.description = (String)shiftHash.get("description");
        this.shiftStart = LocalTime.parse((CharSequence)shiftHash.get("shiftstart"));
        this.shiftStop = LocalTime.parse((CharSequence)shiftHash.get("shiftstop"));
        this.roundInterval = Integer.parseInt((String)shiftHash.get("roundinterval"));
        this.gracePeriod = Integer.parseInt((String)shiftHash.get("graceperiod"));
        this.dockPenalty = Integer.parseInt((String)shiftHash.get("dockpenalty"));
        this.lunchStart = LocalTime.parse((CharSequence)shiftHash.get("lunchstart"));
        this.lunchStop = LocalTime.parse((CharSequence)shiftHash.get("lunchstop"));
        this.lunchThreshhold = Integer.parseInt((String)shiftHash.get("lunchthreshold"));
}

    //"Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)"
    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        s.append(description).append(": ");
        s.append(shiftStart).append(" - ").append(shiftStop);
        s.append(" (").append(ChronoUnit.MINUTES.between(shiftStart, shiftStop)).append(" minutes); Lunch: ");
        s.append(lunchStart).append(" - ").append(lunchStop).append(" (");
        s.append(ChronoUnit.MINUTES.between(lunchStart, lunchStop)).append(" minutes)");

        return s.toString();
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public LocalTime getShiftStop() {
        return shiftStop;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }

    public int getRoundInterval() {
        return roundInterval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public int getDockPenalty() {
        return dockPenalty;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getLunchThreshhold() {
        return lunchThreshhold;
    }
}



