package com.mdm;

import java.sql.*;
import java.util.Map;

public class CourseListServices{
    private final String user;
    private final String pass;

    /**
     * Constructor
     * @param user
     * @param pass
     */
    public CourseListServices(String user, String pass){
        this.user = user;
        this.pass = pass;
    }

    /**
     * Gets connection to database 
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@198.168.52.211:1521/pdbora19c.dawsoncollege.qc.ca", this.user, this.pass);
        System.out.println("Connection successful");
        return con;
    }

    /**
     * Close database connection
     * @param conn
     * @throws SQLException
     */
    public static void closeConnection(Connection conn) throws SQLException{
        if (conn != null){
            conn.close();
            System.out.println("Connection closed");
        }
    }

    /**
     * Displays course information
     * @param conn
     * @param courseNumber
     * @return String array
     * @throws SQLException
     */
    public String[] displayCourse(Connection conn, String courseNumber) throws SQLException {
        try{
            String sql = "{? = call main.getCourseInfo(?)}";
            CallableStatement cs = conn.prepareCall(sql);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, courseNumber);
            cs.execute();
            String result = cs.getString(1);
            return result.split("\\$#%");
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the total amount of rows in table that keeps track of logs
     * @param conn
     * @return int
     */
    public int getLogRows(Connection conn){
        try{
            String sqlRow = "{? = call main.getLogsRow}";
            CallableStatement csr = conn.prepareCall(sqlRow);
            csr.registerOutParameter(1, Types.INTEGER);
            csr.execute();
            int num_row = csr.getInt(1);
            return num_row;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Displays the logs 
     * @param conn
     * @param num_row
     * @return String array
     */
    public String[] displayLogs(Connection conn, int num_row) {
        try{
            String sql = "{? = call main.getLogs(?)}";
            CallableStatement cs = conn.prepareCall(sql);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setInt(2, num_row);
            cs.execute();
            String result = cs.getString(1);
            return result.split("\\$#%");
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a primary key exists
     * @param conn
     * @param id
     * @param type table name
     * @return boolean
     */
    public boolean tableIdExists(Connection conn, String id, String type){
        try{
            String sql = "{? = call main." + type + "_num_exists(?)}";
            CallableStatement cs = conn.prepareCall(sql);
            cs.registerOutParameter(1, Types.CHAR);
            cs.setString(2, id);
            cs.execute();
            int idExists = cs.getInt(1);
            return idExists == 1 ? true : false;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds object to table
     * @param conn
     * @param o
     * @param type table name
     */
    public void addToDatabase(Connection conn, Object o, String type){
        try{
            Map<String, Class<?>> map = conn.getTypeMap();
            conn.setTypeMap(map);
            map.put(type.toUpperCase() + "_TYPE", Class.forName("com.mdm." + type));
            String sql = "{call main.add_" + type + "(?)}";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setObject(1, o);
            cs.execute();
            System.out.println(type + " was successfully added");
        } catch(SQLException e){
            e.printStackTrace();
        } catch(ClassNotFoundException cn){
            System.out.println(cn);
        }
    }

    /**
     * Updates object 
     * @param conn
     * @param o
     * @param type table name
     * @throws SQLException
     */
    public void updateDatabase(Connection conn, Object o, String type) throws SQLException {
        try {
            Map<String, Class<?>> map = conn.getTypeMap();
            conn.setTypeMap(map);
            map.put(type.toUpperCase() + "_TYPE", Class.forName("com.mdm." + type));
            String sql = "{call main.update_" + type + "(?)}";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setObject(1, o);
            cs.execute();
            System.out.println(type + " was successfully updated");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cn){
            System.out.println(cn);
        }
    }

    /**
     * Deletes object
     * @param conn
     * @param id
     * @param type table name
     * @throws SQLException
     */
    public void deleteDatabase(Connection conn, String id, String type) throws SQLException{
        try{
            CallableStatement cs = conn.prepareCall("call main.delete_" + type + "(?)");
            cs.setString(1, id);
            cs.execute();
            System.out.println("Deleted " + type + " " + id + " successfully");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds competency id and course number to determine what course terminates which competency
     * @param conn
     * @param courseNumber
     * @param comId
     * @throws SQLException
     */
    public void addTerminated(Connection conn, String courseNumber, String comId) throws SQLException{
        try {
            CallableStatement cs = conn.prepareCall("call main.addTerminatedComp(?, ?)");
            cs.setString(1, courseNumber);
            cs.setString(2, comId);
            cs.execute();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Adds to check what course covers which sub competency
     * @param conn
     * @param courseNumber
     * @param subCompId
     * @throws SQLException
     */
    public void addCourseSubComp(Connection conn, String courseNumber, String subCompId) throws SQLException{
        try {
            CallableStatement cs = conn.prepareCall("call main.addCourseSubComp(?, ?)");
            cs.setString(1, courseNumber);
            cs.setString(2, subCompId);
            cs.execute();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }
    
    /**
     * Adds to check what competency covers which subcompetency
     * @param conn
     * @param compId
     * @param subCompId
     * @throws SQLException
     */
    public void addCompSubcomp(Connection conn, String compId, String subCompId) throws SQLException{
        try {
            CallableStatement cs = conn.prepareCall("call main.addCompSubcomp(?, ?)");
            cs.setString(1, compId);
            cs.setString(2, subCompId);
            cs.execute();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }   
}
