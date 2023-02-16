/*Find all origin cities that only serve flights shorter than 3 hours. 
You should not include canceled flights in your determination.
*/
--Rows: 109
--Took: 4 seconds
SELECT DISTINCT
f.origin_city AS city
FROM FLIGHTS AS f
WHERE f.actual_time > 0
GROUP BY f.origin_city
HAVING MAX(f.actual_time) < 180 --3 hours = 180 min
ORDER BY city ASC;

/*Results of the first 20 rows
Aberdeen SD
Abilene TX
Alpena MI
Ashland WV
Augusta GA
Barrow AK
Beaumont/Port Arthur TX
Bemidji MN
Bethel AK
Binghamton NY
Brainerd MN
Bristol/Johnson City/Kingsport TN
Butte MT
Carlsbad CA
Casper WY 
Cedar City UT
Chico CA
College Station/Bryan TX
Columbia MO
Columbus GA
*/