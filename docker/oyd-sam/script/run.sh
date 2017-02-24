#!/bin/bash

# install gems
# loop according to http://www.zhuwu.me/blog/posts/solve-gem-installation-timeout-when-building-docker-image
cd /oyd-sam
N=0
until [ ${N} -ge 5 ]
do
  bundle install  && break
  echo 'Try bundle again ...'
  N=$[${N}+1]
  sleep 1
done
rake rails:update:bin

#start postgreSQL
echo "starting postgreSQL ... "
su postgres -c "/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/data &"
sleep 5

echo "creating DB"
su postgres -c "/usr/lib/postgresql/9.4/bin/psql --command \"ALTER USER postgres WITH SUPERUSER PASSWORD 'postgres';\""
su postgres -c "/usr/lib/postgresql/9.4/bin/psql -d postgres -a -f /oyd-sam/script/template.sql"

echo "starting SAM"
cd /oyd-sam
RAILS_ENV=production rake db:create
RAILS_ENV=production rake db:migrate
if $APP_INSTALL; then
	rails r -e production /oyd-sam/script/bank.rb
	rails r -e production /oyd-sam/script/allergy.rb
	rails r -e production /oyd-sam/script/room.rb
	rails r -e production /oyd-sam/script/scheduler.rb
	rails r -e production /oyd-sam/script/webhistory.rb
	rails r -e production /oyd-sam/script/collect.rb
fi
rails server -e production -b 0.0.0.0

# keep the stdin
/bin/bash
