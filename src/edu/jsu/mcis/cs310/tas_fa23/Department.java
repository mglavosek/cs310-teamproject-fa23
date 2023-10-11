
package edu.jsu.mcis.cs310.tas_fa23;


public class Department {

    private final String description;
    private final int id, terminalId;

    public Department(int id, String description, int terminalId) {
        this.id = id;
        this.description = description;
        this.terminalId = terminalId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public int getTerminalID() {
        return terminalId;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append("), ");
        s.append("Terminal ID: ").append(terminalId);

        return s.toString();

    }

}