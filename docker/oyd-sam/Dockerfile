FROM ruby:2.3
MAINTAINER "Christoph Fabianek" christoph@ownyourdata.eu

RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		postgresql-client postgresql-contrib \
	&& rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/OwnYourData/oyd-sam.git --branch sam-rails \
	&& cd oyd-sam \
	&& bundle install \
	&& rake rails:update:bin \
	&& su postgres -c "initdb -D /var/lib/postgresql/data" 

COPY script/run.sh /bin/run.sh
RUN chmod +x /bin/run.sh

EXPOSE 3000

CMD ["/bin/run.sh"]
