/*
Find the maximum price of tickets between Seattle and New York, NY 
(i.e. Seattle to NY or NY to Seattle). 
Show the maximum price for each airline separately.
Name the output columns carrier and max_price, in that order.
[Output relation cardinality: 3 rows]
*/

.read create-tables.sql
.read import-tables.sql

SELECT
c.name AS carrier,
MAX(f.price) AS max_price
FROM  FLIGHTS AS f, CARRIERS AS c
WHERE ((f.origin_city = "Seattle WA" AND f.dest_city = "New York NY")
OR    (f.origin_city = "New York NY" AND f.dest_city = "Seattle WA"))
AND   c.cid = f.carrier_id
GROUP BY c.cid;