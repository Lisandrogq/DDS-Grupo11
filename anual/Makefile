.PHONY: help 

SHELL := /bin/bash
OS := $(shell uname -s)

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

compile: ## Compiles the project
	mvn package -DskipTests
	mvn compile

start: ## Start the java server
	mvn exec:java -Dexec.mainClass="org.grupo11.app.App"

start_and_compile: compile start ## Starts and compiles the java server