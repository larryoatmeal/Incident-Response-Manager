
## Setup ##

Get Play 2.2.2 http://downloads.typesafe.com/play/2.2.2/play-2.2.2.zip

## Development ##

<pre>
ln -s conf/evolutions/h2 conf/evolutions/default
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
play -Dconfig.file=conf/prod-mysql.conf
</pre>

## TODO ##

* embed a solr server for more free form search
