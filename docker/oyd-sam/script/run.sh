#!/bin/bash

#start postgreSQL
echo "starting postgreSQL ... "
su postgres -c "postgres -D /var/lib/postgresql/data &"
sleep 5

echo "creating DB"
su postgres -c "psql --command \"ALTER USER postgres WITH SUPERUSER PASSWORD 'postgres';\""
su postgres -c "createdb postgres"

echo "starting SAM"
cd /oyd-sam
rails server -e production -b 0.0.0.0

# keep the stdin
/bin/bash
