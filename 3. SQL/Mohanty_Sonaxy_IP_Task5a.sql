/*DROP PROCEDURE IF EXISTS New_Employee;
DROP PROCEDURE IF EXISTS New_Employee_Tech;
DROP PROCEDURE IF EXISTS New_Employee_Worker;
DROP PROCEDURE IF EXISTS New_Employee_QC;
DROP PROCEDURE IF EXISTS New_Product_Repair;
DROP PROCEDURE IF EXISTS New_Certify;
DROP PROCEDURE IF EXISTS New_Request;
DROP PROCEDURE IF EXISTS New_Product1;
DROP PROCEDURE IF EXISTS New_Product2;
DROP PROCEDURE IF EXISTS New_Product3;
DROP PROCEDURE IF EXISTS New_Account1;
DROP PROCEDURE IF EXISTS New_Account2;
DROP PROCEDURE IF EXISTS New_Account3;
DROP PROCEDURE IF EXISTS New_Complaint;
DROP PROCEDURE IF EXISTS New_Accident;
*/

-- 1 --
CREATE PROCEDURE New_Employee
@emp_name VARCHAR(70),
@emp_address VARCHAR(70),
@salary REAL
AS
BEGIN
    INSERT INTO Employee VALUES (@emp_name, @emp_address, @salary);
END
GO

-- if the new employee is a technical staff
CREATE PROCEDURE New_Employee_Tech
@emp_name VARCHAR(70),
@tech_position VARCHAR(70),
@BS_ind INTEGER, --VARCHAR(5),
@MS_ind INTEGER,--VARCHAR(5),
@PhD_ind INTEGER--VARCHAR(5) 
AS
BEGIN 
	INSERT INTO Tech_Staff VALUES (@emp_name, @tech_position);
    IF @BS_ind = 1
		INSERT INTO Tech_Staff_Edu VALUES (@emp_name, 'BS');
    IF @MS_ind = 1
        INSERT INTO Tech_Staff_Edu VALUES (@emp_name, 'MS');
    IF @PhD_ind = 1
        INSERT INTO Tech_Staff_Edu VALUES (@emp_name, 'PhD');
         
END
GO

-- if the new employee is a quality controller
CREATE PROCEDURE New_Employee_QC
@emp_name VARCHAR(70),
@product_type VARCHAR(70)
AS
BEGIN 
	INSERT INTO Quality_Controller VALUES (@emp_name, @product_type);
         
END
GO

-- if the new employee is a worker
CREATE PROCEDURE New_Employee_Worker
@emp_name VARCHAR(70),
@max_produce VARCHAR(70)
AS
BEGIN 
	INSERT INTO Worker VALUES (@emp_name, @max_produce);
         
END
GO

-- 2 --
-- procedure for the product with any repair done to the product
CREATE PROCEDURE New_Product_Repair
@product_ID INTEGER,
@prod_date DATE,
@time_taken TIME(7),
@worker_name VARCHAR(64),
@qc_name VARCHAR(64),
@ts_name VARCHAR(64)
AS
BEGIN
   IF @ts_name IS NOT NULL
        INSERT INTO Product (product_ID, prod_date, time_taken, worker_name, qc_name, ts_name) 
        VALUES (@product_ID, @prod_date, @time_taken, @worker_name, @qc_name, @ts_name) ;
    /*ELSE
        INSERT INTO Product --(product_ID, prod_date, time_taken, worker_name, qc_name) 
        VALUES (@product_ID, @prod_date, @time_taken, @worker_name, @qc_name, NULL) ;*/
END
GO

-- insert query for the product with no repair 
INSERT INTO Product (product_ID, prod_date, time_taken, worker_name, qc_name) VALUES (?, ?, ?, ?, ?);

-- procedure to associate product with worker
CREATE PROCEDURE New_Produce
@product_ID INTEGER,
@worker_name VARCHAR(64)
AS
BEGIN
    INSERT INTO Produce VALUES (@product_ID, @worker_name);
END
GO

-- procedure to associate product with quality controller
CREATE PROCEDURE New_Certify
@product_ID INTEGER,
@qc_name VARCHAR(64)
AS
BEGIN
    INSERT INTO Certify VALUES (@product_ID, @qc_name);
END
GO

-- insert statement for repair
INSERT INTO Repair VALUES (?, ?, ?);

-- procedure for associating product with quality controller and technical staff 
-- in case any request is done by quality controller for the repair
CREATE PROCEDURE New_Request
@product_ID INTEGER,
@qc_name VARCHAR(64),
@ts_name VARCHAR(64)
AS
BEGIN
    INSERT INTO Request VALUES (@product_ID, @qc_name, @ts_name);
END
GO

-- procedure to insert product type 1 details
CREATE PROCEDURE New_Product1
@product_ID INTEGER,
@size INTEGER,
@major_software VARCHAR(64)
AS
BEGIN
    INSERT INTO Product1 VALUES (@product_ID, @size, @major_software);
END
GO

-- procedure to insert product type 2 details
CREATE PROCEDURE New_Product2
@product_ID INTEGER,
@size INTEGER,
@color VARCHAR(64)
AS
BEGIN
    INSERT INTO Product2 VALUES (@product_ID, @size, @color);
END
GO

-- procedure to insert product type 3 details
CREATE PROCEDURE New_Product3
@product_ID INTEGER,
@size INTEGER,
@prod_weight INTEGER
AS
BEGIN
    INSERT INTO Product3 VALUES (@product_ID, @size, @prod_weight);
END
GO

-- 3 --
INSERT INTO Customer VALUES (?, ?);
INSERT INTO Purchase VALUES (?, ?);

-- 4 -- 
-- procedure to insert account type 1 details
CREATE PROCEDURE New_Account1
@account_no INTEGER,
@est_date DATE,
@cost REAL,
@product_ID INTEGER
AS
BEGIN 
    INSERT INTO Account VALUES (@account_no, @est_date);
    DECLARE @pid INTEGER;
    SET @pid = (SELECT p.product_ID from Product1 p where p.product_ID = @product_ID);
    IF @pid = @product_ID
        INSERT INTO Prod1_Acc VALUES (@account_no, @cost);
        INSERT INTO Cost VALUES (@product_ID, @account_no);
    IF @pid != @product_ID
        DECLARE @Msg VARCHAR(300);
        SET @Msg = 'Product type mismatch';
        PRINT @Msg;
END
GO

-- procedure to insert account type 2 details
CREATE PROCEDURE New_Account2
@account_no INTEGER,
@est_date DATE,
@cost REAL,
@product_ID INTEGER
AS
BEGIN
    INSERT INTO Account VALUES (@account_no, @est_date);
    DECLARE @pid INTEGER;
    SET @pid = (SELECT p.product_ID from Product2 p where p.product_ID = @product_ID);
    IF @pid = @product_ID
        INSERT INTO Prod2_Acc VALUES (@account_no, @cost);
        INSERT INTO Cost VALUES (@product_ID, @account_no);
    IF @pid != @product_ID
        DECLARE @Msg VARCHAR(300);
        SET @Msg = 'Product type mismatch';
        PRINT @Msg;
END
GO

-- procedure to insert account type 3 details
CREATE PROCEDURE New_Account3
@account_no INTEGER,
@est_date DATE,
@cost REAL,
@product_ID INTEGER
AS
BEGIN
    INSERT INTO Account VALUES (@account_no, @est_date);
    DECLARE @pid INTEGER;
    SET @pid = (SELECT p.product_ID from Product3 p where p.product_ID = @product_ID);
    IF @pid = @product_ID
        INSERT INTO Prod3_Acc VALUES (@account_no, @cost);
        INSERT INTO Cost VALUES (@product_ID, @account_no);
    IF @pid != @product_ID
        DECLARE @Msg VARCHAR(300);
        SET @Msg = 'Product type mismatch';
        PRINT @Msg;
END
GO

-- 5 --
CREATE PROCEDURE New_Complaint
@complaint_ID INTEGER,
@date_of_complaint DATE,
@complaint_desc VARCHAR(64),
--@treatment VARCHAR(64),
@t_ind INTEGER,
@product_ID INTEGER,
@cust_name VARCHAR(64),
@ts_name VARCHAR(64)
AS
BEGIN
    IF @t_ind = 1
        INSERT INTO Complaint VALUES (@complaint_ID, @date_of_complaint, @complaint_desc, 'Refund');
        INSERT INTO Defect VALUES (@complaint_ID, @product_ID, @cust_name);
        INSERT INTO Rep_Compl VALUES (@complaint_ID, @product_ID, @ts_name);
    IF @t_ind = 2
        INSERT INTO Complaint VALUES (@complaint_ID, @date_of_complaint, @complaint_desc, 'Exchange');
        INSERT INTO Defect VALUES (@complaint_ID, @product_ID, @cust_name);
        INSERT INTO Rep_Compl VALUES (@complaint_ID, @product_ID, @ts_name);
END
GO

-- 6 -- 
CREATE PROCEDURE New_Accident
@accident_no INTEGER,
@accident_dt DATE,
@days_lost INTEGER,
--@acc_ind INTEGER,
--@ts_name VARCHAR(64),
--@worker_name VARCHAR(64),
@product_ID INTEGER 
AS
BEGIN
    INSERT INTO Accident VALUES (@accident_no, @accident_dt, @days_lost);
    /*IF @acc_ind = 1
        INSERT INTO Repair_Accident VALUES (@accident_no, @ts_name, @product_ID);
    IF @acc_ind = 2
        INSERT INTO Prod_Accident VALUES (@accident_no, @worker_name, @product_ID);*/
END
GO

INSERT INTO Repair_Accident VALUES (?, ?, ?); 

INSERT INTO Prod_Accident VALUES (?, ?, ?);

-- 7 -- 
SELECT P.prod_date, P.time_taken 
FROM Product P
WHERE P.product_ID = ?;

-- 8 -- 
SELECT  *
FROM Product P
WHERE P.worker_name = ?;

-- 9 --
SELECT COUNT(C.complaint_ID)
FROM Complaint C
INNER JOIN Defect D ON C.complaint_ID = D.complaint_ID
INNER JOIN Product P ON D.product_ID = P.product_ID
WHERE P.qc_name = ?;

-- 10 -- 
SELECT SUM(P.cost_prod3)
FROM Request R
INNER JOIN Cost C ON R.product_ID = C.product_ID
INNER JOIN Prod3_Acc P ON C.account_no = P.account_no
WHERE R.qc_name = ?;

-- 11 --
SELECT C.*  
FROM Customer C
INNER JOIN Purchase P ON C.cust_name = P.cust_name
INNER JOIN Product2 Pr ON Pr.product_ID = P.product_ID
WHERE Pr.color = ?
ORDER BY C.cust_name;

-- 12 -- 
SELECT *
FROM Employee E
WHERE E.emp_sal > ?;

-- 13 -- 
SELECT SUM(A.days_lost) 
FROM Accident A
INNER JOIN Repair_Accident Ra ON A.accident_no = Ra.accident_no
INNER JOIN Rep_Compl R ON R.product_ID = Ra.product_ID;
 
-- 14 --
SELECT AVG(T.cost) as avg_cost FROM
(SELECT P.product_ID as ID, P1.cost_prod1 as cost, YEAR(P.prod_date)  as year
FROM Product P
INNER JOIN Cost C ON P.product_ID = C.product_ID
INNER JOIN Prod1_Acc P1 ON C.account_no = P1.account_no
UNION
SELECT P.product_ID as ID, P2.cost_prod2 as cost, YEAR(P.prod_date)  as year
FROM Product P
INNER JOIN Cost C ON P.product_ID = C.product_ID
INNER JOIN Prod2_Acc P2 ON C.account_no = P2.account_no
UNION
SELECT P.product_ID as ID, P3.cost_prod3 as cost, YEAR(P.prod_date)  as year 
FROM Product P
INNER JOIN Cost C ON P.product_ID = C.product_ID
INNER JOIN Prod3_Acc P3 ON C.account_no = P3.account_no
) T
WHERE T.year = ?

-- 15 --
DELETE FROM Repair_Accident WHERE accident_no = (SELECT accident_no FROM Accident
WHERE accident_dt BETWEEN ? AND ?);

DELETE FROM Prod_Accident WHERE accident_no = (SELECT accident_no FROM Accident
WHERE accident_dt BETWEEN ? AND ?);

DELETE FROM Accident 
WHERE accident_dt BETWEEN ? AND ?;
