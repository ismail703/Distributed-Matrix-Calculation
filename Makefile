all: compile_slaves run_slaves compile_server run_server compile_client run_client

compile_slaves:
	javac Slave.java

run_slaves:
	java Slave 5048 
	java Slave 5049 
	java Slave 5105
	java Slave 5205

compile_server:
	javac DivideMatrix.java
	javac DivideMatrixImpl.java
	javac Server.java

run_server:
	java Server

compile_client:
	javac Client.java

run_client:
	java Client

clean:
	rm -f *.class

.PHONY: all compile_slaves run_slaves compile_server run_server compile_client run_client clean