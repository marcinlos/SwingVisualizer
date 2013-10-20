

all: libschewchuck.so

JNI=${JAVA_HOME}/include
PLATFORM=linux

CFLAGS=-m32 -fPIC -Wall -g -I${JNI} -I${JNI}/${PLATFORM}
LDFLAGS=-m32

libschewchuck.so: schewchuck.o predicates.o
	${CC} -shared -m32 -Wl,-soname,$@ -o $@ $^ 

clean:
	${RM} *.o *.so
