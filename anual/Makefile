.PHONY: help 

SHELL := /bin/bash
OS := $(shell uname -s)

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

compile: ## Compiles the project
	mvn package -DskipTests

docker-up: ## Starts docker containers
	docker-compose up -d

docker-down: ## Stops and removes docker containers
	docker-compose down

docker-clean: docker-down ## Remove containers, images and volumes related to fridgebridge
	docker volume rm fridge-bridge_postgres_data
	docker rmi postgres rabbitmq adminer

start-server: ## Start the java server
	mvn exec:java -Dexec.mainClass="org.grupo11.app.App"

start: docker-up compile start-server ## Starts docker services, compiles and starts the java server

mvn-clean: ## Cleans mvn, useful when mvn is weird
	mvn clean install -DskipTests

test: ## Mvn test
	mvn test
