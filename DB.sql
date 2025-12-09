
-- Create Database
CREATE DATABASE IF NOT EXISTS StudentDB;
USE StudentDB;

-- Create Students Table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(150) NOT NULL
);

 
                                 