/*
Find the total capacity of all direct flights that fly between Seattle and San Francisco, CA 
on July 10th (i.e. Seattle to SF or SF to Seattle).
 Name the output column capacity.
 [Output relation cardinality: 1 row]
*/
.read create-tables.sql
.read import-tables.sql


SELECT
SUM(f.capacity) AS capacity
FROM FLIGHTS as f, MONTHS as m
WHERE ((f.origin_city = "Seattle WA" AND f.dest_city = "San Francisco CA")
OR    (f.origin_city = "San Francisco CA" AND f.dest_city = "Seattle WA"))
AND f.month_id = m.mid
AND f.day_of_month = 10
AND m.month = "July";
