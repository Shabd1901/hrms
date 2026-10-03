-- PeopleOps HRMS - Database Creation Script
-- PostgreSQL Database Creation Command

CREATE DATABASE peopleops_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE peopleops_db
    IS 'Human Resource Management System (PeopleOps) Database';
