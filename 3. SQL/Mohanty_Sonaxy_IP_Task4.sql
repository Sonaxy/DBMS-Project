-- Task 4. 

--dropping indexes 
/*DROP INDEX IF EXISTS defect_prod_idx ON Defect;
DROP INDEX IF EXISTS request_idx ON Request;
DROP INDEX IF EXISTS produce_idx ON Produce;
DROP INDEX IF EXISTS certify_idx ON Certify;
DROP INDEX IF EXISTS accident_dt_idx ON Accident;
DROP INDEX IF EXISTS prod2_color_idx ON Product2;
DROP INDEX IF EXISTS prod_dt_idx ON Product;
DROP INDEX IF EXISTS salary_idx ON Employee;*/

-- dropping tables if already existing
/*DROP TABLE IF EXISTS Prod_Accident;
DROP TABLE IF EXISTS Repair_Accident;
DROP TABLE IF EXISTS Cost;
DROP TABLE IF EXISTS Defect;
DROP TABLE IF EXISTS Purchase;
DROP TABLE IF EXISTS Rep_Compl;
DROP TABLE IF EXISTS Request;
DROP TABLE IF EXISTS Produce;
DROP TABLE IF EXISTS Certify;
DROP TABLE IF EXISTS Repair;
DROP TABLE IF EXISTS Accident;
DROP TABLE IF EXISTS Complaint;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Prod3_Acc;
DROP TABLE IF EXISTS Prod2_Acc;
DROP TABLE IF EXISTS Prod1_Acc;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Product3;
DROP TABLE IF EXISTS Product2;
DROP TABLE IF EXISTS Product1;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Worker;
DROP TABLE IF EXISTS Quality_Controller;
DROP TABLE IF EXISTS Tech_Staff_Edu;
DROP TABLE IF EXISTS Tech_Staff;
DROP TABLE IF EXISTS Employee;*/


-- creating required tables and indexes

-- 1. Employee --
CREATE TABLE Employee (
    emp_name VARCHAR(64) PRIMARY KEY,
    emp_address VARCHAR(64) NOT NULL,
    emp_sal INTEGER NOT NULL
);

CREATE INDEX salary_idx ON Employee (emp_sal);

--ALTER TABLE  Employee ALTER COLUMN [salary] FLOAT NOT NULL;

/*INSERT INTO Employee VALUES
('Adams', 'OKC', 70000),
('Bethany', 'OKC', 70000),
('Codd', 'Stillwater', 60000),
('Daniels', 'Norman', 65000),
('Gordon', 'Austin', 80000),
('Smith', 'Austin', 80000),
('Tyler', 'Dallas', 75000),
('Will', 'Norman', 65000);*/

-- 2. Technical Staff --
CREATE TABLE Tech_Staff (
    ts_name VARCHAR(64) PRIMARY KEY,
    tech_position VARCHAR(64) NOT NULL,
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Tech_Staff VALUES
('Adams', 'Assembler'),
('Daniels', 'Welder'),
('Codd', 'IT');*/

-- 3. Technical Staff Degree Table --
CREATE TABLE Tech_Staff_Edu (
    ts_name VARCHAR(64),
    degree VARCHAR(64),
    PRIMARY KEY (ts_name, degree),
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Tech_Staff_Edu VALUES
('Adams', 'BS'),
('Adams', 'MS'),
('Daniels', 'BS'),
('Codd', 'BS'),
('Codd', 'PhD');*/

-- 4. Quality Controller Table --
CREATE TABLE Quality_Controller (
    qc_name VARCHAR(64) PRIMARY KEY,
    product_type VARCHAR(64) NOT NULL,
    FOREIGN KEY (qc_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Quality_Controller VALUES
('Daniels', 'Product 1'),
('Bethany', 'Product 3'),
('Smith', 'Product 2'),
('Gordon', 'Product 3'),
('Will', 'Product 3');*/

-- 5. Worker Table --
CREATE TABLE Worker (
    worker_name VARCHAR(64) PRIMARY KEY,
    max_produce INTEGER NOT NULL,
    FOREIGN KEY (worker_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Worker VALUES
('Smith', 8),
('Tyler', 9),
('Will', 8),
('Adams', 8);*/

-- 6. Product Table --
CREATE TABLE Product (
    product_ID INTEGER PRIMARY KEY,
    prod_date DATE NOT NULL,
    time_taken TIME(7) NOT NULL,
    worker_name VARCHAR(64) NOT NULL,
    qc_name VARCHAR(64) NOT NULL,
    ts_name VARCHAR(64),
    FOREIGN KEY (worker_name) REFERENCES Employee (emp_name),
    FOREIGN KEY (qc_name) REFERENCES Employee (emp_name),
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

CREATE INDEX prod_dt_idx ON Product (prod_date);

/*INSERT INTO Product (product_ID, prod_date, time_taken, worker_name, qc_name) VALUES
(101, '2008-11-11', '19:30:10', 'Smith', 'Daniels'),
(102, '2008-11-11', '10:30:10', 'Tyler', 'Bethany'),
(104, '2008-12-11', '08:30:00', 'Adams', 'Gordon'),
(106, '2009-10-01', '02:30:00', 'Adams', 'Bethany'),
(107, '2009-08-01', '02:30:00', 'Smith', 'Gordon'),
(108, '2009-08-01', '05:30:00', 'Tyler', 'Gordon');

INSERT INTO Product (product_ID, prod_date, time_taken, worker_name, qc_name, ts_name) VALUES
(103, '2008-11-11', '10:30:10', 'Will', 'Smith', 'Daniels'),
(105, '2009-02-11', '07:30:10', 'Adams', 'Gordon', 'Codd'),
(109, '2010-02-11', '05:30:10', 'Will', 'Gordon', 'Adams'),
(110, '2010-08-11', '23:30:10', 'Adams', 'Smith', 'Daniels');*/

-- 7. Product1 category Table --
CREATE TABLE Product1 (
    product_ID INTEGER PRIMARY KEY,
    size INTEGER NOT NULL,
    major_software VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID)
);

/*INSERT INTO Product1 VALUES
(101, 100, 'Python'),
(102, 100, 'Python');*/

-- 8. Product2 category Table --
CREATE TABLE Product2 (
    product_ID INTEGER PRIMARY KEY,
    size INTEGER NOT NULL,
    color VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID)
);

/*INSERT INTO Product2 VALUES
(103, 110, 'Black'),
(104, 120, 'Black'),
(105, 100, 'Red'),
(110, 120, 'Red');*/

CREATE INDEX prod2_color_idx ON Product2 (color);

-- 9. Product3 category Table --
CREATE TABLE Product3 (
    product_ID INTEGER PRIMARY KEY,
    size INTEGER NOT NULL,
    prod_weight INTEGER NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID)
);

/*INSERT INTO Product3 VALUES
(106, 110, 100),
(107, 120, 125),
(108, 100, 100),
(109, 100, 125);*/

-- 10. Account Table --
CREATE TABLE Account (
    account_no INTEGER PRIMARY KEY,
    est_date DATE NOT NULL
);

/*INSERT INTO Account VALUES
(00001, '2009-01-01'),
(00002, '2009-01-01'),
(00003, '2009-01-01'),
(00004, '2009-01-01'),
(00005, '2010-01-01'),
(00006, '2010-01-01'),
(00007, '2010-01-01'),
(00008, '2010-01-01'),
(00009, '2010-12-01'),
(00010, '2010-12-01');*/

-- 11. Product1-account category Table --
CREATE TABLE Prod1_Acc (
    account_no INTEGER PRIMARY KEY,
    cost_prod1 INTEGER NOT NULL,
    FOREIGN KEY (account_no) REFERENCES Account (account_no)
);

/*INSERT INTO Prod1_Acc VALUES
(00001, 500),
(00002, 500);*/

-- 12. Product2-account category Table --
CREATE TABLE Prod2_Acc (
    account_no INTEGER PRIMARY KEY,
    cost_prod2 INTEGER NOT NULL,
    FOREIGN KEY (account_no) REFERENCES Account (account_no)
);

/*INSERT INTO Prod2_Acc VALUES
(00003, 600),
(00004, 600),
(00005, 600),
(00010, 600);*/

-- 13. Product3-account category Table
CREATE TABLE Prod3_Acc (
    account_no INTEGER PRIMARY KEY,
    cost_prod3 INTEGER NOT NULL,
    FOREIGN KEY (account_no) REFERENCES Account (account_no)
);

/*INSERT INTO Prod3_Acc VALUES
(00006, 650),
(00007, 650),
(00008, 650),
(00009, 650);*/

-- 14. Customer Table --
CREATE TABLE Customer (
    cust_name VARCHAR(64) PRIMARY KEY,
    cust_address VARCHAR(64) NOT NULL
);

/*INSERT INTO Customer VALUES
('Johnson', 'Dallas'),
('Lynn', 'Dallas'),
('Black', 'Austin'),
('Jack', 'OKC'),
('Robert', 'OKC'),
('Zach', 'OKC');*/

-- 15. Complaint Table --
CREATE TABLE Complaint (
    complaint_ID INTEGER PRIMARY KEY,
    date_of_complaint DATE NOT NULL,
    complaint_desc VARCHAR(64) NOT NULL,
    treatment VARCHAR(64) NOT NULL
);

/*INSERT INTO Complaint VALUES
(001, '2009-05-01', 'Size Defect', 'Refund'),
(002, '2011-02-01', 'Color fade', 'Exchange'),
(003, '2011-01-03', 'Size Defect', 'Refund');*/

-- 16. Accident Table --
CREATE TABLE Accident (
    accident_no INTEGER PRIMARY KEY,
    accident_dt DATE NOT NULL,
    days_lost INTEGER NOT NULL
);

CREATE INDEX accident_dt_idx ON Accident (accident_dt);

/*INSERT INTO Accident VALUES
(1, '2009-06-04', 6),
(2, '2011-09-15', 10);*/

-- 17. Repair Table --
CREATE TABLE Repair (
    product_ID INTEGER PRIMARY KEY,
    ts_name VARCHAR(64) NOT NULL,
    repair_dt DATE NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Repair VALUES
(103, 'Daniels', '2009-05-31'),
(105, 'Codd', '2009-03-11'),
(109, 'Adams', '2010-03-11'),
(110, 'Daniels', '2011-09-13');*/

-- 18. Certify Table --
CREATE TABLE Certify (
    product_ID INTEGER PRIMARY KEY,
    qc_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (qc_name) REFERENCES Employee (emp_name) 
);

CREATE INDEX certify_idx ON Certify (qc_name);

/*INSERT INTO Certify VALUES
(101, 'Daniels'),
(102, 'Bethany'),
(103, 'Smith'),
(104, 'Gordon'),
(105, 'Gordon'),
(106, 'Bethany'),
(107, 'Gordon'),
(108, 'Gordon'),
(109, 'Gordon'),
(110, 'Smith');*/

-- 19. Produce Table --
CREATE TABLE Produce (
    product_ID INTEGER PRIMARY KEY,
    worker_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (worker_name) REFERENCES Employee (emp_name)
);

CREATE INDEX produce_idx ON Produce (worker_name);

/*INSERT INTO Produce VALUES
(101, 'Smith'),
(102, 'Tyler'),
(103, 'Will'),
(104, 'Adams'),
(105, 'Adams'),
(106, 'Adams'),
(107, 'Smith'),
(108, 'Tyler'),
(109, 'Will'),
(110, 'Adams');*/

-- 20. Request Table --
CREATE TABLE Request (
    product_ID INTEGER PRIMARY KEY,
    qc_name VARCHAR(64) NOT NULL,
    ts_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (qc_name) REFERENCES Employee (emp_name), 
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

CREATE INDEX request_idx ON Request (qc_name);

/*INSERT INTO Request VALUES
(105, 'Gordon', 'Codd'),
(109, 'Gordon', 'Adams');*/

-- 21. Repair due to complaint Table --
CREATE TABLE Rep_Compl (
    complaint_ID INTEGER PRIMARY KEY,
    product_ID INTEGER NOT NULL,
    ts_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (complaint_ID) REFERENCES Complaint (complaint_ID),
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name)
);

/*INSERT INTO Rep_Compl VALUES
(001, 103, 'Daniels'),
(002, 110, 'Daniels'),
(003, 109, 'Adams');*/

-- 22. Purchase Table --
CREATE TABLE Purchase (
    product_ID INTEGER PRIMARY KEY,
    cust_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (cust_name) REFERENCES Customer (cust_name)
);

/*INSERT INTO Purchase VALUES
(103, 'Black'),
(110, 'Robert'),
(109, 'Jack'),
(101, 'Jack'),
(106, 'Lynn');*/

-- 23. Defect Table --
CREATE TABLE Defect (
    complaint_ID INTEGER PRIMARY KEY,
    product_ID INTEGER NOT NULL,
    cust_name VARCHAR(64) NOT NULL,
    FOREIGN KEY (complaint_ID) REFERENCES Complaint (complaint_ID),
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (cust_name) REFERENCES Customer (cust_name)
);

CREATE INDEX defect_prod_idx ON Defect (product_ID);

/*INSERT INTO Defect VALUES 
(001, 103, 'Black'),
(002, 110, 'Robert'),
(003, 109, 'Jack');*/

-- 24. Cost Table --
CREATE TABLE Cost (
    product_ID INTEGER PRIMARY KEY,
    account_no INTEGER NOT NULL,
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID),
    FOREIGN KEY (account_no) REFERENCES Account (account_no)
);

/*INSERT INTO Cost VALUES
(101, 00001),
(102, 00002),
(103, 00003),
(104, 00004),
(105, 00005),
(106, 00006),
(107, 00007),
(108, 00008),
(109, 00009),
(110, 00010);*/

-- 25. Acident due to repair Table --
CREATE TABLE Repair_Accident (
    accident_no INTEGER,
    ts_name VARCHAR(64),
    product_ID INTEGER,
    PRIMARY KEY (accident_no, ts_name, product_ID),
    FOREIGN KEY (accident_no) REFERENCES Accident (accident_no),
    FOREIGN KEY (ts_name) REFERENCES Employee (emp_name),
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID)
);

/*INSERT INTO Repair_Accident VALUES
(1, 'Daniels', 103),
(2, 'Daniels', 110);*/

-- 26. Accident due to production Table --
CREATE TABLE Prod_Accident (
    accident_no INTEGER,
    worker_name VARCHAR(64),
    product_ID INTEGER,
    PRIMARY KEY (accident_no, worker_name, product_ID),
    FOREIGN KEY (accident_no) REFERENCES Accident (accident_no),
    FOREIGN KEY (worker_name) REFERENCES Employee (emp_name),
    FOREIGN KEY (product_ID) REFERENCES Product (product_ID)
);












