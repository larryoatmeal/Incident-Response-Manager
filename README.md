
## Setup ##

Get Play 2.2.2 http://downloads.typesafe.com/play/2.2.2/play-2.2.2.zip

## Development ##

<pre>
cd conf/evolutions
ln -s h2 default
play -Dconfig.file=conf/test-h2.conf -DapplyEvolutions.test=true
</pre>

## Production ##

Create a MySQL database for the incident response manager

<pre>
create database irm;
grant all on *.* to 'playapp'@'localhost' identified by 'w00t';
</pre>

Create conf/prod-mysql.conf - use conf/sample-mysql.conf as a basis

<pre>
cd conf/evolutions
ln -s mysql default
play -Dconfig.file=conf/prod-mysql.conf
</pre>

## TODO ##

* improve exception and other error handling
* move components that are calling queries on the models out of the views and into the controllers
* "next update at" should default to now + some default interval
* embed a solr server for more free form search
