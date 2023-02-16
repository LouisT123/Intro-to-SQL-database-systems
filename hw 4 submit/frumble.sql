--question 1
CREATE TABLE database(name VARCHAR(20), discount VARCHAR(10), month VARCHAR(20), price INT);
.mode csv
.import mrFrumbleData.csv database
--question 2

--name -> discount
SELECT *
FROM  database as R1, database as R2
WHERE R1.name = R2.name
AND   R1.discount != R2.discount
LIMIT 1;
--produced results, not functional dependency

--discount -> name
SELECT *
FROM  database as R1, database as R2
WHERE R1.discount = R2.discount
AND   R1.name != R2.name
LIMIT 1;
--produced results, not functional dependency

--name -> month
SELECT *
FROM  database as R1, database as R2
WHERE R1.name = R2.name
AND   R1.month != R2.month
LIMIT 1;
--produced results, not functional dependency

--month -> name
SELECT *
FROM  database as R1, database as R2
WHERE R1.month = R2.month
AND   R1.name != R2.name
LIMIT 1;
--produced results, not functional dependency

--name -> price
SELECT *
FROM  database as R1, database as R2
WHERE R1.name = R2.name
AND   R1.price != R2.price
LIMIT 1;
--produced no results, functional dependency #1

--price -> name
SELECT *
FROM  database as R1, database as R2
WHERE R1.price = R2.price
AND   R1.name != R2.name
LIMIT 1;
--produced results, not functional dependency

--discount -> month
SELECT *
FROM  database as R1, database as R2
WHERE R1.discount = R2.discount
AND   R1.month != R2.month
LIMIT 1;
-- produced results, not FD

--month -> discount
SELECT *
FROM  database as R1, database as R2
WHERE R1.month = R2.month
AND   R1.discount != R2.discount
LIMIT 1;
--produced no results, so functional dependency #2

--trying name,month -> discount,price
SELECT *
FROM  database as R1, database as R2
WHERE R1.month = R2.month 
AND   R1.name = R2.name
AND   R1.price != R2.price
AND   R1.discount != R2.discount
LIMIT 1;
--produced no results, so functional dependency #3

--trying discount,price -> name,month 
SELECT *
FROM  database as R1, database as R2
WHERE R1.price = R2.price 
AND   R1.discount = R2.discount
AND   R1.name != R2.name
AND   R1.month != R2.month
LIMIT 1;
-- produced results, not FD

--trying discount,month -> name,price
SELECT *
FROM  database as R1, database as R2
WHERE R1.month = R2.month
AND   R1.discount = R2.discount
AND   R1.name != R2.name
AND   R1.price != R2.price
LIMIT 1;
-- produced results, not FD

--found functional dependencies: 
--name -> price 
--month -> discount
--name,month -> discount,price

--question 3
/* converting name, discount, month, price into n,d,m,p repsectively
and having the two main dependencies n->p and m->d

R(n,d,m,p) with functional dependencies n->p and m->d
Decomposed:**

1. From dependency  n->p, n+ = {n,p} does not meet BCNF,
needs to be decomposed by splitting table.
R1 = {n,p} with leftovers in R2 {n,d,m,p} with n as common link.

2. R1 now meets BCNF, but R2 doesn't: m+ = {d,m}
so it needs to be decomposed by splitting table.
R3 = {m,d} and R4 ={n,m} with C, with m as common link, both tables now meet BCNF
3. Final answer: R1 = {n,p} ; R3 = {m,d} ; R4 = {n,m}
*/

CREATE TABLE R1(
	name VARCHAR(20) PRIMARY KEY,
	price INT
);

CREATE TABLE R3(
	month VARCHAR(20) PRIMARY KEY,
	discount VARCHAR(10)
	
);

CREATE TABLE R4(
	name VARCHAR(20) REFERENCES R1(name),
	month VARCHAR(20) REFERENCES R2(month)
);
	

--question 4
INSERT INTO R1 
SELECT DISTINCT name, price 
FROM database;

INSERT INTO R3 
SELECT DISTINCT month, discount
FROM database;

INSERT INTO R4 
SELECT name, price 
FROM database;

SELECT COUNT(*) FROM R1;
SELECT COUNT(*) FROM R3;
SELECT COUNT(*) FROM R4;

--sizes were 37, 13, 427 respectively