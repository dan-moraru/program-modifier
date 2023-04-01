--Uninstall Database

--Dropping tables
DROP TABLE changelog;
DROP TABLE course_subcomp;
DROP TABLE comp_subcomp;
DROP TABLE terminated_comp;
DROP TABLE competency;
DROP TABLE subcompetency;
DROP TABLE course;
DROP TABLE term;

--Dropping Types
DROP TYPE COURSE_TYPE;
DROP TYPE COMPETENCY_TYPE;
DROP TYPE SUBCOMPETENCY_TYPE;

--Dropping Triggers
DROP TRIGGER course_log_insert_trigger;
DROP TRIGGER course_log_update_trigger;
DROP TRIGGER course_log_delete_trigger;
DROP TRIGGER competency_log_insert_trigger;
DROP TRIGGER competency_log_update_trigger;
DROP TRIGGER competency_log_delete_trigger;
DROP TRIGGER subcomp_log_insert_trigger;
DROP TRIGGER subcomp_log_update_trigger;
DROP TRIGGER subcomp_log_delete_trigger;

--Drop main package
DROP PACKAGE main;