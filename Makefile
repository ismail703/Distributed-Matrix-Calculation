SHELL=cmd.exe
.SHELLFLAGS=/c

all: compile run

compile:
	javac DivideMatrix.java DivideMatrixImpl.java Server.java Slave.java Client.java

run: run_server run_slaves run_client

run_server:
	start "Server" cmd /k "java Server"

run_slaves:
	start "Slave 5048" cmd /k "java Slave 5048"
	start "Slave 5049" cmd /k "java Slave 5049"
	start "Slave 5105" cmd /k "java Slave 5105"
	start "Slave 5205" cmd /k "java Slave 5205"

run_client:
	timeout /t 5 > nul
	start "Client" cmd /k "java Client"

clean:
	del /Q *.class

.PHONY: all compile run run_server run_slaves run_client clean