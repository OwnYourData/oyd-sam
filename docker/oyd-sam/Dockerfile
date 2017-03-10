FROM ruby:2.3
MAINTAINER "Christoph Fabianek" christoph@ownyourdata.eu

ARG APP_INSTALL=false

RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		postgresql-client postgresql-contrib \
	&& rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/OwnYourData/oyd-sam.git --branch sam-rails \
	&& cd /oyd-sam \
    && N=0 \
    && until [ ${N} -ge 5 ]; do bundle install  && break; echo 'Try bundle again ...'; N=$[${N}+1]; sleep 1; done \
	&& su postgres -c "/usr/lib/postgresql/9.4/bin/initdb -D /var/lib/postgresql/data" \
	&& mkdir -p /oyd-sam/script

COPY script/run.sh /bin/run.sh
COPY script/*.sql /oyd-sam/script/
COPY script/*.rb /oyd-sam/script/

RUN chmod +x /bin/run.sh

EXPOSE 3000

CMD ["/bin/run.sh"]
