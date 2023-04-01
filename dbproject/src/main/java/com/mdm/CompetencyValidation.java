package com.mdm;

import java.io.Console;
import java.util.regex.Pattern;

public class CompetencyValidation {
    /**
     * Validates competency id
     * @param c
     * @return String
     */
    public static String validateComId(Console c){
        String comId = null;
        do {
            System.out.println("Please enter the competency id");
            comId = c.readLine();
        } while(!comIdIsValid(comId));
        return comId;
    }
    public static boolean comIdIsValid(String comId){
        if (comId == null || comId.equals("")){
            System.out.println("Competency id can't be empty");
            return false;
        } else if (!Pattern.matches("^.{4}$", comId)){
            System.out.println("Competency id must consist of 4 symbols");
            return false;
        }
        return true;
    }

    /**
     * Validates competency statement
     * @param c
     * @return String
     */
    public static String validateStatement(Console c) {
        String statement = null;
        do {
            System.out.println("Please enter the competency statement");
            statement = c.readLine();
        } while(!statementIsValid(statement));
        return statement;
    }
    public static boolean statementIsValid(String statement){
        if (statement == null || statement.equals("")){
            System.out.println("Statement can't be empty");
            return false;
        } else if (!Pattern.matches("^.{1,100}$", statement)){
            System.out.println("Statement can contain up to 100 symbols");
            return false;
        }
        return true;
    }

    /**
     * Validates competency dedicated hours 
     * @param c
     * @return String
     */
    public static String validateDedHours(Console c) {
        String dedHours = null;
        do {
            System.out.println("Please enter the competency's dedicated hours");
            dedHours = c.readLine();
        } while(!dedHoursIsValid(dedHours));
        return dedHours;
    }
    public static boolean dedHoursIsValid(String dedHours){
        if (!Pattern.matches("^[1-9]{1}[0-9]{0,2}$", dedHours)){
            System.out.println("Hours can't be 0, negative or greater than 999");
            return false;
        }
        return true;
    }

    /**
     * Validates competency type
     * @param c
     * @return String
     */
    public static String validateType(Console c){
        String type = null;
        do {
            System.out.println("Please enter the competency type (Optional/Mandatory)");
            type = c.readLine();
        } while(!typeIsValid(type));
        return type;
    }
    public static boolean typeIsValid(String type){
        if (type == null || type.equals("")){
            System.out.println("Type can't be empty");
            return false;
        }
        else if (!Pattern.matches("^(optional|mandatory)$", type)){
            System.out.println("Type must be optional or mandatory");
            return false;
        }
        return true;
    }
}
