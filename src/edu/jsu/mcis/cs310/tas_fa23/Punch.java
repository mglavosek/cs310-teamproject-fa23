package edu.jsu.mcis.cs310.tas_fa23;

import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import static edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Punch {

    private final Integer terminalid;
    private Integer id;
    private final EventType punchType;
    private final Badge badge;
    private LocalDateTime originalTimeStamp = null;
    private LocalDateTime adjustedTimeStamp = originalTimeStamp;
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
        LocalDateTime ots = originalTimeStamp;
        LocalDateTime shiftstart = originalTimeStamp.toLocalDate().atTime(s.getShiftStart()).withSecond(0).withNano(0);
        LocalDateTime shiftstop = originalTimeStamp.toLocalDate().atTime(s.getShiftStop()).withSecond(0).withNano(0);
        LocalDateTime lunchstart = originalTimeStamp.toLocalDate().atTime(s.getLunchStart()).withSecond(0).withNano(0);
        LocalDateTime lunchstop = originalTimeStamp.toLocalDate().atTime(s.getLunchStop()).withSecond(0).withNano(0);
        int roundInterval = s.getRoundInterval();
        int gracePeriod = s.getGracePeriod();
        int dockPenalty = s.getDockPenalty();
        int minutes = originalTimeStamp.getMinute();
        int adjustedminute;
        LocalDateTime firstClockInDock = shiftstart.plusMinutes(dockPenalty);
        LocalDateTime firstClockOutDock = shiftstop.minusMinutes(dockPenalty);

        ////checks
        if (this.getPunchType() == CLOCK_IN) {
            ///check if within Grace period (shift start)
            if (originalTimeStamp.isAfter(shiftstart) && originalTimeStamp.isBefore(shiftstart.plusMinutes(gracePeriod))) {
                adjustedTimeStamp = shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;

            } //check if within initial round interval (right) (dock penalty)
            else if (originalTimeStamp.isAfter(shiftstart.plusMinutes(gracePeriod)) && originalTimeStamp.isBefore(firstClockInDock)) {
                adjustedTimeStamp = firstClockInDock;
                adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
            } //checking for round interval before lunch start 
            // will need to go up/down rounding
            // maybe give a lunch grace so it will only interval round if it is not within 1 (graceperiod) or (round interval 
            else if (originalTimeStamp.isAfter(firstClockInDock) && originalTimeStamp.isBefore(lunchstart)) {
                //round up
                if ((MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) >= roundInterval / 2) {
                    adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                    adjustedTimeStamp = originalTimeStamp.plusMinutes(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;

                } //rounding down    
                else if ((MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) < roundInterval / 2) {
                    adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                    adjustedTimeStamp = originalTimeStamp.minusMinutes(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }
            } //check if it is lunch start (lunch start)
            else if (originalTimeStamp.isAfter(lunchstart) && originalTimeStamp.isBefore(lunchstop)) {
                adjustedTimeStamp = lunchstart;
                adjustmentType = PunchAdjustmentType.LUNCH_START;
            } //create a new set of rounding up and down intervals for after lunch ? (issue is that it is currently only applying rounding adjustments to timestamps BEFORE lunch , not after.
            else if (originalTimeStamp.isAfter(lunchstop) && originalTimeStamp.isBefore(shiftstop.minusMinutes(dockPenalty))) {
                //round up
                if ((MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) >= roundInterval / 2) {
                    adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                    adjustedTimeStamp = originalTimeStamp.plusMinutes(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;

                } //rounding down    
                else if ((MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) < roundInterval / 2) {
                    adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                    adjustedTimeStamp = originalTimeStamp.minusMinutes(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }

            } //if the time stamp does not apply to any set of conditionals
            else {
                if (originalTimeStamp.getSecond() >= 30) {
                    //rounding up to the nearest minute 
                    adjustedTimeStamp = originalTimeStamp.plusMinutes(1).withSecond(0).withNano(0);
                    adjustmentType = PunchAdjustmentType.NONE;
                } else if (originalTimeStamp.getSecond() < 30) {
                    //rounding down to the nearest minute
                    adjustedTimeStamp = originalTimeStamp.withSecond(0).withNano(0);
                    adjustmentType = PunchAdjustmentType.NONE;

                }

            }

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
