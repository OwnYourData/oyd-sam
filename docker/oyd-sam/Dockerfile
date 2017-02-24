FROM ruby:2.3
MAINTAINER "Christoph Fabianek" christoph@ownyourdata.eu

RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		postgresql-client postgresql-contrib \
	&& rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/OwnYourData/oyd-sam.git --branch sam-rails \
	&& cd /oyd-sam \
	&& gem install nokogiri -v '1.6.7.2' \
	&& gem install pg -v '0.18.2' \
	&& gem install rdoc -v '4.2.2' \
	&& gem install sdoc -v '0.4.0' \
	&& gem install concurrent-ruby -v '1.0.2' \
	&& gem install equalizer -v '0.0.11' \
	&& gem install hashie -v '3.4.6' \
	&& gem install sqlite3 -v '1.3.9' \
	&& gem install multi_json -v '1.12.1' \
	&& gem install therubyracer -v '0.12.2' \
	&& gem install activesupport -v '4.2.5' \
	&& gem install axiom-types -v '0.1.1' \
	&& bundle install

RUN cd /oyd-sam \
	&& rake rails:update:bin \
	&& su postgres -c "initdb -D /var/lib/postgresql/data" 

COPY script/run.sh /bin/run.sh
RUN chmod +x /bin/run.sh

EXPOSE 3000

CMD ["/bin/run.sh"]
