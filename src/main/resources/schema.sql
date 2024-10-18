CREATE DATABASE IF NOT EXISTS sitter_dev;
USE sitter_dev;
CREATE TABLE IF NOT EXISTS parent (
                                      parent_id INT AUTO_INCREMENT PRIMARY KEY,
                                      parent_kidcount int
) AUTO_INCREMENT=2000;
CREATE TABLE IF NOT EXISTS sitter (
                                      sitter_id INT AUTO_INCREMENT PRIMARY KEY,
                                      sitter_profile varchar(1000)
) AUTO_INCREMENT=3000;
CREATE TABLE IF NOT EXISTS job (
                                   job_id INT AUTO_INCREMENT PRIMARY KEY,
                                   job_parent int,
                                   job_sitter int,
                                   job_details varchar(1000),
                                   job_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   job_lockindate DATETIME,
                                   job_start DATETIME,
                                   job_end DATETIME,
                                   job_pay decimal
) AUTO_INCREMENT=7000;
CREATE TABLE IF NOT EXISTS payment (
                                       payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id int,
                                       payment_method varchar(30),
                                       payment_number varchar(30)
);
CREATE TABLE IF NOT EXISTS user (
                                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    emailaddress varchar(100),
                                    user_phone varchar(10),
                                    user_fname varchar(30),
                                    user_lname varchar(30),
                                    user_address varchar(100),
                                    user_city varchar(50),
                                    user_zip varchar(5),
                                    parent_id int,
                                    sitter_id int,
                                    UNIQUE (emailaddress)
) AUTO_INCREMENT=1000;
CREATE TABLE IF NOT EXISTS chat (
                                    chat_id INT AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT=4000;
CREATE TABLE IF NOT EXISTS message (
                                       message_id INT AUTO_INCREMENT PRIMARY KEY,
                                       message_text text,
                                       message_time TIMESTAMP,
                                       chat_id int,
                                       user_id int
) AUTO_INCREMENT=5000;
CREATE TABLE IF NOT EXISTS userchat (
                                        userchat_id INT AUTO_INCREMENT PRIMARY KEY,
                                        user_id int,
                                        chat_id int
) AUTO_INCREMENT=6000;