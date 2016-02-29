OBJS = Alignment.o Alphabet.o DistanceMatrix.o GAP.o LocalSearch.o Node.o SubstitutionMatrix.o Tree.o main.o
CC = g++
DEBUG = -g
prefix = /usr/local
bindir = $(prefix)/bin
CFLAGS = -Wall -w -c
LFLAGS = -Wall -w

M-SALSA: $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o M-SALSA

main.o: main.cpp DistanceMatrix.h SubstitutionMatrix.h Alphabet.h Node.h Tree.h Alignment.h GAP.h LocalSearch.h
	$(CC) $(CFLAGS) main.cpp

Alignment.o: Alignment.cpp Alignment.h DistanceMatrix.h SubstitutionMatrix.h Alphabet.h Tree.h Node.h GAP.h
	$(CC) $(CFLAGS) Alignment.cpp

Alphabet.o: Alphabet.cpp Alphabet.h
	$(CC) $(CFLAGS) Alphabet.cpp

DistanceMatrix.o: DistanceMatrix.cpp DistanceMatrix.h SubstitutionMatrix.h Alphabet.h
	$(CC) $(CFLAGS) DistanceMatrix.cpp

GAP.o: GAP.cpp GAP.h
	$(CC) $(CFLAGS) GAP.cpp

LocalSearch.o: LocalSearch.cpp LocalSearch.h Alignment.h DistanceMatrix.h SubstitutionMatrix.h Alphabet.h Tree.h Node.h GAP.h
	$(CC) $(CFLAGS) LocalSearch.cpp

Node.o: Node.cpp Node.h
	$(CC) $(CFLAGS) Node.cpp

SubstitutionMatrix.o: SubstitutionMatrix.cpp SubstitutionMatrix.h Alphabet.h EmbeddedMatrices.h
	$(CC) $(CFLAGS) SubstitutionMatrix.cpp

Tree.o: Tree.cpp Tree.h Node.h
	$(CC) $(CFLAGS) Tree.cpp

install: M-SALSA
	cp M-SALSA $(bindir)/M-SALSA

uninstall:
	rm $(bindir)/M-SALSA

clean:
	\rm *.o *~ M-SALSA

.PHONY: clean
