/*For each origin city, find the percentage of departed flights whose duration is shorter 
than 3 hours; canceled flights do not count as having departed.  
Be careful to handle cities which do not have any flights shorter than 3 hours; 
you should return 0 as the result for these cities*/
--Rows: 327
--Took: 9 seconds

SELECT
f.origin_city AS origin_city,

(SELECT 100.0 * COUNT(f1.origin_city)/ COUNT(f.origin_city)
FROM FLIGHTS AS f1
WHERE   f1.actual_time < 180 
AND     f1.actual_time > 0
AND		f1.origin_city = f.origin_city) AS percentage

FROM FLIGHTS AS f
GROUP BY f.origin_city
ORDER BY percentage, origin_city ASC;

/*Results of the first 20 rows

Guam TT	0.000000000000
Pago Pago TT	0.000000000000
Aguadilla PR	28.679245283018
Anchorage AK	31.656277827248
San Juan PR	33.543916853474
Charlotte Amalie VI	39.270072992700
Ponce PR	40.322580645161
Fairbanks AK	49.539170506912
Kahului HI	53.341183397115
Honolulu HI	54.533695511576
San Francisco CA	55.223708487084
Los Angeles CA	55.412788344799
Seattle WA	57.410932825673
New York NY	60.532437322305
Long Beach CA	61.719979024646
Kona HI	62.952799121844
Newark NJ	63.367565254599
Plattsburgh NY	64.000000000000
Las Vegas NV	64.471006179920
Christiansted VI	64.666666666666
*/

