.PHONY: help 

SHELL := /bin/bash
OS := $(shell uname -s)
ENVIRONMENT ?= dev

ifeq ($(ENVIRONMENT),prod)
	ORIGIN = https://fridgebridge.simplecharity.com
endif

ifeq ($(ENVIRONMENT),dev)
	ORIGIN = http://localhost:8000
endif


help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

__JAVA_SERVER__:
setup_env:
	cp src/main/resources/.env.dev src/main/resources/.env

compile: ## Compiles the project
	mvn package -DskipTests

docker-up: ## Starts docker containers
	docker-compose up -d

docker-down: ## Stops and removes docker containers
	docker-compose down

docker-rm-volumes: ## Removes all docker volumes related to fridbridge 
	docker volume rm fridge-bridge_postgres_data

docker-clean: docker-down docker-rm-volumes ## Removes containers and volumes related to fridgebridge

docker-clean_all: docker-down docker-rm-volumes ## Remove containers, images and volumes related to fridgebridge
	docker rmi postgres rabbitmq adminer
	
start-server: ## Start the java server
	mvn exec:java -Dexec.mainClass="org.grupo11.app.App"

start: docker-up compile start-server ## Starts docker services, compiles and starts the java server

start-prod: compile ## Start server in prod mode
	mvn exec:java -Dexec.mainClass="org.grupo11.app.App"

mvn-clean: ## Cleans mvn, useful when mvn is weird
	mvn clean install -DskipTests

__TEST__:
test: ## Mvn test
	mvn test

gen-mock-data: ## Generates random mocked data in .csv format
	./scripts/mock-data.sh

mock-db: gen-mock-data ## Mocks data into db for dev enviroments, make sure you have postgres running
	./scripts/insert-data.sh

__DOCS__:
docs-install: ## Install a docs deps 
	@cd docs/site && npm install

docs-dev: ## Starts a local dev server for documentation site 
	@cd docs/site && npm run start

docs-build: ## Builds static documentation site
	@cd docs/site && npm run build

docs-serve: ## Serves static build documentation site
	@cd docs/site && npm run serve

docs-deploy: docs-install docs-build docs-serve ## Builds docs and serves documentation 

docs-open-api-to-markdown: ## Create a mardkown specification from open-api yaml, you need to provide FILE_PATH and OUT_PATH 
	widdershins docs/markdown/Api/$(FILE_NAME).yaml -o docs/markdown/Api/$(FILE_NAME).md

WIDDERSHINS_PATH?=$(HOME)/widdershins-docusaurus
docs-install-widdershins: ## Installs widdernshins used to generate .md files from open-api .yml specification
	git clone https://github.com/MarcosNicolau/widdershins-docusaurus.git $(WIDDERSHINS_PATH)
	cd $(HOME)/widdershins-docusaurus && npm install && npm install --global .

docs-remove-widdershins: ## Removes widdernshins from the system
	npm remove --global widdershins
	rm -rf $(WIDDERSHINS_PATH)

__METRICS__:
start-metrics: ## Run prometheus and grafana metrics with docker-compose 
	@echo "Running metrics..."
	docker compose -f ./metrics/metrics.docker-compose.yaml up

metrics-rm-volumes: ## Remove prometheus and grafana volumes 
	docker volume rm fridge-bridge_prometheus_data
	docker volume rm fridge-bridge_grafana_data

metrics-down: ## Remove prometheus and grafana containers 
	docker compose down -f ./metrics/metrics.docker-compose.yaml up

metrics-clean: metrics-down metrics-rm-volumes ## Stop prometheus and grafana containers and remove volumes 

metrics-clean-all: metrics-clean ## Remove prometheus and grafana images 
	docker rmi grafana/grafana prom/prometheus

__DEPLOY__:
deploy: ## Deploys fridgebridge to given host
	infra/scripts/deploy.sh

deploy-upgrade: ## Updates fridgebridge codebase in a host
	infra/scripts/upgrade.sh $(HOST)

SNYK_PATH?=$(HOME)/bin/
install_snyk: ## Installs snyk at SNYK_PATH
	curl --compressed https://downloads.snyk.io/cli/stable/snyk-macos -o snyk
	chmod +x ./snyk
	mv ./snyk $(SNYK_PATH)

uninstall_snyk: ## Uninstalls snyk
	rm $(SNYK_PATH)

snyk_monitor: ## Runs snyk monitor
	snyk monitor --all-projects --org=4a00c9bc-1491-4a6c-ac88-8a56fc4355a2 	

snyk_test: ## Runs snyk code test
	snyk code test --org=4a00c9bc-1491-4a6c-ac88-8a56fc4355a2 

run_snyk: snyk_monitor snyk_test

gen_jwt_ecdsa_keys:
	@./scripts/gen-jwt-ecdsa.sh

create_admin_user:
	./scripts/insert-admin-user.sh $(PWD)/infra/scripts/deploy-conf.json $(OWNER_ID) $(MAIL) $(PASSWORD_HASH)

__API_CALLS__:
api_get_jwt_token:
	./scripts/api-calls/get-access-token.sh $(ORIGIN) $(MAIL) $(PASSWORD)

api_sensor_send_movement:
	./scripts/api-calls/sensor-send-movement.sh $(ORIGIN) $(FRIDGE_ID) $(SENSOR_ID) $(IS_MOVING) $(ACCESS_TOKEN)

api_sensor_send_temperature:
	./scripts/api-calls/sensor-send-temperature.sh $(ORIGIN) $(FRIDGE_ID) $(SENSOR_ID) $(TEMP) $(ACCESS_TOKEN)

api_pin_withdraw_meal:
	./scripts/api-calls/pin-withdraw-meal.sh $(ORIGIN) $(CARD_NUMBER) $(SECURITY_CODE) $(FRIDGE_ID) $(MEALS_ID)

api_pin_get_usages:
	./scripts/api-calls/pin-get-usages.sh $(ORIGIN) $(CARD_NUMBER) $(SECURITY_CODE)
