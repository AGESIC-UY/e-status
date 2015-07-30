--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2015-05-05 13:13:25 UYT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 2031 (class 1262 OID 108394)
-- Name: estatus; Type: DATABASE; Schema: -; Owner: postgres
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 188 (class 3079 OID 11756)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2034 (class 0 OID 0)
-- Dependencies: 188
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 108395)
-- Name: data; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE data (
    indicator integer NOT NULL,
    taken timestamp without time zone NOT NULL,
    val real NOT NULL
);


ALTER TABLE public.data OWNER TO estatus;

--
-- TOC entry 173 (class 1259 OID 108398)
-- Name: filter; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE filter (
    id integer NOT NULL,
    name character varying(50),
    filter_type smallint NOT NULL,
    field_name character varying(50) NOT NULL,
    filter_operator smallint NOT NULL,
    filter_order integer
);


ALTER TABLE public.filter OWNER TO estatus;

--
-- TOC entry 174 (class 1259 OID 108401)
-- Name: filter_id_seq; Type: SEQUENCE; Schema: public; Owner: estatus
--

CREATE SEQUENCE filter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.filter_id_seq OWNER TO estatus;

--
-- TOC entry 2037 (class 0 OID 0)
-- Dependencies: 174
-- Name: filter_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: estatus
--

ALTER SEQUENCE filter_id_seq OWNED BY filter.id;


--
-- TOC entry 175 (class 1259 OID 108403)
-- Name: filter_option; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE filter_option (
    id integer NOT NULL,
    filter integer NOT NULL,
    option_value character varying(200) NOT NULL
);


ALTER TABLE public.filter_option OWNER TO estatus;

--
-- TOC entry 176 (class 1259 OID 108406)
-- Name: filter_option_id_seq; Type: SEQUENCE; Schema: public; Owner: estatus
--

CREATE SEQUENCE filter_option_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.filter_option_id_seq OWNER TO estatus;

--
-- TOC entry 2039 (class 0 OID 0)
-- Dependencies: 176
-- Name: filter_option_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: estatus
--

ALTER SEQUENCE filter_option_id_seq OWNED BY filter_option.id;


--
-- TOC entry 177 (class 1259 OID 108408)
-- Name: indicator; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE indicator (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    query text,
    active boolean DEFAULT true NOT NULL,
    val_is_integer boolean DEFAULT true NOT NULL,
    val_is_percentage boolean DEFAULT false NOT NULL,
    update_interval integer DEFAULT 15 NOT NULL,
    classification integer,
    explanation text,
    detail text
);


ALTER TABLE public.indicator OWNER TO estatus;

--
-- TOC entry 178 (class 1259 OID 108418)
-- Name: indicator_id_seq; Type: SEQUENCE; Schema: public; Owner: estatus
--

CREATE SEQUENCE indicator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.indicator_id_seq OWNER TO estatus;

--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 178
-- Name: indicator_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: estatus
--

ALTER SEQUENCE indicator_id_seq OWNED BY indicator.id;


--
-- TOC entry 184 (class 1259 OID 108508)
-- Name: local_role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE local_role (
    id integer NOT NULL,
    local_role_name character varying(50) NOT NULL
);


ALTER TABLE public.local_role OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 108506)
-- Name: local_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE local_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.local_role_id_seq OWNER TO postgres;

--
-- TOC entry 2043 (class 0 OID 0)
-- Dependencies: 183
-- Name: local_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE local_role_id_seq OWNED BY local_role.id;


--
-- TOC entry 186 (class 1259 OID 108518)
-- Name: local_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE local_user (
    id integer NOT NULL,
    local_user_name character varying(200) NOT NULL,
    local_role integer,
    local_user_long_name character varying(250),
    must_see_news boolean DEFAULT true NOT NULL
);


ALTER TABLE public.local_user OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 108516)
-- Name: local_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE local_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.local_user_id_seq OWNER TO postgres;

--
-- TOC entry 2045 (class 0 OID 0)
-- Dependencies: 185
-- Name: local_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE local_user_id_seq OWNED BY local_user.id;


--
-- TOC entry 179 (class 1259 OID 108420)
-- Name: log; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE log (
    level integer NOT NULL,
    logger text NOT NULL,
    message text NOT NULL,
    sequence text NOT NULL,
    source_class text NOT NULL,
    source_method text NOT NULL,
    thread_id integer NOT NULL,
    time_entered timestamp without time zone NOT NULL,
    stack_trace text
);


ALTER TABLE public.log OWNER TO estatus;

--
-- TOC entry 180 (class 1259 OID 108426)
-- Name: parameter; Type: TABLE; Schema: public; Owner: estatus_admin; Tablespace: 
--

CREATE TABLE parameter (
    name character varying(30) NOT NULL,
    val character varying(5000)
);


ALTER TABLE public.parameter OWNER TO estatus_admin;

--
-- TOC entry 181 (class 1259 OID 108432)
-- Name: rel_filter_indicator; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE rel_filter_indicator (
    filter integer NOT NULL,
    indicator integer NOT NULL
);


ALTER TABLE public.rel_filter_indicator OWNER TO estatus;

--
-- TOC entry 187 (class 1259 OID 108537)
-- Name: rel_user_indicator; Type: TABLE; Schema: public; Owner: estatus; Tablespace: 
--

CREATE TABLE rel_user_indicator (
    localuser integer NOT NULL,
    indicator integer NOT NULL
);


ALTER TABLE public.rel_user_indicator OWNER TO estatus;

--
-- TOC entry 182 (class 1259 OID 108450)
-- Name: v_log; Type: VIEW; Schema: public; Owner: estatus
--

CREATE VIEW v_log AS
 SELECT pg_shdescription.description AS "user",
    log.message,
    log.time_entered,
        CASE
            WHEN (log.level = 800) THEN 'INFO'::text
            WHEN (log.level = 1000) THEN 'SEVERE'::text
            ELSE NULL::text
        END AS level
   FROM ((log
   LEFT JOIN pg_user ON (((pg_user.usename)::text = "substring"(log.message, '^[^@]*'::text))))
   LEFT JOIN pg_shdescription ON ((pg_shdescription.objoid = pg_user.usesysid)))
  WHERE (log.level >= 800)
  ORDER BY log.time_entered DESC
 LIMIT 1000;


ALTER TABLE public.v_log OWNER TO estatus;

--
-- TOC entry 1880 (class 2604 OID 108460)
-- Name: id; Type: DEFAULT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY filter ALTER COLUMN id SET DEFAULT nextval('filter_id_seq'::regclass);


--
-- TOC entry 1881 (class 2604 OID 108461)
-- Name: id; Type: DEFAULT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY filter_option ALTER COLUMN id SET DEFAULT nextval('filter_option_id_seq'::regclass);


--
-- TOC entry 1886 (class 2604 OID 108462)
-- Name: id; Type: DEFAULT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY indicator ALTER COLUMN id SET DEFAULT nextval('indicator_id_seq'::regclass);


--
-- TOC entry 1887 (class 2604 OID 108511)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY local_role ALTER COLUMN id SET DEFAULT nextval('local_role_id_seq'::regclass);


--
-- TOC entry 1888 (class 2604 OID 108521)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY local_user ALTER COLUMN id SET DEFAULT nextval('local_user_id_seq'::regclass);


--
-- TOC entry 1891 (class 2606 OID 108464)
-- Name: data_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY data
    ADD CONSTRAINT data_pkey PRIMARY KEY (indicator, taken);


--
-- TOC entry 1895 (class 2606 OID 108466)
-- Name: filter_option_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY filter_option
    ADD CONSTRAINT filter_option_pkey PRIMARY KEY (filter, option_value);


--
-- TOC entry 1893 (class 2606 OID 108468)
-- Name: filter_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY filter
    ADD CONSTRAINT filter_pkey PRIMARY KEY (id);


--
-- TOC entry 1897 (class 2606 OID 108470)
-- Name: indicator_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY indicator
    ADD CONSTRAINT indicator_pkey PRIMARY KEY (id);


--
-- TOC entry 1903 (class 2606 OID 108513)
-- Name: local_role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY local_role
    ADD CONSTRAINT local_role_pk PRIMARY KEY (id);


--
-- TOC entry 1905 (class 2606 OID 108515)
-- Name: local_role_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY local_role
    ADD CONSTRAINT local_role_unique UNIQUE (local_role_name);


--
-- TOC entry 1907 (class 2606 OID 108524)
-- Name: local_user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY local_user
    ADD CONSTRAINT local_user_pk PRIMARY KEY (id);


--
-- TOC entry 1909 (class 2606 OID 108526)
-- Name: local_user_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY local_user
    ADD CONSTRAINT local_user_unique UNIQUE (local_user_name);


--
-- TOC entry 1899 (class 2606 OID 108472)
-- Name: parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus_admin; Tablespace: 
--

ALTER TABLE ONLY parameter
    ADD CONSTRAINT parameter_pkey PRIMARY KEY (name);


--
-- TOC entry 1901 (class 2606 OID 108474)
-- Name: rel_filter_indicator_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY rel_filter_indicator
    ADD CONSTRAINT rel_filter_indicator_pkey PRIMARY KEY (filter, indicator);


--
-- TOC entry 1911 (class 2606 OID 108541)
-- Name: rel_user_inicator_pkey; Type: CONSTRAINT; Schema: public; Owner: estatus; Tablespace: 
--

ALTER TABLE ONLY rel_user_indicator
    ADD CONSTRAINT rel_user_inicator_pkey PRIMARY KEY (localuser, indicator);


--
-- TOC entry 1912 (class 2606 OID 108479)
-- Name: data_indicator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY data
    ADD CONSTRAINT data_indicator_fkey FOREIGN KEY (indicator) REFERENCES indicator(id);


--
-- TOC entry 1913 (class 2606 OID 108484)
-- Name: filter_option_filter_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY filter_option
    ADD CONSTRAINT filter_option_filter_fkey FOREIGN KEY (filter) REFERENCES filter(id);


--
-- TOC entry 1916 (class 2606 OID 108527)
-- Name: local_user_role_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY local_user
    ADD CONSTRAINT local_user_role_fk FOREIGN KEY (local_role) REFERENCES local_role(id);


--
-- TOC entry 1914 (class 2606 OID 108489)
-- Name: rel_filter_indicator_filter_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY rel_filter_indicator
    ADD CONSTRAINT rel_filter_indicator_filter_fkey FOREIGN KEY (filter) REFERENCES filter(id);


--
-- TOC entry 1915 (class 2606 OID 108494)
-- Name: rel_filter_indicator_indicator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY rel_filter_indicator
    ADD CONSTRAINT rel_filter_indicator_indicator_fkey FOREIGN KEY (indicator) REFERENCES indicator(id);


--
-- TOC entry 1917 (class 2606 OID 108542)
-- Name: rel_user_indicator_indicator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY rel_user_indicator
    ADD CONSTRAINT rel_user_indicator_indicator_fkey FOREIGN KEY (indicator) REFERENCES indicator(id);


--
-- TOC entry 1918 (class 2606 OID 108547)
-- Name: rel_user_indicator_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: estatus
--

ALTER TABLE ONLY rel_user_indicator
    ADD CONSTRAINT rel_user_indicator_user_fkey FOREIGN KEY (localuser) REFERENCES local_user(id);


--
-- TOC entry 2033 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO estatus;
GRANT USAGE ON SCHEMA public TO estatus_user;
GRANT ALL ON SCHEMA public TO estatus_admin;


--
-- TOC entry 2035 (class 0 OID 0)
-- Dependencies: 172
-- Name: data; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE data FROM PUBLIC;
REVOKE ALL ON TABLE data FROM estatus;
GRANT ALL ON TABLE data TO estatus;
GRANT ALL ON TABLE data TO estatus_admin;
GRANT SELECT ON TABLE data TO estatus_user;


--
-- TOC entry 2036 (class 0 OID 0)
-- Dependencies: 173
-- Name: filter; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE filter FROM PUBLIC;
REVOKE ALL ON TABLE filter FROM estatus;
GRANT ALL ON TABLE filter TO estatus;
GRANT SELECT ON TABLE filter TO estatus_user;
GRANT ALL ON TABLE filter TO estatus_admin;


--
-- TOC entry 2038 (class 0 OID 0)
-- Dependencies: 175
-- Name: filter_option; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE filter_option FROM PUBLIC;
REVOKE ALL ON TABLE filter_option FROM estatus;
GRANT ALL ON TABLE filter_option TO estatus;
GRANT SELECT ON TABLE filter_option TO estatus_user;
GRANT ALL ON TABLE filter_option TO estatus_admin;


--
-- TOC entry 2040 (class 0 OID 0)
-- Dependencies: 177
-- Name: indicator; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE indicator FROM PUBLIC;
REVOKE ALL ON TABLE indicator FROM estatus;
GRANT ALL ON TABLE indicator TO estatus;
GRANT ALL ON TABLE indicator TO estatus_admin;
GRANT SELECT ON TABLE indicator TO estatus_user;


--
-- TOC entry 2042 (class 0 OID 0)
-- Dependencies: 184
-- Name: local_role; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE local_role FROM PUBLIC;
REVOKE ALL ON TABLE local_role FROM postgres;
GRANT ALL ON TABLE local_role TO postgres;
GRANT ALL ON TABLE local_role TO estatus;
GRANT ALL ON TABLE local_role TO estatus_admin;


--
-- TOC entry 2044 (class 0 OID 0)
-- Dependencies: 186
-- Name: local_user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE local_user FROM PUBLIC;
REVOKE ALL ON TABLE local_user FROM postgres;
GRANT ALL ON TABLE local_user TO postgres;
GRANT ALL ON TABLE local_user TO estatus;
GRANT SELECT ON TABLE local_user TO estatus_user;
GRANT ALL ON TABLE local_user TO estatus_admin;


--
-- TOC entry 2046 (class 0 OID 0)
-- Dependencies: 179
-- Name: log; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE log FROM PUBLIC;
REVOKE ALL ON TABLE log FROM estatus;
GRANT ALL ON TABLE log TO estatus;
GRANT ALL ON TABLE log TO estatus_admin;


--
-- TOC entry 2047 (class 0 OID 0)
-- Dependencies: 180
-- Name: parameter; Type: ACL; Schema: public; Owner: estatus_admin
--

REVOKE ALL ON TABLE parameter FROM PUBLIC;
REVOKE ALL ON TABLE parameter FROM estatus_admin;
GRANT ALL ON TABLE parameter TO estatus_admin;
GRANT SELECT,UPDATE ON TABLE parameter TO estatus_user;


--
-- TOC entry 2048 (class 0 OID 0)
-- Dependencies: 181
-- Name: rel_filter_indicator; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE rel_filter_indicator FROM PUBLIC;
REVOKE ALL ON TABLE rel_filter_indicator FROM estatus;
GRANT ALL ON TABLE rel_filter_indicator TO estatus;
GRANT SELECT ON TABLE rel_filter_indicator TO estatus_user;
GRANT ALL ON TABLE rel_filter_indicator TO estatus_admin;


--
-- TOC entry 2049 (class 0 OID 0)
-- Dependencies: 187
-- Name: rel_user_indicator; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE rel_user_indicator FROM PUBLIC;
REVOKE ALL ON TABLE rel_user_indicator FROM estatus;
GRANT ALL ON TABLE rel_user_indicator TO estatus;
GRANT SELECT ON TABLE rel_user_indicator TO estatus_user;
GRANT ALL ON TABLE rel_user_indicator TO estatus_admin;


--
-- TOC entry 2050 (class 0 OID 0)
-- Dependencies: 182
-- Name: v_log; Type: ACL; Schema: public; Owner: estatus
--

REVOKE ALL ON TABLE v_log FROM PUBLIC;
REVOKE ALL ON TABLE v_log FROM estatus;
GRANT ALL ON TABLE v_log TO estatus;
GRANT ALL ON TABLE v_log TO estatus_admin;


-- Completed on 2015-05-05 13:13:26 UYT

--
-- PostgreSQL database dump complete
--

