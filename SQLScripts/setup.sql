--Setup database

--Creating the tables
CREATE TABLE changelog(
    change_type CHAR(7) NOT NULL,
    altered_table CHAR(15) NOT NULL,
    log_id CHAR(10) NOT NULL,
    usern CHAR(10) NOT NULL,
    change_date timestamp NOT NULL
);

CREATE TABLE term(
    termId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    termName VARCHAR2(10) NOT NULL
);

CREATE TABLE course(
        courseNumber CHAR(10) PRIMARY KEY,
        termId NUMBER REFERENCES term(termId),
        name VARCHAR2(30) NOT NULL,
        description VARCHAR2(100) NOT NULL,
        classHours NUMBER(1) NOT NULL,
        labHours NUMBER(1) NOT NULL,
        homeworkHours NUMBER(1) NOT NULL,
        deptName VARCHAR2(50) NOT NULL
);

CREATE TABLE competency(
    compId CHAR(4) PRIMARY KEY,
    statement VARCHAR2(100) NOT NULL,
    dedicated_hours NUMBER(3) NOT NULL,
    type VARCHAR2(50) NOT NULL
);

CREATE TABLE subcompetency(
    subCompId CHAR(4) PRIMARY KEY,
    title VARCHAR2(100) NOT NULL,
    description VARCHAR2(500) NOT NULL
);

CREATE TABLE course_subcomp(
    courseNumber CHAR(10) REFERENCES course(courseNumber),
    subCompId CHAR(4) REFERENCES subcompetency(subCompId)
);

CREATE TABLE comp_subcomp(
    compId CHAR(4) REFERENCES competency(compId),
    subCompId CHAR(4) REFERENCES subcompetency(subCompId)
);

CREATE TABLE terminated_comp(
    courseNumber CHAR(10) REFERENCES course(courseNumber),
    compId CHAR(4) REFERENCES competency(compId)
);

--Inserting sample data
INSERT INTO term(termName)
VALUES('Term 1');
INSERT INTO term(termName)
VALUES('Term 2');
INSERT INTO term(termName)
VALUES('Term 3');
INSERT INTO term(termName)
VALUES('Term 4');
INSERT INTO term(termName)
VALUES('Term 5');
INSERT INTO term(termName)
VALUES('Term 6');

INSERT INTO competency(compId, statement, dedicated_hours, type)
VALUES('00Q2', 'Use Programming Languages', 171, 'Mandatory');
INSERT INTO subcompetency(subCompId, title, description)
VALUES('0AS1', 'Analyze the problem', 'Correct breakdown of the problem 
? Proper identification of input and output data and of the nature of the processes 
? Appropriate choice and adaptation of the algorithm');
INSERT INTO subcompetency(subCompId, title, description)
VALUES('0AS2', 'Translate the algorithm into a programming language', 'Appropriate choice of instructions and types of elementary data
? Efficient modularization of code
? Logical organization of instructions
? Compliance with the language syntax
? Computer code consistent with the algorithm');
INSERT INTO comp_subcomp(compId, subCompId)
VALUES('00Q2', '0AS1');
INSERT INTO comp_subcomp(compId, subCompId)
VALUES('00Q2', '0AS2');

INSERT INTO course(courseNumber, termId, name, description, classHours, labHours, homeworkHours, deptName)
VALUES('420-110-DW', 1, 'Programming 1', 'This course will introduce...', 3, 3, 3, 'Concentration');
INSERT INTO course_subcomp(courseNumber, subCompId)
VALUES('420-110-DW', '0AS2');

INSERT INTO course(courseNumber, termId, name, description, classHours, labHours, homeworkHours, deptName)
VALUES('420-210-DW', 2, 'Programming 2', 'This course will introduce...', 3, 3, 3, 'Concentration');
INSERT INTO course_subcomp(courseNumber, subCompId)
VALUES('420-210-DW', '0AS2');
INSERT INTO terminated_comp(courseNumber, compId)
VALUES('420-210-DW', '00Q2');

--Creating the COURSE_TYPE object
CREATE OR REPLACE TYPE COURSE_TYPE AS OBJECT(
    courseNumber CHAR(10),
    termId NUMBER(1),
    name VARCHAR2(30),
    description VARCHAR2(100),
    classHours NUMBER(1),
    labHours NUMBER(1),
    homeworkHours NUMBER(1),
    deptName VARCHAR2(50)
);
/

--Creating the SUBCOMPETENCY_TYPE object
CREATE OR REPLACE TYPE SUBCOMPETENCY_TYPE AS OBJECT(
    subCompId CHAR(4),
    title VARCHAR2(100),
    description VARCHAR2(500)
);

/
--Creating the COMPETENCY_TYPE object
CREATE OR REPLACE TYPE COMPETENCY_TYPE AS OBJECT(
    compId CHAR(4),
    statement VARCHAR2(100),
    dedicated_hours NUMBER(3),
    type VARCHAR2(50)
);
/

--Triggers
CREATE OR REPLACE TRIGGER course_log_insert_trigger
AFTER INSERT ON course
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('insert', 'course', :NEW.courseNumber, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER course_log_update_trigger
AFTER UPDATE ON course
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('update', 'course', :NEW.courseNumber, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER course_log_delete_trigger
BEFORE DELETE ON course
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('delete', 'course', :OLD.courseNumber, username, CURRENT_TIMESTAMP);
END;
/


CREATE OR REPLACE TRIGGER competency_log_insert_trigger
AFTER INSERT ON competency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('insert', 'competency', :NEW.compId, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER competency_log_update_trigger
AFTER UPDATE ON competency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('update', 'competency', :NEW.compId, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER competency_log_delete_trigger
BEFORE DELETE ON competency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('delete', 'competency', :OLD.compId, username, CURRENT_TIMESTAMP);
END;
/


CREATE OR REPLACE TRIGGER subcomp_log_insert_trigger
AFTER INSERT ON subcompetency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('insert', 'subcompetency', :NEW.subCompId, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER subcomp_log_update_trigger
AFTER UPDATE ON subcompetency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('update', 'subcompetency', :NEW.subCompId, username, CURRENT_TIMESTAMP);
END;
/

CREATE OR REPLACE TRIGGER subcomp_log_delete_trigger
BEFORE DELETE ON subcompetency
FOR EACH ROW
ENABLE 
DECLARE 
    username VARCHAR2(8);
BEGIN
    SELECT USER INTO username FROM dual;
    
    INSERT INTO changelog(change_type, altered_table, log_id, usern, change_date)
    VALUES('delete', 'subcompetency', :OLD.subCompId, username, CURRENT_TIMESTAMP);
END;
/