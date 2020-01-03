--------------------------------------------------------------------------------
-- database functions and other SQL
--------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION tostring(num numeric)  RETURNS text AS 'SELECT CAST($1 as text)'  LANGUAGE sql IMMUTABLE STRICT  COST 100;
CREATE OR REPLACE VIEW user_group AS SELECT u.username, g.name AS groupname FROM sysuser u, sysgroup g, sysuser_sysgroup ug WHERE u.id = ug.users_id AND ug.groups_id = g.id; 

-- Create a function that always returns the first non-NULL item
CREATE OR REPLACE FUNCTION public.first_agg ( anyelement, anyelement ) RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$ SELECT $1; $$;
 
-- And then wrap an aggregate around it
CREATE AGGREGATE public.FIRST ( sfunc    = public.first_agg, basetype = anyelement, stype    = anyelement);
 
-- Create a function that always returns the last non-NULL item
CREATE OR REPLACE FUNCTION public.last_agg ( anyelement, anyelement ) RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$ SELECT $2; $$;
 
-- And then wrap an aggregate around it
CREATE AGGREGATE public.LAST ( sfunc    = public.last_agg, basetype = anyelement, stype    = anyelement);

CREATE OR REPLACE FUNCTION diff_dates_in_hours(toTime TIME WITHOUT TIME ZONE, fromTime TIME WITHOUT TIME ZONE) RETURNS DOUBLE PRECISION LANGUAGE sql COST 100 IMMUTABLE AS $BODY$ SELECT extract(epoch from (toTime - fromTime))/3600 $BODY$;