

all: libschewchuck${ARCH}.so

JNI=${JAVA_HOME}/include
PLATFORM=linux

CFLAGS=-m${ARCH} -fPIC -Wall -g -I${JNI} -I${JNI}/${PLATFORM}
LDFLAGS=-m${ARCH}

%${ARCH}.o: %.c
	${CC} -c ${CFLAGS} ${SPPFLAGS} $< -o $@

libschewchuck${ARCH}.so: schewchuck${ARCH}.o predicates${ARCH}.o
	${CC} -shared -m${ARCH} -Wl,-soname,$@ -o $@ $^ 

clean:
	${RM} *.o *.so
