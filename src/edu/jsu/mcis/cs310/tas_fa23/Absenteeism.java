package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Absenteeism {
    // Instance fields
    private int id;
    Employee employee;
    LocalDate payPeriodStartDate;
    BigDecimal absenteeismPercentage;

    // Constructor
   public Absenteeism(int id, Employee employee, LocalDate payPeriodStartDate, BigDecimal absenteeismPercentage) {
        this.id = id;
       this.employee = employee;
        this.payPeriodStartDate = payPeriodStartDate;
        this.absenteeismPercentage = absenteeismPercentage;
    }
    


    // Accessor methods
   
   public int getId(){
       return id; 
   }
   
   public Employee getEmployee() {
        return employee;
    }
  

   public LocalDate getPayPeriodStartDate() {
        return payPeriodStartDate;
    }

   public BigDecimal getAbsenteeismPercentage() {
        return absenteeismPercentage;
    }

    // Override toString method
    @Override
   public String toString() {
        return String.format("#%X (Pay Period Starting %s): %.2f%%", id, payPeriodStartDate, absenteeismPercentage);
    }

    public BigDecimal getPercentage() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
