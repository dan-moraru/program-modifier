package com.mdm;

import java.sql.*;

public class Course implements SQLData{
    private static final String TYPE_NAME = "COURSE_TYPE";
    private String courseNumber;
    private int termId;
    private String name;
    private String description;
    private int classHours;
    private int labHours;
    private int homeworkHours;
    private String deptName;

    /**
     * Constructor
     * @param courseNumber
     * @param termId
     * @param name
     * @param description
     * @param classHours
     * @param labHours
     * @param homeworkHours
     * @param deptName
     */
    public Course(String courseNumber, int termId, String name, String description, int classHours, int labHours,
            int homeworkHours, String deptName) {
        this.courseNumber = courseNumber;
        this.termId = termId;
        this.name = name;
        this.description = description;
        this.classHours = classHours;
        this.labHours = labHours;
        this.homeworkHours = homeworkHours;
        this.deptName = deptName;
    }


    //Serialization and deserialization
    @Override
    public String getSQLTypeName() throws SQLException {
        return TYPE_NAME;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        setCourseNumber(stream.readString());
        setTermId(stream.readInt());
        setName(stream.readString());
        setDescription(stream.readString());
        setClassHours(stream.readInt());
        setLabHours(stream.readInt());
        setHomeworkHours(stream.readInt());
        setDeptName(stream.readString());
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(getCourseNumber());
        stream.writeInt(getTermId());
        stream.writeString(getName());
        stream.writeString(getDescription());
        stream.writeInt(getClassHours());
        stream.writeInt(getLabHours());
        stream.writeInt(getHomeworkHours());
        stream.writeString(getDeptName());
    }

    //Getters and setters
    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClassHours() {
        return classHours;
    }

    public void setClassHours(int classHours) {
        this.classHours = classHours;
    }

    public int getLabHours() {
        return labHours;
    }

    public void setLabHours(int labHours) {
        this.labHours = labHours;
    }

    public int getHomeworkHours() {
        return homeworkHours;
    }

    public void setHomeworkHours(int homeworkHours) {
        this.homeworkHours = homeworkHours;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}

