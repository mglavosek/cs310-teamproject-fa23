package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_fa23.dao.PunchDAO;
import java.time.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class PunchListRangeFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindPunchList1() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate dateStart = LocalDate.of(2018, Month.AUGUST, 1);
        LocalDate dateEnd = LocalDate.of(2018, Month.AUGUST, 8);

        Badge b = badgeDAO.find("67637925");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, dateStart,dateEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(183));
        p2.add(punchDAO.find(900));
        p2.add(punchDAO.find(287));
        p2.add(punchDAO.find(381));
        p2.add(punchDAO.find(493));
        p2.add(punchDAO.find(553));
        p2.add(punchDAO.find(632));
        p2.add(punchDAO.find(717));
        p2.add(punchDAO.find(719));
        p2.add(punchDAO.find(779));
        
        

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());
        //System.out.println(s2);
        //System.out.println(s1);

    }

    @Test
    public void testFindPunchList2() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate dateStart = LocalDate.of(2018, Month.AUGUST, 8);
        LocalDate dateEnd = LocalDate.of(2018, Month.AUGUST, 1);

        Badge b = badgeDAO.find("67637925");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, dateStart,dateEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        
        p2.add(punchDAO.find(183));
        p2.add(punchDAO.find(900));
        p2.add(punchDAO.find(287));
        p2.add(punchDAO.find(381));
        p2.add(punchDAO.find(493));
        p2.add(punchDAO.find(553));
        p2.add(punchDAO.find(632));
        p2.add(punchDAO.find(717));
        p2.add(punchDAO.find(719));
        p2.add(punchDAO.find(779));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testFindPunchList3() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate dateStart = LocalDate.of(2018, Month.SEPTEMBER, 10);

        Badge b = badgeDAO.find("95497F63");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, dateStart,dateStart);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(3916));
        p2.add(punchDAO.find(3965));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

}

