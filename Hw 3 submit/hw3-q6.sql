/*List the names of carriers that operate flights from Seattle to San Francisco, CA.  
Return each carrier's name only once, and use a nested query to answer this question.*/
--Rows: 4
--Took: 42 seconds
SELECT DISTINCT c.name AS carrier
FROM CARRIERS AS c, FLIGHTS AS f
WHERE f.carrier_id = c.cid
AND c.cid IN (SELECT DISTINCT f1.carrier_id
               FROM FLIGHTS AS f1
               WHERE f1.origin_city = 'Seattle WA'
               AND f1.dest_city = 'San Francisco CA')
ORDER BY carrier ASC;

/*Results of the first 20 rows
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America


*/