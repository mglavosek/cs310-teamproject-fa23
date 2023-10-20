
package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Punch {
    private final int terminalid;
    private int id;
    private final EventType punchType;
    private final Badge badge;
    private LocalDateTime originaltimestamp = null;
    private final LocalDateTime adjustedTimeStamp = null;
    private PunchAdjustmentType adjustmentType = null;
    
    
 public Punch(int terminalid, Badge badge, EventType punchType) {
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchType = punchType;
    }
 
 
  public Punch(int id, int terminalid, Badge badge, LocalDateTime originaltimestamp, EventType punchType) {
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.originaltimestamp = originaltimestamp;
        this.punchType = punchType;      
}
  public void adjust(Shift s){
        //variables
       
        LocalDateTime ots = originaltimestamp;
        LocalTime shiftstart = s.getShiftStart();
        LocalTime shiftstop = s.getShiftStop();
        LocalTime lunchstart = s.getLunchStart();
        LocalTime lunchstop = s.getLunchStop();
        int roundInterval = s.getRoundInterval();
        int gracePeriod = s.getGracePeriod();
        int dockPenalty = s.getDockPenalty();
        int minutesOver = ots.getMinute() % roundInterval;
        
        //Find what kind of shift it is , then
        
        boolean isWeekday = (ots.getDayOfWeek() != DayOfWeek.SATURDAY && ots.getDayOfWeek() != DayOfWeek.SUNDAY);
        boolean isNotTimeout = punchType != EventType.TIME_OUT;
        
        LocalDateTime shiftStart = ots.with(shiftstart);
        LocalDateTime shiftStartGraceBefore = shiftStart.minusMinutes(gracePeriod);
        LocalDateTime shiftStartGraceAfter = shiftStart.plusMinutes(gracePeriod);
        LocalDateTime shiftStop = ots.with(shiftstop);
        
        LocalDateTime shiftStopGrace = shiftStop.minusMinutes(gracePeriod);

        LocalDateTime shiftStopInterval = shiftStop.plusMinutes(roundInterval);

        LocalDateTime lunchStart = ots.with(lunchstart);
        LocalDateTime lunchStop = ots.with(lunchstop);

        LocalDateTime shiftStartDock = shiftStart.withMinute(shiftStart.getMinute()).plusMinutes(dockPenalty);
        LocalDateTime shiftStopDock = shiftStop.withMinute(shiftStop.getMinute()).minusMinutes(dockPenalty);
        
      
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
        return this.originaltimestamp;
    }
   
  public EventType getPunchType() {
        return this.punchType;
    }
  
  
   public String printOriginal() {
        StringBuilder s = new StringBuilder();
          s.append("#").append(badge.getId()).append(" ");
          s.append(punchType.toString()).append(": ");
          
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(originaltimestamp.format(formatter).toUpperCase());

        return s.toString();
    }
   
   public String printAdjusted() {
        StringBuilder s = new StringBuilder();

        s.append("#").append(badge.getId()).append(" ");
        s.append(punchType.toString()).append(": ");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(adjustedTimeStamp.format(formatter).toUpperCase());
        s.append()

        return s.toString();
   }
}
