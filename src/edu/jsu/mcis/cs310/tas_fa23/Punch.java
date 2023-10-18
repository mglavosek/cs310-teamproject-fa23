
package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        Punch unadjustedPunch=null;
        EventType EventType=null;
        s = null;
        EventType AdjustmentType=null;
        
        //Find what kind of shift it is , then
        
      
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

        return s.toString();
   }
}
