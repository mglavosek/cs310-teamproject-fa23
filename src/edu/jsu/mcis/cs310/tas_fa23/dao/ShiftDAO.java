package edu.jsu.mcis.cs310.tas_fa23.dao;


import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;


public class ShiftDAO {

    private static final String QUERY_FIND = "SELECT * FROM shift WHERE id = ?";
    
    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) 
    {
        this.daoFactory = daoFactory;
    }


    public Shift find(int id) {
        
        
        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);
                boolean hasresults = ps.execute();
                if (hasresults) {
                    
                    rs = ps.getResultSet();
                    
                    ResultSetMetaData rsmd;
                    rsmd = rs.getMetaData();      
                    int cols = rsmd.getColumnCount();

                    while (rs.next()) {
                        
                        HashMap<String, Object> shiftHash = new HashMap<>();
                        for(int i = 1; i<= cols;i++){
                            shiftHash.put(rsmd.getColumnLabel(i), rs.getString(i));
                        }
                        
                        shift = new Shift(shiftHash);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return shift;
    }
    
    
    
    
    public Shift find(Badge badge) {

        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            EmployeeDAO employeeDao = daoFactory.getEmployeeDAO();
            Employee employee = employeeDao.find(badge);
            shift = find(employee.getShift().getId());
            
            
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        } finally {
        }
        return shift;
    }
    
    
}
