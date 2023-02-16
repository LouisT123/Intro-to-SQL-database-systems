/*List the distinct flight numbers of all flights 
from Seattle to Boston by Alaska Airlines Inc. 
on Mondays. Please use the flight_num column instead of fid. 
Name the output column flight_num.
Output relation cardinality: 3 rows*/

.read create-tables.sql
.read import-tables.sql
.header ON
.mode column

SELECT DISTINCT f.flight_num AS flight_num
FROM FLIGHTS as f, CARRIERS as c, WEEKDAYS as d
WHERE f.origin_city = "Seattle WA" AND f.dest_city = "Boston MA"
AND c.name = "Alaska Airlines Inc." AND d.day_of_week = "Monday"
AND f.day_of_week_id = d.did AND f.carrier_id = c.cid;
