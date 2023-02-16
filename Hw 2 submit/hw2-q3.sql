/*(10 points) Find the day of the week with the longest average arrival delay. 
Return the name of the day and the average delay.
 Name the output columns day_of_week and delay, in that order.
 [Output relation cardinality: 1 row]
*/
.read create-tables.sql
.read import-tables.sql

--output: 
SELECT 
d.day_of_week AS day_of_week, 
AVG(f.arrival_delay) AS delay

FROM
FLIGHTS AS f,
WEEKDAYS AS d

WHERE f.day_of_week_id = d.did

GROUP BY f.day_of_week_id 

ORDER BY AVG(f.arrival_delay) DESC

LIMIT 1;