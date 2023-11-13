package edu.jsu.mcis.cs310.tas_fa23;

import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import static edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
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

            }
              //check if it is an early clock in, round it up to normal clock in
            else if(originalTimeStamp.isBefore(shiftstart) && originalTimeStamp.isAfter(shiftstart.minusMinutes(roundInterval))){
                adjustedTimeStamp=shiftstart;
                adjustmentType=PunchAdjustmentType.SHIFT_START;
                
            }
            //check if within initial round interval (right) (dock penalty) (late clock in
            else if (originalTimeStamp.isAfter(shiftstart.plusMinutes(gracePeriod)) && originalTimeStamp.isBefore(firstClockInDock)) {
                adjustedTimeStamp = firstClockInDock;
                adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
            } 
//checking for round interval before lunch start 
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
            }
            //create a new set of rounding up and down intervals for after lunch ? (issue is that it is currently only applying rounding adjustments to timestamps BEFORE lunch , not after.
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
                
                //round up to lunch 
                else if (originalTimeStamp.isAfter(lunchstart.minusMinutes(roundInterval)) && originalTimeStamp.isBefore(lunchstart))  {
                    adjustedTimeStamp=lunchstart;
                    adjustmentType=PunchAdjustmentType.LUNCH_START;
                }
                //round down to lunch
                else if (originalTimeStamp.isAfter(lunchstart) && originalTimeStamp.isBefore(lunchstop)){
                    adjustedTimeStamp=lunchstart;
                    adjustmentType=PunchAdjustmentType.LUNCH_START;
                    
                }
             //if the time stamp does not apply to any set of conditionals
            else{
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

        if (this.getPunchType()==CLOCK_OUT){
                
            //check if within grace period (shift stop)
            if (originalTimeStamp.isBefore(shiftstop) && originalTimeStamp.isAfter(shiftstop.minusMinutes(gracePeriod))){
                adjustedTimeStamp=shiftstop;
                adjustmentType=PunchAdjustmentType.SHIFT_STOP;
                
            }
            //check if within interval round (left) (interval round)
            else if (originalTimeStamp.isBefore(shiftstop.plusMinutes(dockPenalty)) && originalTimeStamp.isAfter(shiftstop)){
               adjustedTimeStamp=shiftstop;
               adjustmentType=PunchAdjustmentType.INTERVAL_ROUND;
                System.err.println("wha wha 0");
            }
            //shift dock for early clock out (first initial dock)
            else if(originalTimeStamp.isBefore(shiftstop.minusMinutes(gracePeriod)) && originalTimeStamp.isAfter(firstClockOutDock)){
               adjustedTimeStamp=firstClockOutDock;
               adjustmentType=PunchAdjustmentType.SHIFT_DOCK;
            }
            //regular interval dock for between first clock out dock and lunchstop
            else if (originalTimeStamp.isAfter(lunchstop) && originalTimeStamp.isBefore(firstClockOutDock)){
                //check seconds first 
                if (originalTimeStamp.getSecond() >=30){
                   originalTimeStamp=originalTimeStamp.plusMinutes(1).withSecond(0); 
                }
                if (originalTimeStamp.getSecond()< 30){
                    originalTimeStamp=originalTimeStamp.withSecond(0);
                }
            // rounding up
            if (originalTimeStamp.getMinute()%roundInterval >= roundInterval/2){
                adjustedminute=originalTimeStamp.getMinute()/roundInterval;
                adjustedTimeStamp=originalTimeStamp.truncatedTo(ChronoUnit.HOURS).withMinute(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                    System.err.println(adjustedminute+"wha wha 1");
                
            }
            //rounding down
            else if (originalTimeStamp.getMinute()%roundInterval < roundInterval/2) {  
                    adjustedminute=originalTimeStamp.getMinute()/roundInterval;
                    adjustedTimeStamp= originalTimeStamp.truncatedTo(ChronoUnit.HOURS).withMinute(adjustedminute);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                    System.err.println(adjustedminute +"wha wha 2");
            }    
            
            }
        
                
                
                
                
                
                
                
           
            
                //late lunch stop
                else if(originalTimeStamp.isAfter(lunchstop) && originalTimeStamp.isBefore(lunchstop.plusMinutes(roundInterval))){
                      adjustedTimeStamp=lunchstop;
                      adjustmentType=PunchAdjustmentType.LUNCH_STOP;
                }
                //early lunch stop
                else if(originalTimeStamp.isBefore(lunchstop) && originalTimeStamp.isAfter(lunchstart.plusMinutes(roundInterval))){
                    adjustedTimeStamp=lunchstop;
                    adjustmentType=PunchAdjustmentType.LUNCH_STOP;
                }
           else{
                if (originalTimeStamp.getSecond() >= 30) {
                    //rounding up to the nearest minute 
                    adjustedTimeStamp = originalTimeStamp.plusMinutes(1).withSecond(0).withNano(0);
                    adjustmentType = PunchAdjustmentType.NONE;
                }
                else if (originalTimeStamp.getSecond() < 30) {
                    //rounding down to the nearest minute
                    adjustedTimeStamp = originalTimeStamp.withSecond(0).withNano(0);
                    adjustmentType = PunchAdjustmentType.NONE;

                }

            }
            
                    
            //checik if it is lunch stop (lunch stop)
            
// if none apply fix nanos and seconds(none)


            
            
        

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
