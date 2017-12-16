h = 
s = 

target = java -cp target/npuzzle-1.0-SNAPSHOT-jar-with-dependencies.jar Main

all:
	@mvn install
	@mvn package
	@mvn compile

clean:
	@mvn clean

re: clean all

run:
	${target} -h $h -s $s
