/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Daythyn
 */
public class Employee {
    
    //vars
    private int id;
    private EmployeeType employeeType;
    private String firstname,middlename,lastname;
    private Badge badge;
    private Department department;
    private Shift shift;
    private LocalDateTime active;
    private LocalDateTime inactive;//to be used in future feature
    
    
    
    //constructor
    public Employee(int id, Badge badge, String firstname, String middlename, String lastname, EmployeeType employeeType, Department department, Shift shift, LocalDateTime active){
        this.id = id;
        this.badge = badge;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.employeeType = employeeType;
        this.department = department;
        this.shift = shift;
        this.active = active;
        
        
    }
    
    
    //special methods


    //getter
    public int getId(){
        return id;
    }
    
    public String getFirstname(){
        return firstname;
    }
    
    public String getMiddlename(){
        return middlename;
    }
    
    public String getLastName(){
        return lastname;
    }
    
    public String getFullName(){
        String result;
        result = this.getFirstname() + " " + this.getMiddlename() + " " + this.getLastName();
        return result;
    }
    
    public LocalDateTime getActive(){
        return active;
    }
    
    public Badge getBadge(){
        return badge;
    }
    
    public Department getDepartment(){
        return department;
    }
    
    public Shift getShift(){
        return shift;
    }
    
    public EmployeeType getEmployeeType(){
        return employeeType;
    }

    
    //"ID #127: Elliott, Nancy L (#EC531DE6), Type: Temporary / Part-Time, Department: Shipping, Active: 09/22/2015"
    //toString Overide
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("ID #").append(id).append(": ");
        s.append(lastname).append(", ").append(firstname).append(" ").append(middlename);
        s.append(" (#").append(badge.getId()).append("), Type: ");
        s.append(employeeType.toString()).append(", Department: ").append(department.getDescription()).append(", Active: ");
        s.append(active.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        
        return s.toString();

    }
    
    
    //setter

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
