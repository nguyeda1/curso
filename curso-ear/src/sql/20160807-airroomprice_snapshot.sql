SELECT date FROM (
SELECT * FROM airroomprice WHERE room_id=6680550 AND created<'2016-04-14' ORDER BY date_trunc('day',created) DESC,date DESC LIMIT 365
) sq WHERE available;
