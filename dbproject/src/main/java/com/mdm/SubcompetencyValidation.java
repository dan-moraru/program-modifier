package com.mdm;

import java.io.Console;
import java.util.regex.Pattern;

public class SubcompetencyValidation {
    /**
     * Validates subcompetency id
     * @param c
     * @return String
     */
    public static String validateSubCompId(Console c){
        String subCompId = null;
        do {
            System.out.println("Please enter the Sub-Competency id: ('0AXX')");
            subCompId = c.readLine();
        } while(!subCompIdIsValid(subCompId));
        return subCompId;
    }
    public static boolean subCompIdIsValid(String subCompId){
        if (subCompId == null || subCompId.equals("")){
            System.out.println("Competency id can't be empty");
            return false;
        } else if (!Pattern.matches("^0A.{2}$", subCompId)){
            System.out.println("Competency id must consist of 4 symbols and start with 0A");
            return false;
        }
        return true;
    }

    /**
     * Validates subcompetency title
     * @param c
     * @return String
     */
    public static String validateTitle(Console c) {
        String title = null;
        do {
            System.out.println("Please enter the Sub-Competency title");
            title = c.readLine();
        } while(!titleIsValid(title));
        return title;
    }
    public static boolean titleIsValid(String title){
        if (title == null || title.equals("")){
            System.out.println("Title can't be empty");
            return false;
        } else if (!Pattern.matches("^.{1,100}$", title)){
            System.out.println("Title can contain up to 100 symbols");
            return false;
        }
        return true;
    }

    /**
     * Validates subcompetency description
     * @param c
     * @return String
     */
    public static String validateDescription(Console c) {
        String description = null;
        do {
            System.out.println("Please enter the Sub-Competency description");
            description = c.readLine();
        } while(!descriptionIsValid(description));
        return description;
    }
    public static boolean descriptionIsValid(String description){
        if (description == null || description.equals("")){
            System.out.println("Description can't be empty");
            return false;
        } else if (!Pattern.matches("^.{1,500}$", description)){
            System.out.println("Description can contain up to 500 symbols");
            return false;
        }
        return true;
    }
}
