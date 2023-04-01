Final Database 2 Project - Done with 2 other teamates

Steps: 
Open SQL Developer and connect to your account
1. Run setup.sql to setup database
2. Run main.sql to compile main package
3. Run App.java to launch the program
4. Run uninstall.sql to uninstall database (After usage)
NOTE ABOUT STEP 4: Run the drop triggers separately if you get errors that they do not exists by selecting the drops statement and pressing ctrl+enter

*NOTE*: Subcompetency requires an unique id that follows the format 0AXX (X can be whatever)

When running the program, the user will have to input s if they are a student or t if they are a teacher, and will then log in using their username and password.
The user will then be prompted will the following cases, which can be selected by inputting the corresponding number:

Cases:
1. Displays information about a course given an existing courseNumber.
2. Adds a new course to database. The user will enter all the fields of the new course, and will then be able to add any existing compentencies the course terminates and any existing subcompetencies the course covers.
3. Adds a new competency to database. The user will enter all the fields for the new competency, and will be able to either add  existing subcompetencies that the competency covers or create new subcompetencies that it covers.
4. The user  inputs an existing competency id. They will then be able to either add existing subcompetencies that the competency covers or create new subcompetencies that it covers.
5. The user inputs an existing courseNumber, and is prompted to rewrite all of its fields.
6. The user inputs an existing courseNumber, and can then add an existing Subcompetency that the course cover by inputting its Subcompetency id.
7. The user inputs an existing courseNumber, and then is prompted to enter existing competency id's that are terminated by that course.
8. The user inputs an existing competency id, and is prompted to rewrite all of its fields.
9. The user inputs an existing subcompetency id, and is prompted to rewrite all of its fields.
10. The user inputs an existing courseNumber; that course is deleted from the database (including bridging tables).
11. The user inputs an existing competency id; that competency is deleted from the database (including bridging tables).
12. The user inputs an existing subcompetency id; that subcompetency is deleted from the database (including bridging tables).
13. Displays the changelog to the user.
14. Ends the program, closing the connection.

Note: for the display course and changelog, you will have to scroll up to see them as the cases are then immediately displayed too, taking up the screen.
