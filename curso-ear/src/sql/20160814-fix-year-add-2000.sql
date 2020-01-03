--UPDATE invoicein SET state='DRAFT' WHERE id IN (199, 214, 213, 198, 32);
--SELECT * FROM invoicein_item WHERE invoice_id IN (199, 214, 213, 198, 32);
UPDATE invoicein SET paymentterm=paymentterm+interval '2000 years' WHERE paymentterm<'2000-01-01';
--SELECT * from invoicein WHERE paymentterm<'2000-01-01' LIMIT 10;