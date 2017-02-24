#!/bin/bash

APP="oyd-sam"

# read commandline options
BUILD_CLEAN=false
DOCKER_UPDATE=false
REFRESH=false
VAULT_UPDATE=false
while [ $# -gt 0 ]; do
    case "$1" in
        --clean*)
            BUILD_CLEAN=true
            ;;
        --dockerhub*)
            DOCKER_UPDATE=true
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
            echo "  --clean           baut neues Docker-Image (--no-cache, alles neu kompilieren)"
            echo " "
            echo "Beispiele:"
            echo " ./build.sh --clean --dockerhub --vault"
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
        svn checkout https://github.com/OwnYourData/oyd-pia/trunk/docker/$APP
        echo "refreshed"
        cd ~/docker/$APP
        return
    else
        echo "you need to source the script for refresh"
        exit
    fi
fi
