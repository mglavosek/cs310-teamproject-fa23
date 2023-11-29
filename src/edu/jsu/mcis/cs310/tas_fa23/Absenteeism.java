package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;



public class Absenteeism {

    private final Employee employee;
    private final LocalDate payPeriod;
    private final BigDecimal absenteeismPercentage;

    public Absenteeism(Employee employee, LocalDate payPeriod, BigDecimal absenteeismPercentage) {
        this.employee = employee;
        this.payPeriod = payPeriod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.absenteeismPercentage = absenteeismPercentage.setScale(2, RoundingMode.HALF_UP);
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getPayPeriod() {
        return payPeriod;
    }

    public BigDecimal getAbsenteeismPercentage() {
        return absenteeismPercentage;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("#").append(employee.getBadge().getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        str.append(" (Pay Period Starting ").append(payPeriod.format(formatter)).append("): ");
        str.append(absenteeismPercentage).append("%");
        return str.toString();
    }
}
