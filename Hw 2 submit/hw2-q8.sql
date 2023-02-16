/*
Compute the total departure delay of each airline across all flights. 
Some departure delays may be negative (indicating an early departure); 
they should reduce the total, so you don't need to handle them specially. 
Name the output columns name and delay, in that order. 
[Output relation cardinality: 22 rows]
*/
.read create-tables.sql
.read import-tables.sql


SELECT
c.name AS name,
SUM(f.departure_delay) AS delay
FROM FLIGHTS AS f, CARRIERS AS C
WHERE
c.cid = f.carrier_id
GROUP BY c.cid;

