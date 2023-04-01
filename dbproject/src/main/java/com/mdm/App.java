package com.mdm;

import java.io.Console;
import java.sql.*;
import java.util.regex.Pattern;

public class App 
{
    public static void main( String[] args ) throws SQLException
    {
        Console console = System.console();
        Connection connection = null;
        System.out.println("Hello, are you a student(s) or a teacher(t)?");
        String userType = console.readLine();
        while (userType == null || !Pattern.matches("^[st]{1}$", userType.toLowerCase())){
            System.out.println("Please enter 's' for student or 't' for teacher");
            userType = console.readLine();
        }
        userType = userType.toLowerCase();
        while (connection == null){        
            System.out.println("Enter your username");
            String username = "";
            
            //Get and validate username
            if (userType.equals("s")){
                while (username == null || !Pattern.matches("^A[0-9]{7}$", username)){
                    System.out.println("The username has to start with 'A' and be followed by 7 digits");
                    username = console.readLine();
                }
            } else {
                while (username == null || !Pattern.matches("^[a-zA-z]+$", username)){
                    System.out.println("The username can only contain letters");
                    username = console.readLine();
                }
            }
        
            //Get and validate password
            System.out.println("Enter your password");
            String password = String.valueOf(console.readPassword());
            while (password.equals("")) {
                System.out.println("The password can't be empty");
                password = String.valueOf(console.readPassword());
            }

            //Get connection
            CourseListServices c = new CourseListServices(username, password);  
            try {  
                connection = c.getConnection();

                mainLoop: while(true){
                    int option = 1;
                    System.out.println(" ");
                    System.out.println("What would you like to do?");
                    System.out.println("1 - Display course information");
                    System.out.println("2 - Add a new course to database");
                    System.out.println("3 - Add a new competency to database");
                    System.out.println("4 - Add a new or existing subcompetency to a competency");
                    System.out.println("5 - Update existing course information");
                    System.out.println("6 - Link an existing subcompetency to a course");
                    System.out.println("7 - Add a competency that a course terminates");
                    System.out.println("8 - Update existing competency");
                    System.out.println("9 - Update existing subcompetency");
                    System.out.println("10 - Delete exisiting course");
                    System.out.println("11 - Delete exisiting competency");
                    System.out.println("12 - Delete exisiting subcompetency");
                    System.out.println("13 - Display database logs");
                    System.out.println("14 - Exit program");

                    try{
                        option = Integer.parseInt(console.readLine());
                        switch(option){
                            case 1:
                                System.out.println("Enter the course number you wish to see:");

                                String courseNumToSearch = CourseValidation.validateCourseNumber(console);
                                while (!c.tableIdExists(connection, courseNumToSearch, "course")){
                                    System.out.println("This course number does not exist. Please try another one");
                                    courseNumToSearch = CourseValidation.validateCourseNumber(console);
                                }

                                String[] columns = c.displayCourse(connection, courseNumToSearch);
                                System.out.println("---------");
                                for (String col : columns){
                                    System.out.println(col);
                                }
                                System.out.println("---------");
                                break;
                            case 2:
                                System.out.println("Fill in the details to add a course to the database:");
 
                                String courseNumA = CourseValidation.validateCourseNumber(console);
                                while (c.tableIdExists(connection, courseNumA, "course")){
                                    System.out.println("This course number is already chosen. Please try another one");
                                    courseNumA = CourseValidation.validateCourseNumber(console);
                                }

                                int termId = Integer.parseInt(CourseValidation.validateTerm(console));                            
                                String nameA = CourseValidation.validateCourseName(console);                               
                                String descA = CourseValidation.validateCourseDescription(console);
                                int classHA = Integer.parseInt(CourseValidation.validateWorkHours(console, "class"));
                                int labHA = Integer.parseInt(CourseValidation.validateWorkHours(console, "lab"));                               
                                int homeHA = Integer.parseInt(CourseValidation.validateWorkHours(console, "homework"));
                                String courseTypeA = CourseValidation.validateCourseType(console);

                                Course addC = new Course(courseNumA, termId, nameA, descA, classHA, labHA, homeHA, courseTypeA);
                                c.addToDatabase(connection, addC, "Course");

                                
                                String terminatedC = CourseValidation.validateTerminated(console);
                                while (terminatedC.toLowerCase().equals("y")){
                                    String comIdT = CompetencyValidation.validateComId(console);
                                    while (!c.tableIdExists(connection, comIdT, "Competency")){
                                        System.out.println("The competency id does not exist. Please try another one");
                                        comIdT = CompetencyValidation.validateComId(console);
                                    }
                                    c.addTerminated(connection, addC.getCourseNumber(), comIdT);
                                    terminatedC = CourseValidation.validateTerminated(console);
                                }

                                do{
                                    System.out.println("Please enter the subcompetency id that the course covers");
                                    String subCompIdA = SubcompetencyValidation.validateSubCompId(console);
                                    while(!c.tableIdExists(connection, subCompIdA, "subcompetency")){
                                        System.out.println("This Sub-Competency id does not exist. Please enter an existing one");
                                        subCompIdA = SubcompetencyValidation.validateSubCompId(console);
                                    }
                                    c.addCourseSubComp(connection, courseNumA, subCompIdA);
                                    System.out.println("Do you want to add another subcompetency to the course (y/n)?");
                                }while(console.readLine().equals("y"));


                                break;
                            case 3:
                                System.out.println("Fill in the details to add a competency to the database:");

                                String comIdA = CompetencyValidation.validateComId(console);
                                while (c.tableIdExists(connection, comIdA, "competency")){
                                    System.out.println("This competency id is already chosen. Please try another one");
                                    comIdA = CompetencyValidation.validateComId(console);
                                }
                                String statementA = CompetencyValidation.validateStatement(console);
                                int dedHoursA = Integer.parseInt(CompetencyValidation.validateDedHours(console));
                                String typeA = CompetencyValidation.validateType(console);

                                Competency addCp = new Competency(comIdA, statementA, dedHoursA, typeA);
                                c.addToDatabase(connection, addCp, "Competency");

                                do{
                                    System.out.println("Enter either a new or existing Sub-Competency id that this competency covers:");
                                    String subCompIdL = SubcompetencyValidation.validateSubCompId(console);
                                    if (c.tableIdExists(connection, subCompIdL, "Subcompetency")){
                                        System.out.println("Existing subcompetency added to competency.");
                                        c.addCompSubcomp(connection, addCp.getComId(), subCompIdL);
                                    }
                                    else {
                                        System.out.println("Creating new subcompetency id:");
                                        String subCompTitle = SubcompetencyValidation.validateTitle(console);
                                        String subCompDesc = SubcompetencyValidation.validateDescription(console);
                                        Subcompetency addSubComp = new Subcompetency(subCompIdL, subCompTitle, subCompDesc);
                                        c.addToDatabase(connection, addSubComp, "Subcompetency");
                                        c.addCompSubcomp(connection, addCp.getComId(), subCompIdL);
                                    }
                                    System.out.println("Do you want to add another subcompetency to the competency (y/n)?");
                                }while(console.readLine().equals("y"));
                                
                                
                                break;

                            case 4:
                                System.out.println("Enter an existing competencyid you wish to add a subcompetency to.");
                                String comIdS = CompetencyValidation.validateComId(console);
                                while (c.tableIdExists(connection, comIdS, "competency") == false){
                                    System.out.println("This competency id does not exist. Please try an existing one");
                                    comIdS = CompetencyValidation.validateComId(console);
                                }
                                do{
                                    System.out.println("Enter either a new or existing Sub-Competency id that this competency covers:");
                                    String subCompIdL = SubcompetencyValidation.validateSubCompId(console);
                                    if (c.tableIdExists(connection, subCompIdL, "Subcompetency")){
                                        System.out.println("Existing subcompetency added to competency.");
                                        c.addCompSubcomp(connection, comIdS, subCompIdL);
                                    }
                                    else {
                                        System.out.println("Creating new subcompetency id:");
                                        String subCompTitle = SubcompetencyValidation.validateTitle(console);
                                        String subCompDesc = SubcompetencyValidation.validateDescription(console);
                                        Subcompetency addSubComp = new Subcompetency(subCompIdL, subCompTitle, subCompDesc);
                                        c.addToDatabase(connection, addSubComp, "Subcompetency");
                                        c.addCompSubcomp(connection, comIdS, subCompIdL);
                                    }
                                    System.out.println("Do you want to add another subcompetency to the competency (y/n)?");
                                }while(console.readLine().equals("y"));

                                break;

                            case 5:
                                System.out.println("Enter the course number you wish to update:");
                                
                                String courseNumToUpdate = CourseValidation.validateCourseNumber(console);
                                while (!c.tableIdExists(connection, courseNumToUpdate, "course")){
                                    System.out.println("This course number does not exist. Please try another one");
                                    courseNumToUpdate = CourseValidation.validateCourseNumber(console);
                                }
                                System.out.println("Fill in the details to update a course:");
                                int termIdU = Integer.parseInt(CourseValidation.validateTerm(console));                              
                                String nameU = CourseValidation.validateCourseName(console);                               
                                String descU = CourseValidation.validateCourseDescription(console);
                                int classHU = Integer.parseInt(CourseValidation.validateWorkHours(console, "class"));
                                int labHU = Integer.parseInt(CourseValidation.validateWorkHours(console, "lab"));                               
                                int homeHU = Integer.parseInt(CourseValidation.validateWorkHours(console, "homework"));
                                String courseTypeU = CourseValidation.validateCourseType(console);

                                Course updC = new Course(courseNumToUpdate, termIdU, nameU, descU, classHU, labHU, homeHU, courseTypeU);
                                
                                c.updateDatabase(connection, updC, "Course");
                                break;

                            case 6:
                                System.out.println("Enter the course number you wish to link a subcompetency to:");
                                    
                                String courseNumToLink = CourseValidation.validateCourseNumber(console);
                                while (!c.tableIdExists(connection, courseNumToLink, "course")){
                                    System.out.println("This course number does not exist. Please try another one");
                                    courseNumToLink  = CourseValidation.validateCourseNumber(console);
                                }
                                do{
                                    System.out.println("Enter an existing Sub-Competency id that this course covers:");
                                    String subCompIdC = SubcompetencyValidation.validateSubCompId(console);
                                    while (c.tableIdExists(connection, subCompIdC, "Subcompetency") == false){
                                        System.out.println("No existing subcompetency matches this id, enter an existing subcompetency");
                                        subCompIdC = SubcompetencyValidation.validateSubCompId(console);
                                    }
                                    
                                    c.addCourseSubComp(connection, courseNumToLink, subCompIdC);
                                    System.out.println("Do you want to link another subcompetency to the course (y/n)?");
                                }while(console.readLine().equals("y"));
                                
                                break;
                            
                            case 7:
                                System.out.println("Enter the course number that you wish to terminated a competency:");
                                        
                                String courseNumToTerminate = CourseValidation.validateCourseNumber(console);
                                while (!c.tableIdExists(connection, courseNumToTerminate, "course")){
                                    System.out.println("This course number does not exist. Please try another one");
                                    courseNumToTerminate  = CourseValidation.validateCourseNumber(console);
                                }
                                
                                String terminatedComp = CourseValidation.validateTerminated(console);
                                while (terminatedComp.equals("y")){
                                    String comIdT = CompetencyValidation.validateComId(console);
                                    while (!c.tableIdExists(connection, comIdT, "Competency")){
                                        System.out.println("The competency id was not found. Please try another one");
                                        comIdT = CompetencyValidation.validateComId(console);
                                    }
                                    c.addTerminated(connection, courseNumToTerminate, comIdT);
                                    System.out.println("Does this course terminate another competency (y/n)?");
                                    terminatedComp = console.readLine();
                                }
                                break;
                        
                            case 8:
                                System.out.println("Enter the competency id you wish to update:");

                                String comIdToUpdate = CompetencyValidation.validateComId(console);
                                while(!c.tableIdExists(connection, comIdToUpdate, "competency")){
                                    System.out.println("This competency id does not exist. Please try another one");
                                    comIdToUpdate  = CompetencyValidation.validateComId(console);
                                }

                                System.out.println("Fill in the details to update the competency:");

                                String statementU = CompetencyValidation.validateStatement(console);
                                int dedHoursU = Integer.parseInt(CompetencyValidation.validateDedHours(console));
                                String typeU = CompetencyValidation.validateType(console);

                                Competency updCp = new Competency(comIdToUpdate, statementU, dedHoursU, typeU);
                                c.updateDatabase(connection, updCp, "Competency");
                                break;

                            case 9:
                                System.out.println("Enter the subcompetency id you wish to update:");

                                String subComIdToUpdate = SubcompetencyValidation.validateSubCompId(console);
                                while(!c.tableIdExists(connection, subComIdToUpdate, "subcompetency")){
                                    System.out.println("This subcompetency id does not exist. Please try another one");
                                    subComIdToUpdate = SubcompetencyValidation.validateSubCompId(console);
                                }
                                
                                System.out.println("Fill in the details to update the subcompetency:");

                                String updatedTitle = SubcompetencyValidation.validateTitle(console);
                                String updatedDesc = SubcompetencyValidation.validateDescription(console);
                                Subcompetency updatedSubComp = new Subcompetency(subComIdToUpdate, updatedTitle, updatedDesc);
                                
                                c.updateDatabase(connection, updatedSubComp, "Subcompetency");
                                break;
                            
                            case 10:
                                System.out.println("Enter the course number you wish to delete:");
                                
                                String courseNumToDelete = CourseValidation.validateCourseNumber(console);
                                while (!c.tableIdExists(connection, courseNumToDelete, "course")){
                                    System.out.println("This course number does not exist. Please try another one");
                                    courseNumToDelete = CourseValidation.validateCourseNumber(console);
                                }

                                c.deleteDatabase(connection, courseNumToDelete, "Course");
                                break;

                            case 11:
                                System.out.println("Enter the competency id you wish to delete:");
                                
                                String comIdToDelete = CompetencyValidation.validateComId(console);

                                c.deleteDatabase(connection, comIdToDelete, "Competency");
                                break;

                            case 12:
                                System.out.println("Enter the subcompetency id you wish to delete:");
                                
                                String subComIdToDelete = SubcompetencyValidation.validateSubCompId(console);

                                c.deleteDatabase(connection, subComIdToDelete, "Subcompetency");
                                break;

                            case 13:
                                int num_rows = c.getLogRows(connection);
                                if (num_rows > 0){
                                    String[] labels = {"Change: " , "Table: ", "ID: ", "User: ", "Date: "};
                                    for (int i = 1; i <= num_rows; i++){
                                        String[] logsR = c.displayLogs(connection, i);
                                        for (int j = 0; j < logsR.length; j++){
                                            System.out.print(labels[j] + logsR[j] + " - ");
                                        }
                                        System.out.println("");
                                    } 
                                }
                                else{
                                    System.out.println("No Logs Found");
                                }
                                break;

                            case 14:
                                System.out.println("Exiting program...");                         
                                break mainLoop;
                            default:
                                System.out.println("Please enter a number between 1 and 14!");
                        }
                    }
                    catch(NumberFormatException e){
                        System.out.println("Please enter a number!");
                    }                                
                }

            } catch(SQLException e){
                System.out.println("Could not connect. Please make sure your crendentials are valid and you are on Dawson's network");
            } finally {
                CourseListServices.closeConnection(connection);
            }
        }
    }
}