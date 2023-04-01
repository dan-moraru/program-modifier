--Main functions

/
CREATE OR REPLACE PACKAGE main AS
    FUNCTION getCourseInfo(cNum IN VARCHAR2) RETURN VARCHAR2;
    
    FUNCTION getLogsRow RETURN NUMBER;
    FUNCTION getLogs(num_row IN NUMBER) RETURN VARCHAR2;
    
    FUNCTION course_num_exists(cNum IN CHAR) RETURN CHAR;
    FUNCTION competency_num_exists(compNum IN CHAR) RETURN CHAR;
    FUNCTION subcompetency_num_exists(subCompNum IN CHAR) RETURN CHAR;
    
    PROCEDURE add_course(cType IN COURSE_TYPE);
    PROCEDURE add_competency(comType IN COMPETENCY_TYPE);
    PROCEDURE add_subcompetency(sType IN SUBCOMPETENCY_TYPE);
    
    PROCEDURE addTerminatedComp(cNum IN CHAR, cId IN CHAR);
    PROCEDURE addCourseSubComp(cNum IN CHAR, sId IN CHAR);
    PROCEDURE addCompSubcomp(cId IN CHAR, sId IN CHAR);
    
    PROCEDURE update_course(cType IN COURSE_TYPE);
    PROCEDURE update_competency(comType IN COMPETENCY_TYPE);
    PROCEDURE update_subcompetency(sType IN SUBCOMPETENCY_TYPE);

    PROCEDURE delete_course(cNum IN CHAR);
    PROCEDURE delete_competency(cId IN CHAR);
    PROCEDURE delete_subcompetency(scId IN CHAR);
END main;

/
CREATE OR REPLACE PACKAGE BODY main AS 

    --Procedure to display information
    FUNCTION getCourseInfo(cNum IN VARCHAR2) RETURN VARCHAR2 IS
        TYPE vType IS VARRAY(200) OF VARCHAR2(10);
        vRay vType;
        TYPE vcType IS VARRAY(200) OF VARCHAR2(10);
        vcRay vcType;
        all_columns_as_str VARCHAR2(1000);
        c_hours NUMBER(1);
        l_hours NUMBER(1);
        h_hours NUMBER(1);
        total_hours NUMBER(3);
        total_credits NUMBER(3);
        num_rowsT NUMBER(2);
        num_rowsC NUMBER(2);
        comIds VARCHAR2(100);
        subComIds VARCHAR2(100);
    BEGIN
        SELECT classHours, labHours, homeworkHours INTO c_hours, l_hours, h_hours FROM course
        WHERE courseNumber = cNum;
        
        total_hours := (c_hours + l_hours)*15;
        total_credits := ((c_hours + l_hours + h_hours)*15)/45;
        
        SELECT COUNT(*) INTO num_rowsT FROM terminated_comp
        WHERE courseNumber = cNum;
        
        IF num_rowsT > 0 THEN
            SELECT compId BULK COLLECT INTO vcRay FROM terminated_comp 
            WHERE courseNumber = cNum;
            
            FOR id IN 1 .. vcRay.count LOOP
                comIds := comIds || vcRay(id) || ', ';
            END LOOP;
        ELSE
            comIds := '0000';
        END IF;
        
        SELECT COUNT(*) INTO num_rowsT FROM course_subcomp
        WHERE courseNumber = cNum;
        
        IF num_rowsT > 0 THEN 
            SELECT subCompId BULK COLLECT INTO vRay FROM course_subcomp
            WHERE courseNumber = cNum;
            
            FOR id IN 1 .. vRay.count LOOP
                subComIds := subComIds || vRay(id) || ', ';
            END LOOP;
        ELSE
            subComIds := '0000';
        END IF;
    
        SELECT ('Course ID: ' || COURSENUMBER               || '$#%' ||
                'Course Term: ' ||      TERMNAME            || '$#%' || 
                'Course Name: ' || NAME                     || '$#%' || 
                'Course Description: ' || DESCRIPTION       || '$#%' || 
                'Course Class Hours: ' || CLASSHOURS        || '$#%' ||
                'Course Lab Hours: ' || LABHOURS            || '$#%' ||
                'Course Homework Hours: ' || HOMEWORKHOURS  || '$#%' ||
                'Course Type: ' || DEPTNAME                 || '$#%')
        INTO all_columns_as_str FROM term
        JOIN course USING(termid)
        WHERE courseNumber = cNum;
        all_columns_as_str := all_columns_as_str || 'Course Total Hours: ' || total_hours || '$#%' || 'Course Total Credits: ' || total_credits || '$#%';
        all_columns_as_str := all_columns_as_str || 'Terminated Competencies: ' || comIds || '$#%' || 'Course Sub-Competencies: ' || subComIds;
        RETURN all_columns_as_str;
    END;
    
    --Get number of rows in changeLog table
    FUNCTION getLogsRow RETURN NUMBER IS
        num_rows NUMBER(2);
    BEGIN
        SELECT COUNT(*) INTO num_rows FROM changeLog;
        RETURN num_rows;
    END;
    
    --Get the logs from changeLog table
    FUNCTION getLogs(num_row IN NUMBER) RETURN VARCHAR2 IS
        all_logs_as_str VARCHAR2(1000);
    BEGIN
        SELECT (CHANGE_TYPE     || '$#%' ||
            ALTERED_TABLE       || '$#%' ||
            LOG_ID              || '$#%' ||
            USERN               || '$#%' ||
            CHANGE_DATE)
        INTO all_logs_as_str FROM (SELECT ROWNUM R, changeLog.* FROM changeLog)
        WHERE R = num_row;
        RETURN all_logs_as_str;
    END;
    
    --Check if course number exists
    FUNCTION course_num_exists(cNum IN CHAR) RETURN CHAR IS
        num_rows NUMBER(1);
    BEGIN
        SELECT COUNT(*) INTO num_rows FROM course WHERE coursenumber = cNum;
        IF num_rows = 1 THEN
            RETURN '1';
        ELSE 
            RETURN '0';
        END IF;
    END;
    
    --Check if competency id exists
    FUNCTION competency_num_exists(compNum IN CHAR) RETURN CHAR IS
        num_rows NUMBER(1);
    BEGIN
        SELECT COUNT(*) INTO num_rows FROM competency WHERE compId = compNum;
        IF num_rows = 1 THEN
            RETURN '1';
        ELSE 
            RETURN '0';
        END IF;
    END;
    
    --Check if subcompetency id exists
    FUNCTION subcompetency_num_exists(subCompNum IN CHAR) RETURN CHAR IS
        num_rows NUMBER(1);
    BEGIN
        SELECT COUNT(*) INTO num_rows FROM subcompetency s WHERE s.subCompId = subCompNum;
        IF num_rows = 1 THEN
            RETURN '1';
        ELSE 
            RETURN '0';
        END IF;
    END;
    
    --Procedure to add course objects
    PROCEDURE add_course(cType IN COURSE_TYPE) IS
    BEGIN
        INSERT INTO course(courseNumber, termId, name, description, classHours, labHours, homeworkHours, deptName)
        VALUES(cType.courseNumber, cType.termId, cType.name, cType.description, cType.classHours, cType.labHours, cType.homeworkHours, cType.deptName);
    END;
    
    --Procedure to add competency objects
    PROCEDURE add_competency(comType IN COMPETENCY_TYPE) IS
    BEGIN
        INSERT INTO competency(compId, statement, dedicated_hours, type)
        VALUES(comType.compId, comType.statement, comType.dedicated_hours, comType.type);
    END;
    
    
    --Procedure to add sub_competency objects
    PROCEDURE add_subcompetency(sType IN SUBCOMPETENCY_TYPE) IS
    BEGIN
        INSERT INTO subcompetency(subCompId, title, description)
        VALUES(sType.subCompId, sType.title, sType.description);
    END;
    
    --Procedure to add terminated competency
    PROCEDURE addTerminatedComp(cNum IN CHAR, cId IN CHAR) IS
    BEGIN
        INSERT INTO terminated_comp(courseNumber, compId)
        VALUES(cNum, cId);
    END;
    
    --Procedure to add course_subcomp bridging table
    PROCEDURE addCourseSubComp(cNum IN CHAR, sId IN CHAR) IS
    BEGIN
        INSERT INTO course_subcomp(courseNumber, subCompId)
        VALUES(cNum, sId);
    END;
    
    --Procedure to add course_subcomp bridging table
    PROCEDURE addCompSubcomp(cId IN CHAR, sId IN CHAR) IS
    BEGIN
        INSERT INTO comp_subcomp(compId, subCompId)
        VALUES(cId, sId);
    END;
    
    --Procedure to update course
    PROCEDURE update_course(cType IN COURSE_TYPE) IS
    BEGIN
        UPDATE course SET
            name = cType.name,
            termId = cType.termId,
            description = cType.description,
            classHours = cType.classHours,
            labHours = cType.labHours,
            homeworkHours = cType.homeworkHours,
            deptName = cType.deptName
        WHERE courseNumber = cType.courseNumber;
    END;
    
    --Procedure to update competency
    PROCEDURE update_competency(comType IN COMPETENCY_TYPE) IS
    BEGIN    
        UPDATE competency SET 
            statement = comType.statement,
            dedicated_hours = comType.dedicated_hours,
            type = comType.type
        WHERE compId = comType.compId;
    END;
    
    --Procedure to update subcompetency
    PROCEDURE update_subcompetency(sType IN SUBCOMPETENCY_TYPE) IS
    BEGIN
        UPDATE subcompetency SET
            title = sType.title,
            description = sType.description
        WHERE subCompId = sType.subCompId;
    END;
    
    --Procedure to delete course
    PROCEDURE delete_course(cNum IN CHAR) IS
    BEGIN
        DELETE FROM course_subcomp WHERE courseNumber = cNum;
        DELETE FROM terminated_comp WHERE courseNumber = cNum;
        DELETE FROM course WHERE courseNumber = cNum;
    END;
    
    --Procedure to delete competency
    PROCEDURE delete_competency(cId IN CHAR) IS
    BEGIN
        DELETE FROM comp_subcomp WHERE compId = cId;
        DELETE FROM terminated_comp WHERE compId = cId;
        DELETE FROM competency WHERE compId = cId;
    END; 


    --Procedure to delete subcompetency
    PROCEDURE delete_subcompetency(scId IN CHAR) IS
    BEGIN 
        DELETE FROM comp_subcomp WHERE subCompId = scId;
        DELETE FROM course_subcomp WHERE subCompId = scId;
        DELETE FROM subcompetency WHERE subCompId = scId;
    END;
    
    
END main;
