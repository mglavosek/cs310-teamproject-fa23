
package edu.jsu.mcis.cs310.tas_fa23.dao;


import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.Absenteeism;
 
public class AbsenteeismDAO {
    private Connection conn;
    
    public AbsenteeismDAO(Connection conn) {
        this.conn = conn;
    }

}