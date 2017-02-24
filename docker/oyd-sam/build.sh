#!/bin/bash

APP="oyd-sam"
APP_NAME="sam"
IMAGE="oydeu/$APP"

# read commandline options
APP_INSTALL=false
BUILD_CLEAN=false
DOCKER_UPDATE=false
REFRESH=false
VAULT_UPDATE=false
while [ $# -gt 0 ]; do
    case "$1" in
        --apps*)
            APP_INSTALL=true
            ;;
        --clean*)
            BUILD_CLEAN=true
            ;;
        --dockerhub*)
            DOCKER_UPDATE=true
            ;;
        --name=*)
            APP_NAME="${1#*=}"
            ;;
        --refresh*)
            REFRESH=true
            ;;
        --vault*)
            VAULT_UPDATE=true
            ;;
        --help*)
            echo "Verwendung: [source] ./build.sh  --options"
            echo "erzeugt und startet OwnYourData SAM"
            echo " "
            echo "Optionale Argumente:"
            echo "  --apps            initialisiert die Datenbank mit vorhanden OwnYourData Apps"
            echo "  --clean           baut neues Docker-Image (--no-cache, alles neu kompilieren)"
            echo "  --dockerhub       pusht Docker-Image auf hub.docker.com"
            echo "  --refresh         aktualisiert docker Verzeichnis von Github"
            echo "                    (Achtung: löscht alle vorhandenen Zwischenschritte)"
            echo "  --vault           startet Docker Container auf datentresor.org"
            echo " "
            echo "Beispiele:"
            echo " ./build.sh --clean --dockerhub --vault --apps"
            echo " ./build.sh --name=beta-sam --vault"
            if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
                return 1
            else
                exit 1
            fi
            ;;
        *)
            printf "unbekannte Option(en)\n"
            if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
                return 1
            else
                exit 1
            fi
    esac
    shift
done

if $REFRESH; then
    if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
        cd ~/docker
        rm -rf $APP
        svn checkout https://github.com/OwnYourData/oyd-sam/branches/sam-rails/docker/$APP
        echo "refreshed"
        cd ~/docker/$APP
        return
    else
        echo "you need to source the script for refresh"
        exit
    fi
fi

if $BUILD_CLEAN; then
    docker build --no-cache -t $IMAGE --build-arg APP_INSTALL=$APP_INSTALL .
else
	docker build -t $IMAGE --build-arg APP_INSTALL=$APP_INSTALL .
fi

if $DOCKER_UPDATE; then
    docker push $IMAGE
fi

if $VAULT_UPDATE; then
	docker stop $APP_NAME
	docker rm $(docker ps -q -f status=exited)
	docker run --name $APP_NAME -d --expose 3000 -e VIRTUAL_HOST=$APP_NAME.datentresor.org -e VIRTUAL_PORT=3000 $IMAGE
fi
