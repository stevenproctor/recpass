SHELL = /bin/bash

# This is the default target because it is the first real target in this Makefile
.PHONY: default # Same as "make ci"
default: run-app-server

.PHONY: run-app-server # starts a jetty application server defaults to port 3456, can be overriden with RECPASS_PORT environment variable
run-app-server: RECPASS_PORT ?= 3456
run-app-server:
	clj -M:main -p $(RECPASS_PORT)

# Copied from: https://github.com/jeffsp/makefile_help/blob/master/Makefile
# Tab nonesense resolved with help from StackOverflow... need a literal instead of the \t escape on MacOS
help: # Generate list of targets with descriptions
	@grep '^.PHONY: .* #' Makefile | sed 's/\.PHONY: \(.*\) # \(.*\)/\1	\2/' | expand -t20


