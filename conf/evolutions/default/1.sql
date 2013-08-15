# --- !Ups
DROP TABLE IF EXISTS incidents;


CREATE TABLE incidents (

	id 					int 			NOT NULL AUTO_INCREMENT, 			#? Needs to be not null?
	title 				varchar(256)	NOT NULL,
	description 		TEXT			NOT NULL, #? Probably need to change max characters
	incident_type		varchar(256)	NOT NULL,
	status				varchar(256)	NOT NULL,
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
	deleted 			Boolean 		NOT NULL,


	PRIMARY KEY(id),
	UNIQUE (email)
);


INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("1","Mercedes","Pittman","Nunc.ut.erat@gravidaAliquamtincidunt.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("2","Astra","Burke","eget@dignissimpharetraNam.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("3","Cleo","Haynes","nisi.Mauris@dui.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("4","Davis","Ramos","at.nisi@gravida.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("5","Beatrice","Harrington","dolor.Nulla@Donec.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("6","Rhoda","Alvarez","erat.volutpat@vitae.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("7","Armando","Jensen","faucibus.Morbi.vehicula@antedictumcursus.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("8","Susan","Vargas","amet.ante@utmiDuis.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("9","Philip","Flores","habitant.morbi@auctorveliteget.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("10","Rina","Boone","justo.sit@nec.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("11","Dakota","Reid","lectus@nisi.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("12","Blythe","Bond","adipiscing@a.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("13","Merrill","Murray","pellentesque.a.facilisis@Vivamuseuismodurna.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("14","Marcia","Gomez","bibendum.Donec.felis@etlacinia.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("15","Keith","Glenn","nunc.interdum@tempus.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("16","Gage","Lloyd","adipiscing.non@acturpis.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("17","Madeson","Little","libero.at@non.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("18","Caleb","Nicholson","sit.amet.consectetuer@purus.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("19","Ivory","Cooley","vestibulum@ornareelit.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("20","Martha","Horne","nec.euismod.in@utodiovel.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("21","Logan","Fleming","in.faucibus.orci@aliquetliberoInteger.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("22","Alexander","Pierce","aliquet@Nullafacilisi.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("23","Honorato","Bird","sem@est.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("24","Rose","Espinoza","ante.lectus@nislMaecenasmalesuada.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("25","Nerea","Sexton","porttitor.eros.nec@scelerisque.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("26","Willa","Castaneda","nec@tinciduntadipiscingMauris.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("27","Duncan","King","velit@ornarelectus.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("28","September","Reynolds","Etiam.bibendum@risus.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("29","Pearl","Gregory","luctus.sit@odio.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("30","Ferris","Clemons","vehicula@aclibero.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("31","Connor","Middleton","purus.ac@ridiculus.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("32","Neil","Savage","ultricies.ligula.Nullam@dolorDonec.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("33","Lysandra","Bradshaw","diam@Integervulputate.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("34","Matthew","Gaines","risus.odio@etnetus.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("35","Scarlett","Coleman","accumsan.sed@ornaretortorat.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("36","Vincent","Hood","Cras.dolor.dolor@nibh.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("37","Serina","Mckay","purus.Duis@nasceturridiculusmus.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("38","Cameran","Marquez","nonummy.ultricies@ametloremsemper.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("39","Harlan","Barton","non@eu.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("40","Amir","Houston","Lorem.ipsum.dolor@Donecporttitortellus.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("41","Cairo","Bauer","dolor.sit.amet@semmolestie.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("42","Gavin","Simpson","luctus@luctusvulputate.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("43","Elliott","Brewer","lectus@egestas.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("44","Harding","Moran","vestibulum@ipsum.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("45","Yael","Mayer","eros@Donecelementum.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("46","Dorothy","Abbott","metus.urna@acmieleifend.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("47","Demetrius","Phelps","sed.libero@dolorFuscemi.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("48","Xena","Meadows","lorem@Aliquam.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("49","Gareth","Fitzgerald","mi@Duissitamet.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("50","Jolie","Bishop","feugiat.Sed@nonummy.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("51","Ulysses","Valentine","at.augue@dapibusgravida.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("52","Janna","Hines","varius.orci.in@Fusce.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("53","Elaine","Green","pede.malesuada@feugiatSed.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("54","Karyn","Acevedo","Integer.aliquam@tortor.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("55","Cairo","Alford","tristique@fermentummetusAenean.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("56","Lacey","Mccall","tincidunt.pede.ac@dolor.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("57","Alma","Workman","sed@veliteu.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("58","Abel","Mcclure","magnis.dis.parturient@nostraper.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("59","Mechelle","Cline","imperdiet@a.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("60","Breanna","Bullock","montes@cursusIntegermollis.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("61","Nell","Zamora","Donec@nonbibendumsed.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("62","Gareth","Dillon","dui@sitametluctus.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("63","Holmes","Chavez","malesuada.fames@euneque.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("64","Kenyon","Carrillo","Etiam.bibendum.fermentum@ac.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("65","Winifred","Vinson","bibendum@nonummyFuscefermentum.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("66","Tyrone","Morrow","risus.quis@elementumduiquis.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("67","Noelle","Greene","dolor.Donec@cursuspurus.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("68","Susan","Lucas","elit@lacusQuisqueimperdiet.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("69","Winter","Munoz","ut.dolor@estvitae.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("70","Kareem","Oneil","vestibulum.neque.sed@Integerin.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("71","Cora","Stokes","porttitor@nec.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("72","Quinlan","Mcmahon","parturient@feugiat.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("73","Sheila","Buckner","a@magnaetipsum.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("74","Ginger","Ayala","Duis@vehiculaPellentesquetincidunt.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("75","Amir","Osborne","vel.sapien.imperdiet@Morbivehicula.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("76","Roanna","Ayala","elit@ametdapibus.com","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("77","Rebecca","Schroeder","Praesent@arcuCurabiturut.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("78","Noah","Abbott","tristique@dui.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("79","Paloma","Riddle","et@lacusMauris.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("80","Aline","Kelly","eu@molestiearcu.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("81","Cameran","Barron","Suspendisse.non@porttitorvulputateposuere.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("82","Rinah","Hale","Cum@fermentumfermentum.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("83","Hilda","Perkins","cursus.a@egestaslacinia.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("84","Wendy","Whitehead","in.dolor.Fusce@diamluctus.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("85","Yael","Rojas","ante@mollislectuspede.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("86","Nero","Schroeder","ultrices.sit@lobortis.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("87","Lars","Vang","tellus.imperdiet@Sedeu.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("88","Brian","Spears","ut@Nuncsedorci.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("89","Eaton","Frank","Sed.id.risus@Vivamus.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("90","Valentine","Andrews","vel.venenatis@aduiCras.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("91","Amal","Russo","pretium@idnuncinterdum.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("92","Glenna","Strong","mauris.Suspendisse.aliquet@dictumeuplacerat.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("93","Lance","Schwartz","turpis.egestas@sed.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("94","Dacey","Miranda","turpis.vitae@nonummyac.net","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("95","Jocelyn","Beck","Quisque.fringilla.euismod@tinciduntaliquamarcu.edu","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("96","Lee","Silva","Nunc.sollicitudin.commodo@lectusconvallisest.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("97","Jeanette","Dillon","egestas.a.scelerisque@Nullam.co.uk","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("98","Claire","Brennan","sed@rutrumurnanec.ca","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("99","Halee","Hopper","egestas.blandit@sitamet.org","0");
INSERT INTO users (id,first_name,last_name,email, deleted) VALUES ("100","Grady","Hall","non.leo@In.co.uk","0");


#

DROP TABLE IF EXISTS teams;

CREATE TABLE teams (
	id 					int 			NOT NULL AUTO_INCREMENT,
	name 				varchar(256)	NOT NULL,
	deleted 			Boolean 		NOT NULL,

	PRIMARY KEY(id),
	UNIQUE(name)
);

INSERT INTO teams (id,name, deleted) VALUES ("1","Sit Amet Inc.","0");
INSERT INTO teams (id,name, deleted) VALUES ("2","Neque Sed Sem Company","0");
INSERT INTO teams (id,name, deleted) VALUES ("3","Sed Nulla Ante Limited","0");
INSERT INTO teams (id,name, deleted) VALUES ("4","Montes Industries","0");
INSERT INTO teams (id,name, deleted) VALUES ("5","Integer Sem Elit PC","0");
INSERT INTO teams (id,name, deleted) VALUES ("6","Iaculis Odio Nam Inc.","0");
INSERT INTO teams (id,name, deleted) VALUES ("7","Metus Facilisis Lorem PC","0");
INSERT INTO teams (id,name, deleted) VALUES ("8","Dictum LLP","0");
INSERT INTO teams (id,name, deleted) VALUES ("9","Neque Sed Limited","0");
INSERT INTO teams (id,name, deleted) VALUES ("10","Euismod Mauris Eu Incorporated","0");
INSERT INTO teams (id,name, deleted) VALUES ("11","Cras Vulputate LLP","0");
INSERT INTO teams (id,name, deleted) VALUES ("12","Donec Porttitor Tellus Incorporated","0");
INSERT INTO teams (id,name, deleted) VALUES ("13","Morbi Non Sapien Associates","0");
INSERT INTO teams (id,name, deleted) VALUES ("14","Felis Ullamcorper Limited","0");
INSERT INTO teams (id,name, deleted) VALUES ("15","Sed Nunc Ltd","0");
INSERT INTO teams (id,name, deleted) VALUES ("16","Diam Limited","0");
INSERT INTO teams (id,name, deleted) VALUES ("17","Neque LLC","0");
INSERT INTO teams (id,name, deleted) VALUES ("18","Et Malesuada Fames LLP","0");
INSERT INTO teams (id,name, deleted) VALUES ("19","At Arcu Incorporated","0");
INSERT INTO teams (id,name, deleted) VALUES ("20","Integer In Magna Ltd","0");


#

DROP TABLE IF EXISTS user_team_map;

CREATE TABLE user_team_map (
	user_id				int 			NOT NULL,
	team_id  			int 			NOT NULL,

	PRIMARY KEY (user_id, team_id) # Composite primary key
);


INSERT INTO user_team_map (user_id,team_id) VALUES ("38","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("46","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("2","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("73","14");
INSERT INTO user_team_map (user_id,team_id) VALUES ("79","8");
INSERT INTO user_team_map (user_id,team_id) VALUES ("46","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("54","2");
INSERT INTO user_team_map (user_id,team_id) VALUES ("61","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("53","20");
INSERT INTO user_team_map (user_id,team_id) VALUES ("40","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("62","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("14","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("15","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("1","8");
INSERT INTO user_team_map (user_id,team_id) VALUES ("22","18");
INSERT INTO user_team_map (user_id,team_id) VALUES ("4","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("45","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("21","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("63","19");
INSERT INTO user_team_map (user_id,team_id) VALUES ("12","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("33","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("17","18");
INSERT INTO user_team_map (user_id,team_id) VALUES ("66","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("68","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("77","2");
INSERT INTO user_team_map (user_id,team_id) VALUES ("66","8");
INSERT INTO user_team_map (user_id,team_id) VALUES ("52","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("55","14");
INSERT INTO user_team_map (user_id,team_id) VALUES ("38","11");
INSERT INTO user_team_map (user_id,team_id) VALUES ("5","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("40","2");
INSERT INTO user_team_map (user_id,team_id) VALUES ("19","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("57","8");
INSERT INTO user_team_map (user_id,team_id) VALUES ("31","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("32","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("60","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("99","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("51","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("55","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("83","7");
INSERT INTO user_team_map (user_id,team_id) VALUES ("24","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("70","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("28","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("42","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("79","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("24","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("54","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("3","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("80","7");
INSERT INTO user_team_map (user_id,team_id) VALUES ("30","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("75","18");
INSERT INTO user_team_map (user_id,team_id) VALUES ("76","15");
INSERT INTO user_team_map (user_id,team_id) VALUES ("66","6");
INSERT INTO user_team_map (user_id,team_id) VALUES ("38","4");
INSERT INTO user_team_map (user_id,team_id) VALUES ("43","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("52","14");
INSERT INTO user_team_map (user_id,team_id) VALUES ("69","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("42","20");
INSERT INTO user_team_map (user_id,team_id) VALUES ("45","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("61","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("29","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("42","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("26","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("93","1");
INSERT INTO user_team_map (user_id,team_id) VALUES ("77","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("17","11");
INSERT INTO user_team_map (user_id,team_id) VALUES ("12","19");
INSERT INTO user_team_map (user_id,team_id) VALUES ("25","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("17","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("97","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("82","10");
INSERT INTO user_team_map (user_id,team_id) VALUES ("26","11");
INSERT INTO user_team_map (user_id,team_id) VALUES ("69","14");
INSERT INTO user_team_map (user_id,team_id) VALUES ("46","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("50","2");
INSERT INTO user_team_map (user_id,team_id) VALUES ("37","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("90","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("62","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("21","11");
INSERT INTO user_team_map (user_id,team_id) VALUES ("19","20");
INSERT INTO user_team_map (user_id,team_id) VALUES ("77","7");
INSERT INTO user_team_map (user_id,team_id) VALUES ("48","18");
INSERT INTO user_team_map (user_id,team_id) VALUES ("27","15");
INSERT INTO user_team_map (user_id,team_id) VALUES ("64","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("34","12");
INSERT INTO user_team_map (user_id,team_id) VALUES ("1","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("8","6");
INSERT INTO user_team_map (user_id,team_id) VALUES ("66","16");
INSERT INTO user_team_map (user_id,team_id) VALUES ("93","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("89","9");
INSERT INTO user_team_map (user_id,team_id) VALUES ("17","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("22","2");
INSERT INTO user_team_map (user_id,team_id) VALUES ("2","17");
INSERT INTO user_team_map (user_id,team_id) VALUES ("20","5");
INSERT INTO user_team_map (user_id,team_id) VALUES ("37","8");
INSERT INTO user_team_map (user_id,team_id) VALUES ("20","3");
INSERT INTO user_team_map (user_id,team_id) VALUES ("74","14");
INSERT INTO user_team_map (user_id,team_id) VALUES ("100","20");
INSERT INTO user_team_map (user_id,team_id) VALUES ("39","13");
INSERT INTO user_team_map (user_id,team_id) VALUES ("42","15");




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

INSERT INTO issue_types VALUES(1, "slug1", null, "Direct");
INSERT INTO issue_types VALUES(2, "slug2", null, "JIRA");
INSERT INTO issue_types VALUES(3, "slug3", null, "Pager Duty");




# --- !Downs