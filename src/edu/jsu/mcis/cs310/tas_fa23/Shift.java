package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
    public Shift(int id, String description, LocalTime shiftStart, LocalTime shiftStop, int roundInterval, int gracePeriod, 
            int dockPenalty, LocalTime lunchStart, LocalTime lunchStop, int lunchThreshold) {
        
        this.id = id;
        this.description = description;
        this.shiftStart = shiftStart;
        this.shiftStop = shiftStop;
        this.roundInterval = roundInterval;
        this.gracePeriod = gracePeriod;
        this.dockPenalty = dockPenalty;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.lunchThreshhold = lunchThreshold;
}

    
    @Override
    public String toString() {
        
        StringBuilder result = new StringBuilder();
        result.append("Shift " ).append(id).append(": ");
        result.append(shiftStart).append(" ");

        return result.toString();
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



