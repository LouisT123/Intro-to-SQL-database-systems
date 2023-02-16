/*Express the same query as above, but do so without using a subquery.*/
--Rows: 4
--Took: 5 seconds
SELECT DISTINCT c.name AS carrier
FROM CARRIERS AS c, FLIGHTS AS f
WHERE f.carrier_id = c.cid
AND f.origin_city = 'Seattle WA'
AND f.dest_city = 'San Francisco CA'
ORDER BY carrier ASC;

/*Results of the first 20 rows
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America


*/