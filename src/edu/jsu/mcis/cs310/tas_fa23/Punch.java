
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
    private LocalDateTime adjustedTimeStamp = null;
    private PunchAdjustmentType adjustmentType = null;
    
    
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
  public void adjust(Shift s){
        LocalDateTime ots = originalTimeStamp;
        LocalDateTime shiftstart = originalTimeStamp.toLocalDate().atTime(s.getShiftStart());       
        LocalDateTime shiftstop = originalTimeStamp.toLocalDate().atTime(s.getShiftStop());
        LocalDateTime lunchstart = originalTimeStamp.toLocalDate().atTime(s.getLunchStart());
        LocalDateTime lunchstop = originalTimeStamp.toLocalDate().atTime(s.getLunchStop());
        int roundInterval = s.getRoundInterval();
        int gracePeriod = s.getGracePeriod();
        int dockPenalty = s.getDockPenalty();
        int minutesOver = ots.getMinute() % roundInterval;
           
        ////checks
        
        
        
        
        //turn everything into one unit using the minutes between and duration methods
        //format it back into the correct kind for the junittest.
        
     
        if(this.getPunchType() == CLOCK_IN){    
            
            //Within Grace Period
            if (MINUTES.between(originalTimeStamp,shiftstart)< gracePeriod && originalTimeStamp.isAfter(shiftstart)){
                //round down to shiftstart 
                adjustedTimeStamp=shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;                                                                                                                      }
            
            //intervalround up to shiftstart 
            else if (MINUTES.between(originalTimeStamp,shiftstart) < roundInterval && originalTimeStamp.isBefore(shiftstart)){
                // round up to shiftstart 
                adjustedTimeStamp=shiftstart;
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;    
            }
            
            //Clock in Dock Penalty for clocking in after graceperiod
            else if (MINUTES.between(originalTimeStamp,shiftstart)> gracePeriod && originalTimeStamp.isAfter(shiftstart)){
            
                adjustedTimeStamp=shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_START;                                                                               
            }
            
            //no Adjustments needed as it is within an increment set by the roundInterval
            else if ((MINUTES.between (originalTimeStamp,shiftstart)%roundInterval == 0) && (originalTimeStamp.isAfter(shiftstart))){                      originalTimeStamp=shiftstart;
                     adjustmentType = PunchAdjustmentType.SHIFT_START;
            }
            //no Adjustments needed as it is a correct clock in
            else if (originalTimeStamp==shiftstart){
                    originalTimeStamp=shiftstart;
                    
            }
            else if ((adjustmentType==PunchAdjustmentType.LUNCH_START) && MINUTES.between(originalTimeStamp,lunchstart)< roundInterval){
                
                
            }        
       
        } 
 
                
        else if (this.getPunchType()== CLOCK_OUT){
         if (MINUTES.between(originalTimeStamp,shiftstop)< gracePeriod && originalTimeStamp.isBefore(shiftstop)){
                //round up to shiftstop
                adjustedTimeStamp=shiftstop;
                adjustmentType = PunchAdjustmentType.SHIFT_STOP;                                                                                                                      }
            

            else if (MINUTES.between(originalTimeStamp,shiftstop) < roundInterval && originalTimeStamp.isAfter(shiftstop)){
                //interval round down to shiftstop
                adjustedTimeStamp=shiftstop;
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;    
            }
            
           
            else if (MINUTES.between(originalTimeStamp,shiftstop)> gracePeriod && originalTimeStamp.isBefore(shiftstop)){
                //Clock in Dock Penalty for clocking in after graceperiod
                adjustedTimeStamp=shiftstart;
                adjustmentType = PunchAdjustmentType.SHIFT_STOP;                                                                               
            }
            
            
            else if ((MINUTES.between (originalTimeStamp,shiftstop)%roundInterval == 0) && (originalTimeStamp.isBefore(shiftstop))){                    //no Adjustments needed as it is within an increment set by the roundInterval
                originalTimeStamp=shiftstop;
                adjustmentType = PunchAdjustmentType.SHIFT_STOP;
           
            }           
            else if (originalTimeStamp==shiftstop){
                //no Adjustments needed as it is a correct clock in
                originalTimeStamp=shiftstop;
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
