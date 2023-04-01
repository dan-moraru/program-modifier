package com.mdm;

import java.sql.*;

public class Subcompetency implements SQLData{
    private static final String TYPE_NAME = "SUBCOMPETENCY_TYPE";
    private String subCompId;
    private String title;
    private String description;
    
    /**
     * Constructor
     * @param subCompId
     * @param title
     * @param description
     */
    public Subcompetency(String subCompId, String title, String description){
        this.subCompId = subCompId;
        this.title = title;
        this.description = description;
    }

    //Getters and setters
    public String getSubCompId() {
        return subCompId;
    }

    public void setSubCompId(String subCompId) {
        this.subCompId = subCompId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Object SQL
    @Override
    public String getSQLTypeName() throws SQLException {
        return TYPE_NAME;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException{
        setSubCompId(stream.readString());
        setTitle(stream.readString());
        setDescription(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeString(getSubCompId());       
        stream.writeString(getTitle());
        stream.writeString(getDescription());
    }
}