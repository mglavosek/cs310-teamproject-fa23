package edu.jsu.mcis.cs310.tas_fa23;

import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import static edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Punch {

    private final Integer terminalid;
    private Integer id;
    private final EventType punchType;
    private final Badge badge;
    private LocalDateTime originalTimeStamp = null;
    private LocalDateTime adjustedTimeStamp = null;
    private PunchAdjustmentType adjustmentType = NONE;

    public Punch(Integer terminalid, Badge badge, EventType punchType) {
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchType = punchType;
        this.originalTimeStamp = LocalDateTime.now();
    }

    public Punch(int id, int terminalid, Badge badge, LocalDateTime originalTimeStamp, EventType punchType) {
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.originalTimeStamp = originalTimeStamp;
        this.punchType = punchType;
    }

    public void adjust(Shift s) {
        boolean isAdjusted = false;
        boolean isWeekend = false;
        int SShiftRoundInt = s.getRoundInterval();

        //Main Flags
        LocalDateTime SShiftStart = originalTimeStamp.toLocalDate().atTime(s.getShiftStart());
        LocalDateTime SShiftStop = originalTimeStamp.toLocalDate().atTime(s.getShiftStop());
        LocalDateTime SShiftLunchStart = originalTimeStamp.toLocalDate().atTime(s.getLunchStart());
        LocalDateTime SShiftLunchStop = originalTimeStamp.toLocalDate().atTime(s.getLunchStop());

        //Minor Flags
        LocalDateTime SShiftStartGrace = SShiftStart.plusMinutes(s.getGracePeriod());
        LocalDateTime SShiftStopGrace = SShiftStop.minusMinutes(s.getGracePeriod());

        LocalDateTime SShiftStartIntervalLeft = SShiftStart.minusMinutes(SShiftRoundInt);
        LocalDateTime SShiftStopIntervalRight = SShiftStop.plusMinutes(SShiftRoundInt);

        LocalDateTime SShiftStopIntervalLeft = SShiftStop.minusMinutes(s.getDockPenalty());
        LocalDateTime SShiftStartIntervalRight = SShiftStart.plusMinutes(s.getDockPenalty());

        if (adjustedTimeStamp == null) {
            adjustedTimeStamp = originalTimeStamp;
        }

        //Check clock in
            //check if left interval
            //check if grace period
            //check if right interval
            //check lunch
            
        //check clock out
            //check if right interval
            //check if grace period
            //check if left interval
            //check lunch
            
        //check whitspace evens
            //none
            
        //check whitespace odds
            //round up/down interval
            
        //throw error if still not adjusted
            //shouldn't happen
            
            
        if(originalTimeStamp.getDayOfWeek() == DayOfWeek.SATURDAY || originalTimeStamp.getDayOfWeek() == DayOfWeek.SUNDAY){
            adjustmentType = NONE;
            isAdjusted = true;
            isWeekend = true;
        }
            
        //check clock in
        if (isAdjusted == false && punchType == CLOCK_IN) {
            if (originalTimeStamp.isAfter(SShiftStartIntervalLeft) && originalTimeStamp.isBefore(SShiftStart)) {
                adjustedTimeStamp = SShiftStart;
                isAdjusted = true;
                adjustmentType = SHIFT_START;
            } else if (originalTimeStamp.isAfter(SShiftStart) && originalTimeStamp.isBefore(SShiftStartGrace)) {
                adjustedTimeStamp = SShiftStart;
                isAdjusted = true;
                adjustmentType = SHIFT_START;
            } else if (originalTimeStamp.isEqual(SShiftStartIntervalRight) || originalTimeStamp.isAfter(SShiftStartGrace) && originalTimeStamp.isBefore(SShiftStartIntervalRight)) {
                adjustedTimeStamp = SShiftStartIntervalRight;
                isAdjusted = true;
                adjustmentType = SHIFT_DOCK;
            } else if (originalTimeStamp.isAfter(SShiftLunchStart) && originalTimeStamp.isBefore(SShiftLunchStop)) {
                adjustedTimeStamp = SShiftLunchStop;
                isAdjusted = true;
                adjustmentType = LUNCH_STOP;
            }
        }

        //check clock out
        if (isAdjusted == false && (punchType == CLOCK_OUT || punchType == TIME_OUT)) {
            if (originalTimeStamp.isBefore(SShiftStopIntervalRight) && originalTimeStamp.isAfter(SShiftStop)) {
                adjustedTimeStamp = SShiftStop;
                isAdjusted = true;
                adjustmentType = SHIFT_STOP;
            } else if (originalTimeStamp.isBefore(SShiftStop) && originalTimeStamp.isAfter(SShiftStopGrace)) {
                adjustedTimeStamp = SShiftStop;
                isAdjusted = true;
                adjustmentType = SHIFT_STOP;
            } else if (originalTimeStamp.isEqual(SShiftStopIntervalLeft) || originalTimeStamp.isBefore(SShiftStopGrace) && originalTimeStamp.isAfter(SShiftStopIntervalLeft)) {
                adjustedTimeStamp = SShiftStopIntervalLeft;
                isAdjusted = true;
                adjustmentType = SHIFT_DOCK;
            } else if (originalTimeStamp.isAfter(SShiftLunchStart) && originalTimeStamp.isBefore(SShiftLunchStop)) {
                adjustedTimeStamp = SShiftLunchStart;
                isAdjusted = true;
                adjustmentType = LUNCH_START;
            }
        }

        //check whitespace evens
        //works only if round interval divides the minutes of an hour
        if ((isAdjusted == false || isWeekend == true) && originalTimeStamp.getMinute() % s.getRoundInterval() == 0) {
            adjustedTimeStamp = originalTimeStamp.withSecond(0).withNano(0);
            isAdjusted = true;
            adjustmentType = NONE;
        }

        //check whitespace odds
        
        if (isAdjusted == false || isWeekend == true) {
            int roundMultiplier = originalTimeStamp.getMinute() / SShiftRoundInt; //get lower end of round, +1 is upper end
            LocalDateTime SShiftIntervalRound = originalTimeStamp.withMinute((SShiftRoundInt * roundMultiplier) + (SShiftRoundInt / 2));
            
            if (originalTimeStamp.isBefore(SShiftIntervalRound)) {
                adjustedTimeStamp = originalTimeStamp.withMinute(0).plusMinutes(SShiftRoundInt * roundMultiplier);
                isAdjusted = true;
                adjustmentType = INTERVAL_ROUND;
            } else {
                adjustedTimeStamp = originalTimeStamp.withMinute(0).plusMinutes(SShiftRoundInt * (roundMultiplier + 1));
                isAdjusted = true;
                adjustmentType = INTERVAL_ROUND;
            }
            
            adjustedTimeStamp = adjustedTimeStamp.withSecond(0).withNano(0);
        }

        if (isAdjusted == false) {
            System.err.println("Something terrible has happened");
        }
    }

    public int getId() {
        return id;
    }

    public int getTerminalId() {
        return this.terminalid;
    }

    public Badge getBadge() {
        return this.badge;
    }

    public LocalDateTime getOriginalTimeStamp() {
        return this.originalTimeStamp;
    }

    public EventType getPunchType() {
        return this.punchType;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public LocalDateTime getAdjustedTimeStamp() {
        return adjustedTimeStamp;
    }

    public String printOriginal() {
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ");
        s.append(punchType.toString()).append(": ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        s.append(originalTimeStamp.format(formatter).toUpperCase());

        return s.toString();
    }

    //#28DC3FB8 CLOCK IN: FRI 09/07/2018 06:50:35
    //#28DC3FB8 CLOCK IN: FRI 09/07/2018 07:00:00 (Shift Start)
    public String printAdjusted() {
        StringBuilder s = new StringBuilder();

        s.append("#").append(badge.getId()).append(" ");
        s.append(punchType.toString()).append(": ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        s.append(adjustedTimeStamp.format(formatter).toUpperCase());
        s.append(" (").append(adjustmentType).append(")");

        return s.toString();
    }
}
