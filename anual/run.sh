mvn package -DskipTests
mvn compile

jshell --class-path target/anual-1.0.0.jar init.jsh