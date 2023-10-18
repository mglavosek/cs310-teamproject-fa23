package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Shift {
    private LocalTime shiftStart;
    private LocalTime shiftStop;
    private int interval;
    private int gracePeriod;
    private int dock;
    private int id;
    private String description;
    private int lunchDeduct;
 

    // Constructors, getters, and setters..
    public Shift(int id, String description, int interval, int gracePeriod, int dock, int lunchDeduct) {
        this.id = id;
        this.description = description;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchDeduct = lunchDeduct;
}


    public LocalDateTime adjustClockInPunch(LocalDateTime punchTime) {
        long minutesDifference = ChronoUnit.MINUTES.between(punchTime, shiftStart);

        if (minutesDifference < -dock) {
            return punchTime.plusMinutes(dock);
        } else if (minutesDifference >= -dock && minutesDifference < -gracePeriod) {
            return shiftStart;
        } else if (minutesDifference >= -gracePeriod && minutesDifference < -interval) {
            return shiftStart;
        } else {
            return roundPunch(punchTime);
        }
    }

    public LocalDateTime adjustClockOutPunch(LocalDateTime punchTime) {
        long minutesDifference = ChronoUnit.MINUTES.between(punchTime, shiftStop);

        if (minutesDifference > dock) {
            return punchTime.minusMinutes(dock);
        } else if (minutesDifference <= dock && minutesDifference > gracePeriod) {
            return shiftStop;
        } else if (minutesDifference <= gracePeriod && minutesDifference > interval) {
            return shiftStop;
        } else {
            return roundPunch(punchTime);
        }
    }

    public LocalDateTime roundPunch(LocalDateTime punchTime) {
        // Implement rounding logic based on the interval
        return null;
        // Implement rounding logic based on the interval
    }
    @Override
    public String toString() {
        
        StringBuilder result = new StringBuilder();
        result.append("Shift " ).append(id).append(": ");
        result.append(shiftStart).append(" ");

        return result.toString();
    }
}

