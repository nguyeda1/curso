SELECT DISTINCT c.id,b.guestcount,b.listing_id,b.startdate,b.enddate, 'http://test.schedek.com/curso/app/booking/view.xhtml?id=' || c.id FROM (
(SELECT id FROM booking WHERE cleaningsupplier_id =3 ORDER BY id) 
UNION 
(SELECT b.id FROM booking b JOIN sysuser u ON b.keymaster_cleaning_id=u.id WHERE u.username='CLF'  ORDER BY id) 
) c JOIN booking b ON c.id=b.id ORDER BY id;

SELECT id,listing_id,startdate,enddate, 'http://test.schedek.com/curso/app/booking/view.xhtml?id=' || id,cistatustext,cicleanstatustext,cicleantext,ciguesttext FROM booking ORDER BY id;