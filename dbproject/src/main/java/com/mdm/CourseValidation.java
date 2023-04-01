package com.mdm;

import java.io.Console;
import java.util.regex.Pattern;


public class CourseValidation {
    /**
     * Validates course number
     * @param c
     * @return String
     */
    public static String validateCourseNumber(Console c){
        String courseNumber = null;
        do {
            System.out.println("Please enter the course number");
            courseNumber = c.readLine();
        } while(!courseNumberIsValid(courseNumber));
        return courseNumber;
    }
    public static boolean courseNumberIsValid(String courseNumber){
        if (courseNumber == null || courseNumber.equals("")){
            System.out.println("Course number can't be empty");
            return false;
        } else if (!Pattern.matches("^[0-9]{3}-[0-9A-Z]{3}-[0-9A-Z]{2}$", courseNumber)) {
            System.out.println("The course number must be in the format 999-(A|9)(A|9)(A|9)-(A|9)(A|9)");
            return false;
        }
        return true;
    }

    /**
     * Validates course term
     * @param c
     * @return String
     */
    public static String validateTerm(Console c){
        String term = null;
        do {
            System.out.println("Please enter the term of the course");
            term = c.readLine();
        } while(!termIsValid(term));
        return term;
    }
    public static boolean termIsValid(String term){
        if (term == null || term.equals("")){
            System.out.println("The term can't be empty");
            return false;
        } else if (!Pattern.matches("^[1-6]{1}$", term)){
            System.out.println("The term must be anumber between 1 and 6");
            return false;
        }
        return true;
    }

    /**
     * Validates course name
     * @param c
     * @return String
     */
    public static String validateCourseName(Console c){
        String courseName = null;
        do {
            System.out.println("Please enter the course name");
            courseName = c.readLine();
        } while(!courseNameIsValid(courseName));
        return courseName;
    }
    public static boolean courseNameIsValid(String courseName){
        if (courseName == null || courseName.equals("")){
            System.out.println("The course name can't be empty");
            return false;
        } else if (!Pattern.matches("^.{1,30}$", courseName)){
            System.out.println("The course name can only contain up to 30 symbols");
            return false;
        }
        return true;
    }

    /**
     * Validates course description
     * @param c
     * @return String
     */
    public static String validateCourseDescription(Console c){
        String courseDescription = null;
        do {
            System.out.println("Please enter the course description");
            courseDescription = c.readLine();
        } while(!courseDescriptionIsValid(courseDescription));
        return courseDescription;
    }
    public static boolean courseDescriptionIsValid(String courseDescription){
        if (courseDescription == null || courseDescription.equals("")){
            System.out.println("The course description can't be empty");
            return false;
        } else if (!Pattern.matches("^.{1,100}$", courseDescription)){
            System.out.println("The course description can only contain up to 100 symbols");
            return false;
        }
        return true;
    }

    /**
     * Validates course work hours based on type
     * @param c
     * @param type
     * @return String
     */
    public static String validateWorkHours(Console c, String type){
        String workHours = null;
        do {
            System.out.println("Enter the " + type + " hours per week");
            workHours = c.readLine();
        } while(!workHoursIsValid(workHours, type));
        return workHours;
    }
    public static boolean workHoursIsValid(String workHours, String type){
        if (workHours == null || workHours.equals("")){
            System.out.println("The " + type + "hours can't be empty");
            return false;
        } else if (!Pattern.matches("^[0-9]{1}$", workHours)){
            System.out.println("The " + type + " hours must be a number between 0 and 9");
            return false;
        }
        return true;
    }

    /**
     * Validates course type
     * @param c
     * @return String
     */
    public static String validateCourseType(Console c){
        String courseType = null;
        do {
            System.out.println("Enter the course type (concentration/general)");
            courseType = c.readLine();
        } while(!courseTypeIsValid(courseType));
        return courseType;
    }
    public static boolean courseTypeIsValid(String courseType){
        if (courseType == null || courseType.equals("")){
            System.out.println("The course type can't be empty");
            return false;
        } else if (!courseType.equals("concentration") && !courseType.equals("general")){
            System.out.println("The course type must be 'concentration' or 'general'");
            return false;
        }
        return true;
    }

    /**
     * Validates if course terminates a competency
     * @param c
     * @return String
     */
    public static String validateTerminated(Console c) {
        String terminated = null;
        do {
            System.out.println("Does this course terminate a competency? Enter (y/n):");
            terminated = c.readLine();
        } while(!courseTermination(terminated));
        return terminated;
    }
    public static boolean courseTermination(String terminated){
        if (terminated == null || terminated.equals("")){
            System.out.println("Please answer");
            return false;
        } else if (!Pattern.matches("^[y|n]{1}$", terminated)){
            System.out.println("Pleases answer with (y/n)");
            return false;
        }
        return true;
    }
}