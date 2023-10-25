
package edu.jsu.mcis.cs310.tas_fa23;

import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import static edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;

public class Punch {
    private final Integer terminalid;
    private Integer id;
    private final EventType punchType;
    private final Badge badge;
    private LocalDateTime originalTimeStamp = null;
    private final LocalDateTime adjustedTimeStamp = null;
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
  public void adjust(Shift shift){
        //variables
       LocalDateTime shiftStart = originalTimeStamp.toLocalDate().atTime(shift.getShiftStart());
       LocalDateTime shiftGrace = shiftStart.plusMinutes(shift.getGracePeriod());
       
        
        ////checks
        
        //compare clock in, check grace period
        if(this.getPunchType() == CLOCK_IN){
            
            if (shiftGrace){
                
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

        return s.toString();
   }
}
