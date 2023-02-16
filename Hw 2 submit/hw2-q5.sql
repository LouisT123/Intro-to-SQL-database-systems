/*Find all airlines that had more than 0.5% (= 0.005) 
of their flights out of Seattle canceled. 
Return the name of the airline and the percentage of canceled flights out of Seattle. 
Percentages should be outputted in percent format (3.5% as 3.5 not 0.035). 
Order the results by the percentage of canceled flights in ascending order.
Name the output columns name and percentage, in that order.
 [Output relation cardinality: 6 rows]*/

.read create-tables.sql
.read import-tables.sql

SELECT
c.name AS name,
AVG(f.canceled) As percentage
FROM FLIGHTS AS f, CARRIERS AS c 
WHERE f.origin_city = "Seattle WA"
AND   f.carrier_id = c.cid
GROUP BY c.cid
HAVING percentage > .005
ORDER BY percentage ASC;