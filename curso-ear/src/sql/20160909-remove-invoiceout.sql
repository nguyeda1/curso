DELETE FROM invoiceout_invoicein_item;
DELETE FROM invoiceout_booking;
DELETE FROM invoiceout;
UPDATE invoicein_item SET invoiced=false;
UPDATE booking SET invoiced=false;
UPDATE booking SET bookingstate='ACCOUNTING' WHERE bookingstate='FINISH';