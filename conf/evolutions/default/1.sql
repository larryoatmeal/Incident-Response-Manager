# --- !Ups
DROP TABLE IF EXISTS incidents;


CREATE TABLE incidents (

	id 					int 			NOT NULL AUTO_INCREMENT, 			#? Needs to be not null?
	title 				varchar(256)	NOT NULL,
	description 		TEXT			NOT NULL, #? Probably need to change max characters
	incident_type		ENUM('internal','external'),
	status				ENUM('open','closed'),
	issue_id			varchar(256),
	issue_type_id		int,	
	respond_team_id		int,
	primary_responder 	int   			NOT NULL,
	started_at			DATETIME,
	finished_at			DATETIME,
	next_update_at		DATETIME,
	created_at			DATETIME		NOT NULL,
	updated_at			DATETIME		NOT NULL,
	created_by			int 			NOT NULL,
	updated_by			int,

	PRIMARY KEY (id)
);



DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id 					int           	NOT NULL AUTO_INCREMENT,
	first_name			varchar(256)	NOT NULL,
	last_name			varchar(256)	NOT NULL,
	email				varchar(256)	NOT NULL,

	PRIMARY KEY(id),
	UNIQUE (email)
);




DROP TABLE IF EXISTS teams;

CREATE TABLE teams (
	id 					int 			NOT NULL AUTO_INCREMENT,
	name 				varchar(256)	NOT NULL,

	PRIMARY KEY(id),
	UNIQUE(name)
);



DROP TABLE IF EXISTS user_team_map;

CREATE TABLE user_team_map (
	user_id				int 			NOT NULL,
	team_id  			int 			NOT NULL,

	PRIMARY KEY (user_id, team_id) # Composite primary key
);


DROP TABLE IF EXISTS incident_updates;

CREATE TABLE incident_updates (
	id  				int 			NOT NULL AUTO_INCREMENT,
	incident_id			int             NOT NULL,
	description			TEXT 			NOT NULL,
	created_by			int 			NOT NULL,
	created_at			DATETIME		NOT NULL,
	deleted				BOOLEAN 		NOT NULL,

	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS incident_subscriptions;

CREATE TABLE incident_subscriptions (
	incident_id			int 			NOT NULL,
	team_id 			int 			NOT NULL,

	PRIMARY KEY(incident_id, team_id)
);


DROP TABLE IF EXISTS issue_types;

CREATE TABLE issue_types (
	id					int 			NOT NULL AUTO_INCREMENT,
	slug				varchar(256)	NOT NULL,
	clazz				varchar(256),
	name 				varchar(256),

	PRIMARY KEY(id),
	UNIQUE(slug)

);



# --- !Downs