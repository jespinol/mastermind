# Mastermind 
[Mastermind](https://en.wikipedia.org/wiki/Mastermind_(board_game)) is a code-breaking game where the player must guess a secret code in a limited number of attempts. The game provides feedback to the player after each guess, which can be used to deduce the secret code. This is a Java implementation of the game.

The central part of the project consists of a game [library](mastermindcore) that provides core game logic. I used Object-Oriented Design to allow the game to be customized. This library can be used to create different implementations of for the game (I will use "**client**" to mean users of the game library, and "**end users**" to mean players of the game). Javadocs for the game library [can be found here](https://jespinol.github.io/mastermindcore_docs/). The customizations that are possible are available to clients by calling options on a Game.Builder.

As examples of client implementations, I wrote a [CLI program](mastermindcli) for playing the game in the terminal. Additionally, I implemented a Spring Boot web application to play the game via HTTP requests. Additional information about the web app project can be found in this [repository](https://github.com/jespinol/mastermindweb).

# Getting started
## Prerequisites
JDK 21.0.2 or later[*](https://jdk.java.net/21/)

Maven 3.9.6 or later[*](https://maven.apache.org/download.cgi)

## Installation

These instructions were tested on a Mac M1 laptop running macOS Sonoma 14.4.1, and in a [Dev Container](https://github.com/devcontainers/templates/tree/main/src/java) running Debian 11 bullseye.

1. Clone the repository
    ```shell
    git clone https://github.com/jespinol/mastermind.git && cd mastermind
    ```
2. From the repository root, run
    ```shell
    mvn clean install
    ```
    This will compile, package into JARs, and install the game library in your local Maven repository, making it available to Mastermind CLI and other implementations.

## Usage of the CLI game

The maven package and maven install commands above use the `maven-assembly-plugin` to create an executable JAR which includes the CLI as well as the game library dependency. 

1. To start the Mastermind CLI, run the following command
   ```shell
   java -jar mastermindcli/target/mastermindcli-1.0-jar-with-dependencies.jar
   ```
   
2. (Optional) Change the game settings by entering `2` in the main menu

3. Start the game by entering `1` in the main menu. The game will provide the specifications for a guess. After each valid guess, feedback will be displayed to help you crack the code.

# Development details
## Project structure
This repo is set up as a [maven multi-module project](https://maven.apache.org/guides/mini/guide-multiple-modules.html):
- `mastermindcore` contains the core game logic. This module defines Game classes which can be used by a client to implement games. The Game.Builder allows a client to customize the game settings, if desired.
- `mastermindcli` is an implementation of the game, which uses the core library as a dependency. This implementation allows an end user to play the game in the terminal.

## Challenges

1. Clearly identifying what were the must-haves in the game library, vs. what should be left to clients
2. Making it possible for clients to create variants of the original Mastermind game without duplication of code
3. Due to not knowing how clients might use/abuse the game library, I aimed to preserve the integrity of Game objects by utilizing access modifiers, immutable types, and defensive copies throughout the game library code, in addition to tests of many edge case scenarios.

## Extensions implemented
Beyond the required minimum viable product, the following features were added:
1. **Game parameter customization**
    - Game length and difficulty: Includes specifying values for length of the code, number of colors, and maximum attempts
    - Code supplier: allows the client implementation to choose how the initial secret code is generated, for example: a randomly generated code or a code provided by the end user
    - Feedback strategy: allows the client implementation to choose the type of the feedback received after each guess
2. **Error handling**: The game rejects invalid inputs and provides error information to the client. Also, the game handles errors that may occur when the CodeSupplier generates a secret code
3. **Spring boot web implementation**: A simple web application was created to demonstrate an implementation of the game that can be played with HTTP requests. It also demonstrates how a client can define its own FeedbackStrategy to create a customized game. [Link to that project here](https://github.com/jespinol/mastermindweb)
4. **Tests**: Unit tests for the game library
5. **API documentation**: Docs and [Javadocs](https://jespinol.github.io/mastermindcore_docs/) for the game library
6. **Ability to use the game library in other projects**: For example, one can run `mvn install` in this project, which installs the library to the local maven reposition Then another project could use the library as a dependency, e.g. a maven project could add the following lines to the project's `pom.xml` file:
   ```xml
   <dependencies>
      <dependency>
         <groupId>org.jmel</groupId>
         <artifactId>mastermindcore</artifactId>
         <version>1.0</version>
      </dependency>
   </dependencies>
   ```

## Future extensions that I did not implement yet
1. **Additional feedback strategies**: these could change the game difficulty or change how the game is played
2. **Multiplayer mode**: to play against the computer
3. **Hint provider**: would provide a suggestion for the next guess
4. **Persistence**: to save game state to continue later
5. **Scores**: measure how long it takes to crack the code and keep track of best scores
6. **CI/CD**: running tests, publishing Javadocs, and other tasks could be run automatically when my repository is updated

<br></br>
***

#### Created by [Jose M. Espinola Lopez](https://github.com/jespinol)



