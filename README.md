# Engine Tournament Application
## Introduction (Read me first)

This software is a personal self learning project I was asked to share the code of. This software has no tests, 
is unfinished and is definetely not production quality. So take it with a grain of salt.

It also cannot be easily updated to a later Spring Boot Version, because I used deprecated and now removed framework
features.

I used it to write my first application using the Reactive paradigm, specially the rapid data transfer from server
to browser using Server Sent Events instead of polling. And geee, that is really impressive :-D

## How to run

This is a Spring Boot Application written in Java using Maven as dependency management. Import it into a Java IDE of 
your choice (I used IntelliJ) and then edit the resources/application.yml and define at least two UCI Chess engines
including their exectable path on your machine. If you don't know what to download:

Stockfish (Open Source, World Stronges Engine, uses CPU): https://stockfishchess.org/download/
LC0 (Open Source AI Chess Engine by Google, uses GPU): https://github.com/LeelaChessZero/lc0

Or just google one of the Engines in the application.yml - They are mostly Open Source or at least free to use.

After editing the application.yml run the application and point your Browser to http://localhost:8080/index.html

Set up the game in the GUI (MultiPV is how many trains of thoughts are displayed)

## Frontend

The Source Code of the frontend is not included. There's only a compiled version in the static folder. If you
need access to the frontend code, too, drop me an issue.

## Links

To understand the optput of the engines displayed in the log study the Universal Chess Interface (UCI):

https://www.shredderchess.com/download/div/uci.zip

To understand how to use a different start position:

If you want to see a funny but balanced starting position I dicovered during testing:

Try: RNbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBnr w Qk - 0 1

If you want to learn how to set up an own position:

https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
