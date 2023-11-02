package edu.jsu.mcis.cs310.tas_fa23;

import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import static edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import edu.jsu.mcis.cs310.tas_fa23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_fa23.dao.EmployeeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.PunchDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.ShiftDAO;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit.*;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.LocalTime;
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
        LocalDateTime shiftstart = originalTimeStamp.toLocalDate().atTime(s.getShiftStart());
        LocalDateTime shiftstop = originalTimeStamp.toLocalDate().atTime(s.getShiftStop());
        LocalDateTime lunchstart = originalTimeStamp.toLocalDate().atTime(s.getLunchStart());
        LocalDateTime lunchstop = originalTimeStamp.toLocalDate().atTime(s.getLunchStop());
        int roundInterval = s.getRoundInterval();
        int gracePeriod = s.getGracePeriod();
        int dockPenalty = s.getDockPenalty();
        int minutes = originalTimeStamp.getMinute();
        int adjustedminute;
        LocalDateTime firstClockInDock=shiftstart.plusMinutes(roundInterval);
        LocalDateTime firstClockOutDock=shiftstop.minusMinutes(roundInterval);

        ////checks
        if (this.getPunchType() == CLOCK_IN) {
// interval round up to shift start
            if (MINUTES.between(originalTimeStamp, shiftstart) < roundInterval && originalTimeStamp.isBefore(shiftstart)) {
                adjustedTimeStamp = shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;
            }
            //Within Grace Period (round down to shift start
            else if (MINUTES.between(originalTimeStamp, shiftstart) < gracePeriod && originalTimeStamp.isAfter(shiftstart)) {
                adjustedTimeStamp = shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;
            } 
             
            //Clock in Dock Penalty for clocking in after graceperiod
            else if (MINUTES.between(originalTimeStamp, shiftstart) > gracePeriod && originalTimeStamp.isAfter(shiftstart)) {
                adjustedTimeStamp = shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
            } 
            //interval rounding UP to nearest increment
            else if (MINUTES.between(originalTimeStamp, shiftstart) > MINUTES.between(originalTimeStamp, shiftstart.plusMinutes(dockPenalty)) && (MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) >= roundInterval / 2) {
                adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                 shiftstart = originalTimeStamp.plusMinutes(adjustedminute); 
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
            //interval rounding down to nearest increment
            else if (MINUTES.between(originalTimeStamp, shiftstart) > MINUTES.between(originalTimeStamp, shiftstart.plusMinutes(dockPenalty)) && (MINUTES.between(originalTimeStamp, shiftstart) / roundInterval) < roundInterval / 2) {
                adjustedminute = Math.round(minutes / roundInterval) * roundInterval;
                shiftstart = originalTimeStamp.minusMinutes(adjustedminute);
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            } 
            //lunch clock in , needs fixing
            else if (MINUTES.between(originalTimeStamp, shiftstart) > (MINUTES.between(originalTimeStamp, lunchstart))) {

            }
            //no adjustment needed as it is either at the time of shift start or falls at an incremment of the round interval
            else if ((MINUTES.between(originalTimeStamp, shiftstart) % roundInterval == 0) && (originalTimeStamp.isAfter(shiftstart))) {
                originalTimeStamp = shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;
            }
            
        }
        else if (this.getPunchType() == CLOCK_OUT) {
            //interval Round down to shift stop
            if (MINUTES.between(originalTimeStamp, shiftstop) < gracePeriod && originalTimeStamp.isBefore(shiftstop)) {
                
                adjustedTimeStamp = shiftstop;
                adjustmentType = PunchAdjustmentType.SHIFT_STOP;
            }
            //within grace period round up to shiftstop
            else if((MINUTES.between(originalTimeStamp,shiftstop)<gracePeriod) && originalTimeStamp.isBefore(shiftstop)){
                adjustedTimeStamp=shiftstop;
                adjustmentType=PunchAdjustmentType.SHIFT_STOP;
            }
            //dock penalty round shiftstop the nearest 15 min interval directly under(this is to avoid having an issue where it will want to round up if it is just a minute behind the grace period but still close enough to want it to round up)
            else if ((MINUTES.between(originalTimeStamp,shiftstop)>gracePeriod) && (MINUTES.between(originalTimeStamp,shiftstop)<roundInterval)){
            adjustedTimeStamp=firstClockOutDock;
            adjustmentType=PunchAdjustmentType.SHIFT_DOCK;
            
            }
            //further interval round post the initial dock penalty
            
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(originalTimeStamp.format(formatter).toUpperCase());

        return s.toString();
    }

    //#28DC3FB8 CLOCK IN: FRI 09/07/2018 06:50:35
    //#28DC3FB8 CLOCK IN: FRI 09/07/2018 07:00:00 (Shift Start)
    public String printAdjusted() {
        StringBuilder s = new StringBuilder();

        s.append("#").append(badge.getId()).append(" ");
        s.append(punchType.toString()).append(": ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(adjustedTimeStamp.format(formatter).toUpperCase());
        s.append(" (").append(adjustmentType).append(")");

        return s.toString();
    }
}
