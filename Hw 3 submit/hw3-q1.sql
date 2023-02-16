/*For each origin city, 
find the destination city (or cities) with the longest direct flight 
(by direct flight, we mean a flight with no intermediate stops). 
Judge the longest flight using duration, not distance. */
--Rows: 334
--Took: 9 seconds

SELECT DISTINCT 
f.origin_city AS origin_city, 
f.dest_city AS dest_city,
f.actual_time AS time

FROM FLIGHTS AS f,

(SELECT f1.origin_city AS origin_city,
 MAX(f1.actual_time) AS max_time
 FROM   FLIGHTS AS f1 
 GROUP BY f1.origin_city) AS max_flight

WHERE f.origin_city = max_flight.origin_city
	  AND f.actual_time = max_flight.max_time
ORDER BY f.origin_city, f.dest_city ASC;


/*Results of the first 20 rows

Aberdeen SD	Minneapolis MN	106
Abilene TX	Dallas/Fort Worth TX	111
Adak Island AK	Anchorage AK	471
Aguadilla PR	New York NY	368
Akron OH	Atlanta GA	408
Albany GA	Atlanta GA	243
Albany NY	Atlanta GA	390
Albuquerque NM	Houston TX	492
Alexandria LA	Atlanta GA	391
Allentown/Bethlehem/Easton PA	Atlanta GA	456
Alpena MI	Detroit MI	80
Amarillo TX	Houston TX	390
Anchorage AK	Barrow AK	490
Appleton WI	Atlanta GA	405
Arcata/Eureka CA	San Francisco CA	476
Asheville NC	Chicago IL	279
Ashland WV	Cincinnati OH	84
Aspen CO	Los Angeles CA	304
Atlanta GA	Honolulu HI	649
Atlantic City NJ	Fort Lauderdale FL	212
Augusta GA	Atlanta GA	176
*/
