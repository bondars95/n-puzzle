all:
	@mvn install
	@mvn package
	@mvn compile

clean:
	@mvn clean

re: clean all
