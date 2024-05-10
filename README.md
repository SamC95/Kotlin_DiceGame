# Android Dice Game Application
## Mobile Application Development - Kotlin

By [Sam Clark](https://github.com/SamC95)

This project was coursework for my second year module: Mobile Application Development -- at the University of Westminster

## Contents
* [Project Brief](https://github.com/SamC95/Kotlin_DiceGame#project-brief)
* [Approach](https://github.com/SamC95/Kotlin_DiceGame#approach)
* [Technologies](https://github.com/SamC95/Kotlin_DiceGame#technologies)
* [Implementation](https://github.com/SamC95/Kotlin_DiceGame#implementation)
* [Key Learnings](https://github.com/SamC95/Kotlin_DiceGame#key-learnings)
* [Achievements](https://github.com/SamC95/Kotlin_DiceGame#achievements)
* [Challenges](https://github.com/SamC95/Kotlin_DiceGame#challenges)
* [Conclusions](https://github.com/SamC95/Kotlin_DiceGame#conclusions)

## Project Brief
### Core Aims

* You are required to implement an Android application using Kotlin.
* You are not allowed to use third party libraries. The only libraries that you can use are the standard Android API libraries.
* Your task is to implement a dice game Android application and according to the game rules and implementation details below.
* Your program will be displaying images of the dice thrown by the two players: the human and the computer.

### Game Rules
* A human player competes against the computer. Both players throw 5 dice at the same time.
The score of each throw for each player is the sum of the numbers shown on the faces of the
dice. The objective of the game is to reach a score of 101 or more (instead of 101 another target
can be set by the human before play starts) by throwing 5 dice as many times as necessary.
* After a roll, each player may either score it or take up to two optional rerolls. For each reroll,
they may reroll all of the dice or select any of the dice to keep and only reroll the remainder.
A player may score at any time, thus ending their current throw; after the second reroll (three
rolls in total) they must score it.
* After both players score their rolls, the procedure is repeated until one of the players reach
101 or more points by summing all of their scores. If both players reach 101 or more with
the same number of attempts (a single attempt is considered as one roll followed by 2 optional
rerolls) the player with the highest score wins.
* As an example, assume that the human player scores 30 points in the first attempt (a roll
followed by 2 optional rerolls), 25 in the second, 11 in the third, 28 in the fourth and 15 in
the fifth achieving a total score of 109 in five attempts. If the computer did not score 101 in
four attempts or more than 109 in five attempts, the human wins. In the case that both the
computer and the human achieved the same score in the five attempts (i.e. 109 in the example),
each player throws for a last time all five dice and the player with the highest sum in that roll
wins (no optional rerolls are allowed in this case). In the case of a tie again, both players keep
rethrowing all five dice until one of them wins.

### Specification
* When the application starts, it presents the user with 2 buttons labelled New Game, and About.
* Clicking on the New Game button, the user will be presented with the game screen which
they interact with.
* The screen should contain 2 buttons labelled Throw and Score (additional buttons could
be included for the additional tasks described below).
* Each time the Throw button is pressed, a simulation of throwing 5 dice by both the human
player and the computer is performed simultaneously:
The images of the five dice rolled by the human player and the five dice rolled by the
computer are displayed.
* Each of the dice images should be selected randomly with an outcome of a number between
1 to 6. Not all of them need be unique, as one roll of 5 dice may result in 6, 1, 4, 4, 2, i.e.
the same outcome representing 4 on the face of a die could be generated for two different
dice.
* The human player may choose to score by pressing a button Score or take up to two
optional rerolls (see below). As soon as the player clicks on Score the total score for
the current game should be updated for both the human player and the computer player.
* The current total score for the game (both human and computer) is displayed on the top
right of the screen. If the user performs the maximum of 3 rolls for that turn, the score is updated automatically without the need to press the Score button.
* For each of the 2 optional rerolls, the human player should be able to select (it is left up
to you to design the appropriate user interface for this) which dice (if any) he would like
to keep for that roll. After selecting this, the human player should press the Throw button
again and the dice which have not been selected for keeping should be rerolled.
* The computer player follows a random strategy. I.e. first it decides randomly whether it
would like to reroll (up to a maximum of 3 rolls per time) and if this is the case it decides
randomly which dice to keep. A single (first) roll for the computer player occurs and it is
displayed only after the human player clicks on the Throw button.
If the human player clicks on the Score button, the computer player uses all of its remaining
rolls for that turn according to the random strategy, i.e. the final result of the five dice
is displayed after the computer has used (optionally based on the random strategy) the 2
rerolls (for a total maximum of 3 rolls).
* When a player (human or computer) reaches 101 or more points a pop up window with
the message “You win!” is displayed in green colour (if the human wins) or “You lose”
in red colour (if the computer wins).
* As soon as a winner is determined, the game is not playable any more. In order to start
a new game, the user needs to press the Android “Back” button to move to the initial
screen of the application from where a new game can be started by pressing the New Game
button.
* Implement what happens in the case of a tie according to the exact rules of the game
* Extend your implementation by allowing the human player to set the number of points
which is the target to reach for the winning player.
* Extend your implementation so that the total number of wins for the human and computer
players is displayed on the top left of the playable screen in the form H:10/C:5 where 10
is the total number of human wins and 5 is the total number of computer wins. The score does not need to be persisted
using a file or other way, as soon as the applicationis exited the score should be lost and the next time that the application is
restarted thescore starts with 0/0.
* Design and implement an efficient (as optimum as possible) strategy for the computer
player strategy. The strategy you design should determine whether the computer player
should reroll some of its dice and which ones (up to a maximum of 2 rerolls - total maximum
of rolls is 3).
You should assume that the computer player cannot see the current dice of the human
player displayed in the application, but it is aware of (i.e. it can see) the current total of
the current game for both itself and the human player, i.e. the computer player knows
that the score of the current game is, e.g. 79 − 93.
* For all the tasks the application should behave in a user friendly manner when the device
is rotated from portrait to landscape and back to portrait mode. I.e. the application
should resume from exactly the same point (same screen and data) when the orientation
changes. The rotation of the device should not change what the user was seeing before
the rotation and the state of the application including the score should be fully restored.

## Approach

Since the project is very pre-defined in terms of expected behaviour and game logic, the primary approach was to implement these features exactly as described in the brief.

However, I did decide to try to gain some experience utilising basic animations for when the user decides to select a dice to not re-roll, so that they would move up to reflect that they are active.
This was not in the design brief but seemed like a natural way to display this to the user whilst also learning an extra aspect of Kotlin development.

Another aspect was the requirement in the brief to implement an efficient strategy for the Computer AI to utilise in the game. I will discuss this further in the Implementation section below.

## Technologies

![Static Badge](https://img.shields.io/badge/Kotlin-%237F52FF?style=for-the-badge&logo=Kotlin&logoColor=white)  
![Static Badge](https://img.shields.io/badge/Android%20Studio-%233DDC84?style=for-the-badge&logo=Android%20Studio&logoColor=white)

## Implementation

### Computer Strategy

As mentioned in the Approach section, one aspect of the program was the implementation of some logic for the Computer in how it decides to handle re-rolls.
The regular requirement was for an entirely random logic for the Computer, however this section expected the Computer to be able to see the current score of the game
and how close it is to winning and determine its roll based on this.

I implemented the functionality so that the computer will check the current scores and determine if the dice it currently has would result in a winning score. It also checks
how far behind the player is in score compared to it to determine if it should re-roll or not.

I feel this aspect of the implementation was effective, however I could have implmeneted more functionality for the scenario where the computer is behind the player and is
taking more risk to catch up.

```kotlin
 fun computerLogical(buttons: List<ImageButton>, images: LinkedHashSet<Int>, compRolls: Int,
                    totalPlayScore: Int, totalCompScore: Int, winningScore: Int,
                    scorePressed: Boolean, currentRoundScore: Int): Int {
        var score = 0
        var remainingRolls = compRolls

        if (!scorePressed && (remainingRolls == 2)) {
            remainingRolls = 1
        }

        while (remainingRolls != 0) {
            if (((currentRoundScore + totalCompScore) >= winningScore) &&
                (totalPlayScore !in (totalCompScore - 10 until totalCompScore) &&
                        totalPlayScore < totalCompScore)) {
                return currentRoundScore
            }
            else {
                score = 0
                buttons.forEachIndexed { _, button ->
                    val tag = button.tag as? Int

                    for (i in 0 until 3) {
                        if (tag == images.elementAt(i)) {
                            val randomiseDice = (0 until 6).random()
                            val selectedImage = images.elementAt(randomiseDice)

                            button.setImageResource(selectedImage)
                            button.tag = selectedImage
                        }
                    }
                }
            }
            remainingRolls--
        }
```

### Application Screenshots

<p float="left">
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/db03ae07-34a4-475b-8b93-5e8364d68e5b" width="250" />
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/04815f5a-2269-4a6c-b7ab-4f5a402b54c0" width="250" />
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/26a13939-a602-4d89-848d-0d92a3dbc5ab" width="250" />
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/15abb784-c473-48a4-b6f9-2b41d6f8452c" width="250" />
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/b483a033-8bd5-451f-9a3c-d4f1627e8522" width="250" />
  <img src="https://github.com/SamC95/Kotlin_DiceGame/assets/132593571/63710038-c7f8-400c-8cee-083a2b0b912d" width="250" />
</p>

## Key Learnings
* First project using Kotlin and Android Studio, so I learnt the basics on utilising this language and designing the UI utilising XML.
* Learnt some basic game programming logic for the first time.
* Learnt how to handle retaining data and appearance on orientation change (Vertical to Horizontal display and vice versa) by saving UI states.

## Achievements
* Managed to effectively implement the core functionality laid out in the project brief based on the specification.
* Gained a lot of understanding on using Kotlin, which allowed for me to improve my approach to future projects.

## Challenges
* I feel that my implementation of the Computer Logic could have been better implemented to have it risk more to catch up when the player is ahead.
* I had a bug in the application where it would crash if the target points textview was left empty when starting a match.
* Overall, the core challenge I felt took longer than I expected was figuring out how to correctly retain data on the orientation changing; this took some time for me to get my head around how to implement.

## Conclusions
Overall, I feel that this project helped to teach me the basics of Kotlin development and also allowed me to gain some understanding of basic game logic. There are definitely aspects that I would refine or improve
such as the computer strategy, fixing bugs and improving the overall style of the application.
