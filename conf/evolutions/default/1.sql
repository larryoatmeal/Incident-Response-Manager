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

	PRIMARY KEY(id),
	UNIQUE (email)
);


INSERT INTO users (id,first_name,last_name,email) VALUES ("1","Mercedes","Pittman","Nunc.ut.erat@gravidaAliquamtincidunt.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("2","Astra","Burke","eget@dignissimpharetraNam.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("3","Cleo","Haynes","nisi.Mauris@dui.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("4","Davis","Ramos","at.nisi@gravida.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("5","Beatrice","Harrington","dolor.Nulla@Donec.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("6","Rhoda","Alvarez","erat.volutpat@vitae.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("7","Armando","Jensen","faucibus.Morbi.vehicula@antedictumcursus.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("8","Susan","Vargas","amet.ante@utmiDuis.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("9","Philip","Flores","habitant.morbi@auctorveliteget.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("10","Rina","Boone","justo.sit@nec.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("11","Dakota","Reid","lectus@nisi.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("12","Blythe","Bond","adipiscing@a.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("13","Merrill","Murray","pellentesque.a.facilisis@Vivamuseuismodurna.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("14","Marcia","Gomez","bibendum.Donec.felis@etlacinia.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("15","Keith","Glenn","nunc.interdum@tempus.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("16","Gage","Lloyd","adipiscing.non@acturpis.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("17","Madeson","Little","libero.at@non.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("18","Caleb","Nicholson","sit.amet.consectetuer@purus.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("19","Ivory","Cooley","vestibulum@ornareelit.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("20","Martha","Horne","nec.euismod.in@utodiovel.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("21","Logan","Fleming","in.faucibus.orci@aliquetliberoInteger.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("22","Alexander","Pierce","aliquet@Nullafacilisi.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("23","Honorato","Bird","sem@est.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("24","Rose","Espinoza","ante.lectus@nislMaecenasmalesuada.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("25","Nerea","Sexton","porttitor.eros.nec@scelerisque.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("26","Willa","Castaneda","nec@tinciduntadipiscingMauris.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("27","Duncan","King","velit@ornarelectus.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("28","September","Reynolds","Etiam.bibendum@risus.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("29","Pearl","Gregory","luctus.sit@odio.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("30","Ferris","Clemons","vehicula@aclibero.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("31","Connor","Middleton","purus.ac@ridiculus.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("32","Neil","Savage","ultricies.ligula.Nullam@dolorDonec.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("33","Lysandra","Bradshaw","diam@Integervulputate.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("34","Matthew","Gaines","risus.odio@etnetus.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("35","Scarlett","Coleman","accumsan.sed@ornaretortorat.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("36","Vincent","Hood","Cras.dolor.dolor@nibh.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("37","Serina","Mckay","purus.Duis@nasceturridiculusmus.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("38","Cameran","Marquez","nonummy.ultricies@ametloremsemper.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("39","Harlan","Barton","non@eu.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("40","Amir","Houston","Lorem.ipsum.dolor@Donecporttitortellus.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("41","Cairo","Bauer","dolor.sit.amet@semmolestie.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("42","Gavin","Simpson","luctus@luctusvulputate.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("43","Elliott","Brewer","lectus@egestas.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("44","Harding","Moran","vestibulum@ipsum.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("45","Yael","Mayer","eros@Donecelementum.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("46","Dorothy","Abbott","metus.urna@acmieleifend.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("47","Demetrius","Phelps","sed.libero@dolorFuscemi.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("48","Xena","Meadows","lorem@Aliquam.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("49","Gareth","Fitzgerald","mi@Duissitamet.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("50","Jolie","Bishop","feugiat.Sed@nonummy.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("51","Ulysses","Valentine","at.augue@dapibusgravida.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("52","Janna","Hines","varius.orci.in@Fusce.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("53","Elaine","Green","pede.malesuada@feugiatSed.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("54","Karyn","Acevedo","Integer.aliquam@tortor.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("55","Cairo","Alford","tristique@fermentummetusAenean.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("56","Lacey","Mccall","tincidunt.pede.ac@dolor.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("57","Alma","Workman","sed@veliteu.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("58","Abel","Mcclure","magnis.dis.parturient@nostraper.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("59","Mechelle","Cline","imperdiet@a.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("60","Breanna","Bullock","montes@cursusIntegermollis.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("61","Nell","Zamora","Donec@nonbibendumsed.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("62","Gareth","Dillon","dui@sitametluctus.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("63","Holmes","Chavez","malesuada.fames@euneque.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("64","Kenyon","Carrillo","Etiam.bibendum.fermentum@ac.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("65","Winifred","Vinson","bibendum@nonummyFuscefermentum.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("66","Tyrone","Morrow","risus.quis@elementumduiquis.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("67","Noelle","Greene","dolor.Donec@cursuspurus.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("68","Susan","Lucas","elit@lacusQuisqueimperdiet.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("69","Winter","Munoz","ut.dolor@estvitae.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("70","Kareem","Oneil","vestibulum.neque.sed@Integerin.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("71","Cora","Stokes","porttitor@nec.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("72","Quinlan","Mcmahon","parturient@feugiat.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("73","Sheila","Buckner","a@magnaetipsum.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("74","Ginger","Ayala","Duis@vehiculaPellentesquetincidunt.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("75","Amir","Osborne","vel.sapien.imperdiet@Morbivehicula.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("76","Roanna","Ayala","elit@ametdapibus.com");
INSERT INTO users (id,first_name,last_name,email) VALUES ("77","Rebecca","Schroeder","Praesent@arcuCurabiturut.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("78","Noah","Abbott","tristique@dui.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("79","Paloma","Riddle","et@lacusMauris.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("80","Aline","Kelly","eu@molestiearcu.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("81","Cameran","Barron","Suspendisse.non@porttitorvulputateposuere.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("82","Rinah","Hale","Cum@fermentumfermentum.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("83","Hilda","Perkins","cursus.a@egestaslacinia.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("84","Wendy","Whitehead","in.dolor.Fusce@diamluctus.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("85","Yael","Rojas","ante@mollislectuspede.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("86","Nero","Schroeder","ultrices.sit@lobortis.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("87","Lars","Vang","tellus.imperdiet@Sedeu.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("88","Brian","Spears","ut@Nuncsedorci.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("89","Eaton","Frank","Sed.id.risus@Vivamus.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("90","Valentine","Andrews","vel.venenatis@aduiCras.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("91","Amal","Russo","pretium@idnuncinterdum.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("92","Glenna","Strong","mauris.Suspendisse.aliquet@dictumeuplacerat.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("93","Lance","Schwartz","turpis.egestas@sed.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("94","Dacey","Miranda","turpis.vitae@nonummyac.net");
INSERT INTO users (id,first_name,last_name,email) VALUES ("95","Jocelyn","Beck","Quisque.fringilla.euismod@tinciduntaliquamarcu.edu");
INSERT INTO users (id,first_name,last_name,email) VALUES ("96","Lee","Silva","Nunc.sollicitudin.commodo@lectusconvallisest.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("97","Jeanette","Dillon","egestas.a.scelerisque@Nullam.co.uk");
INSERT INTO users (id,first_name,last_name,email) VALUES ("98","Claire","Brennan","sed@rutrumurnanec.ca");
INSERT INTO users (id,first_name,last_name,email) VALUES ("99","Halee","Hopper","egestas.blandit@sitamet.org");
INSERT INTO users (id,first_name,last_name,email) VALUES ("100","Grady","Hall","non.leo@In.co.uk");




DROP TABLE IF EXISTS teams;

CREATE TABLE teams (
	id 					int 			NOT NULL AUTO_INCREMENT,
	name 				varchar(256)	NOT NULL,

	PRIMARY KEY(id),
	UNIQUE(name)
);

INSERT INTO teams (id,name) VALUES ("1","Sit Amet Inc.");
INSERT INTO teams (id,name) VALUES ("2","Neque Sed Sem Company");
INSERT INTO teams (id,name) VALUES ("3","Sed Nulla Ante Limited");
INSERT INTO teams (id,name) VALUES ("4","Montes Industries");
INSERT INTO teams (id,name) VALUES ("5","Integer Sem Elit PC");
INSERT INTO teams (id,name) VALUES ("6","Iaculis Odio Nam Inc.");
INSERT INTO teams (id,name) VALUES ("7","Metus Facilisis Lorem PC");
INSERT INTO teams (id,name) VALUES ("8","Dictum LLP");
INSERT INTO teams (id,name) VALUES ("9","Neque Sed Limited");
INSERT INTO teams (id,name) VALUES ("10","Euismod Mauris Eu Incorporated");
INSERT INTO teams (id,name) VALUES ("11","Cras Vulputate LLP");
INSERT INTO teams (id,name) VALUES ("12","Donec Porttitor Tellus Incorporated");
INSERT INTO teams (id,name) VALUES ("13","Morbi Non Sapien Associates");
INSERT INTO teams (id,name) VALUES ("14","Felis Ullamcorper Limited");
INSERT INTO teams (id,name) VALUES ("15","Sed Nunc Ltd");
INSERT INTO teams (id,name) VALUES ("16","Diam Limited");
INSERT INTO teams (id,name) VALUES ("17","Neque LLC");
INSERT INTO teams (id,name) VALUES ("18","Et Malesuada Fames LLP");
INSERT INTO teams (id,name) VALUES ("19","At Arcu Incorporated");
INSERT INTO teams (id,name) VALUES ("20","Integer In Magna Ltd");




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