/*List all cities that can be reached from Seattle using exactly one stop. 
In other words, the flight itinerary should use an intermediate city, 
but cannot be reached through a direct flight. 
Do not include Seattle as one of these destinations*/
--Rows: 256
--Took: 43 seconds
SELECT DISTINCT
f1.dest_city AS city
FROM
FLIGHTS AS f, FLIGHTS AS f1
WHERE
f.origin_city = 'Seattle WA' 
AND f1.origin_city = f.dest_city
AND f1.dest_city != 'Seattle WA'
AND	f1.dest_city NOT IN
(SELECT DISTINCT f2.dest_city 
FROM	FLIGHTS AS f2
WHERE   f2.origin_city = 'Seattle WA')
ORDER BY city ASC;

/*Results of the first 20 rows

Aberdeen SD
Abilene TX
Adak Island AK
Aguadilla PR
Akron OH
Albany GA
Albany NY
Alexandria LA
Allentown/Bethlehem/Easton PA
Alpena MI
Amarillo TX
Appleton WI
Arcata/Eureka CA
Asheville NC
Ashland WV
Aspen CO
Atlantic City NJ
Augusta GA
Bakersfield CA
Bangor ME
*/