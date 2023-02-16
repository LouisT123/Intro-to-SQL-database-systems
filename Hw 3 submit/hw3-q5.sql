/*List all cities that can be reached from Seattle, 
but which require two intermediate stops or more.  
*/
--Rows: 3
--Took: 180 freaking seconds
SELECT DISTINCT
f.dest_city AS city
FROM
FLIGHTS AS f
WHERE
f.dest_city NOT IN
(SELECT f1.dest_city 
FROM	FLIGHTS AS f1
WHERE   f1.origin_city = 'Seattle WA')

AND	f.dest_city NOT IN
(SELECT f3.dest_city 
FROM	FLIGHTS AS f2, FLIGHTS AS f3
WHERE   f2.origin_city = 'Seattle WA'
AND		f2.dest_city = f3.origin_city)

ORDER BY city ASC;

/*Results of the first 20 rows
Devils Lake ND
Hattiesburg/Laurel MS
St. Augustine FL

*/