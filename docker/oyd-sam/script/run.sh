#!/bin/bash

#start postgreSQL
echo "starting postgreSQL ... "
su postgres -c "/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/data &"
sleep 5

echo "creating DB"
su postgres -c "/usr/lib/postgresql/9.4/bin/psql --command \"ALTER USER postgres WITH SUPERUSER PASSWORD 'postgres';\""
su postgres -c "/usr/lib/postgresql/9.4/bin/createdb postgres"

echo "starting SAM"
cd /oyd-sam
rails server -e production -b 0.0.0.0

# keep the stdin
/bin/bash
