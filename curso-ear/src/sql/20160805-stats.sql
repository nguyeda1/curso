SELECT mon,sum(guestcount),count(id) FROM (SELECT *,date_trunc('month',startdate) mon FROM booking) b WHERE bookingstate != 'CANCELED' GROUP BY mon ORDER BY mon;
