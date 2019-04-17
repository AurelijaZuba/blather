CREATE TABLE public."UserModel"
(
   "ID" SERIAL PRIMARY KEY,
   "Name" character varying(100) NOT NULL
)
WITH (
   OIDS = FALSE
);

ALTER TABLE public."UserModel"
   OWNER to postgres;\connect DBNAME