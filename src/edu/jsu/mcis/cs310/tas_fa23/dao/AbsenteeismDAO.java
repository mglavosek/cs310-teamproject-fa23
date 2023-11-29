package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Absenteeism;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AbsenteeismDAO {

    // SQL queries for finding, creating, and updating absenteeism records
    private static final String FIND_QUERY = "SELECT * FROM absenteeism WHERE employeeid = ? ";
    private static final String CREATE_QUERY = "INSERT INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE absenteeism SET payperiod = ?, percentage = ? WHERE employeeid = ?";

    private final DAOFactory daoFactory; // Factory for obtaining database connections

    AbsenteeismDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory; // Initialize the DAOFactory
    }

    // Find absenteeism record for a specific employee and pay period
    public Absenteeism find(Employee employee, LocalDate payPeriod) {
        Absenteeism absenteeism = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = daoFactory.getConnection();

            if (connection.isValid(0)) {
                preparedStatement = connection.prepareStatement(FIND_QUERY);
                preparedStatement.setInt(1, employee.getId());

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double percentage = resultSet.getDouble("percentage");
                    // Create Absenteeism object from database results
                    absenteeism = new Absenteeism(employee, payPeriod, BigDecimal.valueOf(percentage));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            closeResultSetAndStatement(resultSet, preparedStatement);
        }

        return absenteeism;
    }

    // Create or update absenteeism record in the database
    public void create(Absenteeism absenteeism) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = daoFactory.getConnection();

            if (connection.isValid(0)) {
                if (find(absenteeism.getEmployee(), absenteeism.getPayPeriod()) != null) {
                    // If the record already exists, update it
                    preparedStatement = connection.prepareStatement(UPDATE_QUERY);
                    preparedStatement.setDate(1, java.sql.Date.valueOf(absenteeism.getPayPeriod()));
                    preparedStatement.setDouble(2, absenteeism.getAbsenteeismPercentage().doubleValue());
                    preparedStatement.setInt(3, absenteeism.getEmployee().getId());
                } else {
                    // If the record does not exist, create a new one
                    preparedStatement = connection.prepareStatement(CREATE_QUERY);
                    preparedStatement.setInt(1, absenteeism.getEmployee().getId());
                    preparedStatement.setDate(2, java.sql.Date.valueOf(absenteeism.getPayPeriod()));
                    preparedStatement.setDouble(3, absenteeism.getAbsenteeismPercentage().doubleValue());
                }

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            closeResultSetAndStatement(resultSet, preparedStatement);
        }
    }

    // Helper method to close the ResultSet and PreparedStatement
    private void closeResultSetAndStatement(ResultSet resultSet, PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }
}
