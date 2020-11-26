--Script pour cr�er les tables dans une BD vide--

--Drop des tables pour les remplacer--

drop table if exists public.users_events;
drop table if exists public.events;
drop table if exists public.users;
drop table if exists public.sports;

--Creation des tables--

--user--

CREATE TABLE public.users (
	username varchar NOT NULL,
	email varchar NOT NULL,
	"password" varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (email)
);

--sport--

CREATE TABLE public.sports (
	"name" varchar NOT NULL,
	CONSTRAINT sports_pk PRIMARY KEY (name)
);

--event--

CREATE TABLE public.events (
	idevent int NOT NULL,
	"owner" varchar NOT NULL,
	address varchar NOT NULL,
	sport varchar NOT NULL,
	eventtype varchar NOT NULL,
	maxplayers int4 NOT NULL,
	"date" date NOT NULL,
	currentplayers int4 NOT NULL,
	contact varchar NOT NULL,
	description text NOT NULL,
	cancelled bool NOT NULL,
	CONSTRAINT events_pk PRIMARY KEY (id)
);

--cle etrangeres de la table event--

ALTER TABLE public.events ADD CONSTRAINT events_fk FOREIGN KEY (owner) REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.events ADD CONSTRAINT events_fk2 FOREIGN KEY (sport) REFERENCES sports(name) ON UPDATE CASCADE ON DELETE CASCADE;

--user_event--

CREATE TABLE public.users_events (
	email varchar NOT NULL,
	idevent int4 NOT NULL,
	CONSTRAINT users_events_pk PRIMARY KEY (email, idevent)
);

--cle etrangeres de la table user_event--

ALTER TABLE public.users_events ADD CONSTRAINT users_events_fk FOREIGN KEY (idevent) REFERENCES events(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.users_events ADD CONSTRAINT users_events_fk2 FOREIGN KEY (email) REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE;
