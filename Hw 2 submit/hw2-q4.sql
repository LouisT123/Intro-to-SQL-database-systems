/*(10 points) Find the names of all airlines that ever flew more than 
1000 flights in one day (i.e., a specific day/month, but not any 24-hour period). 
Return only the names of the airlines. 
Do not return any duplicates (i.e., airlines with the exact same name).
Name the output column name.
[Output relation cardinality: 12 rows]*/

.read create-tables.sql
.read import-tables.sql

SELECT DISTINCT
C.name AS name
FROM FLIGHTS AS f, CARRIERS AS c
WHERE f.carrier_id = c.cid
GROUP BY c.name, f.day_of_month, f.month_id
HAVING COUNT(f.fid) > 1000;