package com.mdm;

import java.sql.*;

public class Term implements SQLData{
    private static final String TYPE_NAME = "TERM_TYPE";
    private String termName;

    /**
     * Constructor
     * @param termName
     */
    public Term(String termName){
        this.termName = termName;
    }

    public String toString(){
        return " Term: " + this.termName;
    }

    //Serialization and deserialization
    @Override
    public String getSQLTypeName() throws SQLException {
        return TYPE_NAME;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setTermName(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getTermName());
    }

    //Getters and setters
    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }
}

