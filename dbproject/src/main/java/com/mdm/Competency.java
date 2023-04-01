package com.mdm;

import java.sql.*;

public class Competency implements SQLData{
    private static final String TYPE_NAME = "COMPETENCY_TYPE";
    private String comId;
    private String statement;
    private int dedicatedHours;
    private String type;

    /**
     * Constructor
     * @param comId
     * @param statement
     * @param dedicatedHours
     * @param type
     */
    public Competency(String comId, String statement, int dedicatedHours, String type) {
        this.comId = comId;
        this.statement = statement;
        this.dedicatedHours = dedicatedHours;
        this.type = type;
    }
    
    //Getters and setters
    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getDedicatedHours() {
        return dedicatedHours;
    }

    public void setDedicatedHours(int dedicatedHours) {
        this.dedicatedHours = dedicatedHours;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //Object SQL
    @Override
    public String getSQLTypeName() throws SQLException {
        return TYPE_NAME;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setComId(stream.readString());
        setStatement(stream.readString());
        setDedicatedHours(stream.readInt());
        setType(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getComId());
        stream.writeString(getStatement());
        stream.writeInt(getDedicatedHours());
        stream.writeString(getType());
    }
}