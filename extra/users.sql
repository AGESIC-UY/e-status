--
-- PostgreSQL database cluster dump
--

-- Started on 2015-05-05 13:16:55 UYT

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE estatus;
ALTER ROLE estatus WITH SUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION VALID UNTIL 'infinity';
COMMENT ON ROLE estatus IS 'Estatus';
CREATE ROLE estatus_admin;
ALTER ROLE estatus_admin WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION VALID UNTIL 'infinity';
CREATE ROLE estatus_user;
ALTER ROLE estatus_user WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION VALID UNTIL 'infinity';



-- Completed on 2015-05-05 13:16:55 UYT

--
-- PostgreSQL database cluster dump complete
--

