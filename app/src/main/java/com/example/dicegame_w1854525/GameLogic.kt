package com.example.dicegame_w1854525

import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class GameLogic {
    // Function that randomises the dice images and derives the appropriate score from each
    fun randomise(Buttons: List<ImageButton>, player: Boolean, roundScore: Int,
                  images: LinkedHashSet<Int>, diceActive: MutableList<Int>): Int {
        // Variable for Score so that it can be altered
        var score = roundScore

        /*For each of the Buttons in the list of Buttons, checks if dice is set not to be randomised
          If diceActive is 0, randomise the dice and set the image appropriately
          If diceActive is 1, gets the tag of the current button and determines the score on if
          the tag matches the tag of the image then returns it and continues to next button*/
        Buttons.forEachIndexed { index, button ->
            if ((diceActive[index] == 1) && (player)) {
                // Gets the tag of the button as an integer
                val tag = button.tag as? Int
                /* If tag is not null, checks tag and increases score by given amount if the tag
                     * matches the image, if no match then score does not increase */
                if (tag != null) {
                    score += when (tag) {
                        images.elementAt(0) -> 1
                        images.elementAt(1) -> 2
                        images.elementAt(2) -> 3
                        images.elementAt(3) -> 4
                        images.elementAt(4) -> 5
                        images.elementAt(5) -> 6
                        else -> 0
                    }
                }
                return@forEachIndexed
            }
            // Select a random number from 0 to 5
            val randomNum = (0 until 6).random()
            // Sets the dice image to the appropriate number
            val selectedImage = images.elementAt(randomNum)
            button.setImageResource(selectedImage)
            button.tag = Integer.valueOf(selectedImage)

            // Adds the number of the dice into the score for that round
            when (randomNum) {
                0 -> score += 1
                1 -> score += 2
                2 -> score += 3
                3 -> score += 4
                4 -> score += 5
                5 -> score += 6
            }
        }
        return score
    }

    fun score(throwButton: Button, totalCompScore: Int, totalPlayScore: Int, winningScore: Int,
              scoreTracker: TextView, roundCompScore: Int, roundPlayScore: Int, ): Triple<MutableList<Int>, String, Int> {
        // Variables for the total player and computer scores so that they can be altered
        var compTotal = totalCompScore
        var playTotal = totalPlayScore
        var winChecker = 0

        // List variable for returning the compTotal and playTotal variables
        val totals = mutableListOf<Int>()

        // Sets the total wins for the player/computer
        var totalWinLoss = TotalScore.updateTotal()

        if ((compTotal < winningScore) && (playTotal < winningScore) || (compTotal == playTotal)) {
            // Sets Throw Button text back to 'Throw' if it was changed to 'Re-Roll (2) or 'Re-roll (1)
            if (throwButton.text != "Throw")
                throwButton.text = "Throw"

            // Adds the round scores for player and computer into their total scores respectively
            compTotal += roundCompScore
            playTotal += roundPlayScore
            // Updates text for current total scores on the score tracker
            scoreTracker.text = String.format("H:$playTotal/C:$compTotal")

            // Adds totals to array list for returning
            totals.add(playTotal)
            totals.add(compTotal)

            /*If the player score is above or equal to the winning number,
            * and above the computer's score. Display alert box with You Win message
            * as well as incrementing the player's score on the total wins*/
            if ((playTotal >= winningScore) && (playTotal > compTotal)) {
                winChecker = 1
                totalWinLoss = TotalScore.incrementScore(playerWin = true)
            }
            /*If the computer score is above or equal to the winning number,
            * and above the player's score. Display alert box with You Lose message
            * as well as incrementing the computer's score on the total wins*/
            else if ((compTotal >= winningScore) && (compTotal > playTotal)) {
                winChecker = 2
                totalWinLoss = TotalScore.incrementScore(playerWin = false)
            }
        }
        return Triple(totals, totalWinLoss, winChecker)
    }

    /* Function for computer's randomised re-roll logic, first roll handled by randomise function
       Remaining re-rolls handled by this function */
    fun computerRandom(buttons: List<ImageButton>, images: LinkedHashSet<Int>, compRolls: Int): Int {
        var score = 0
        var remainingRolls = compRolls

        // If score was pressed, computer will run through re-roll process twice, else only once

        while (remainingRolls != 0) {
            score = 0
            // Randomises whether the computer wants to re-roll this turn
            val rollChoice = (0 until 2).random()

            // If re-roll is triggered, remaining rolls is reduced by 1
            if (rollChoice == 1) {
                remainingRolls -= 1
                // For each button, diceRoll determines whether they want to re-roll that dice
                buttons.forEach {
                    val diceRoll = (0 until 2).random()
                    // If result is 1 then dice is re-rolled and the image and tag are appropriately updated
                    if (diceRoll == 1) {
                        val randomiseDice = (0 until 6).random()
                        val selectedImage = images.elementAt(randomiseDice)
                        it.setImageResource(selectedImage)
                        it.tag = Integer.valueOf(selectedImage)
                    }
                }
            }
            // If re-roll is not triggered, remaining rolls is reduced by 1 and nothing else happens
            else
                remainingRolls -= 1
        }
        // Updates the score for the round based on the updated dice tags during the while loop
        buttons.forEachIndexed { _, button ->
            val tag = button.tag as? Int

            if (tag != null) {
                score += when (tag) {
                    images.elementAt(0) -> 1
                    images.elementAt(1) -> 2
                    images.elementAt(2) -> 3
                    images.elementAt(3) -> 4
                    images.elementAt(4) -> 5
                    images.elementAt(5) -> 6
                    else -> 0
                }
            }
        }
        return score
    }

    fun computerLogical(buttons: List<ImageButton>, images: LinkedHashSet<Int>, compRolls: Int,
                    totalPlayScore: Int, totalCompScore: Int, winningScore: Int,
                    scorePressed: Boolean, currentRoundScore: Int): Int {
        var score = 0
        var remainingRolls = compRolls

        /* If user re-rolls with multiple re-rolls remaining, sets remaining rolls to 1 in function
         *  so that they don't re-roll twice in one re-roll, if score is pressed then this is ignored
         *  and the computer will take all its remaining re-rolls */
        if (!scorePressed && (remainingRolls == 2)) {
            remainingRolls = 1
        }

        while (remainingRolls != 0) {
            /* Performs a few checks on the current scores, checks if its current dice would
            *  result in a winning score, and also checks to see how close the player score is
            *  Checks that total player score is less than the computer's total score and
            *  also checks that the player is far enough behind in score.
            *  If all these conditions are met then the computer decides not to re-roll. */
            if (((currentRoundScore + totalCompScore) >= winningScore) &&
                (totalPlayScore !in (totalCompScore - 10 until totalCompScore) &&
                        totalPlayScore < totalCompScore)) {
                return currentRoundScore
            }
            else {
                score = 0
                buttons.forEachIndexed { _, button ->
                    val tag = button.tag as? Int

                    /* Checks that the current dice button's tag does not match with the id of
                       the first three images (dices 1 to 3)
                     * If it does then that dice is re-rolled, if it does not then it is left alone */
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

        /* Updates the score accordingly
         (either the same if no changes were made, or updated with any altered dice) */
        buttons.forEachIndexed { _, button ->
            val tag = button.tag as? Int
            if (tag != null) {
                score += when (tag) {
                    images.elementAt(0) -> 1
                    images.elementAt(1) -> 2
                    images.elementAt(2) -> 3
                    images.elementAt(3) -> 4
                    images.elementAt(4) -> 5
                    images.elementAt(5) -> 6
                    else -> 0
                    }
                }
            }
        return score
    }
}
/*
---------------------------------------------------------------------------------------------------
DESCRIPTION & REFLECTION ON TASK 12 - EFFICIENT APPROACH FOR COMPUTER PLAYER STRATEGY
---------------------------------------------------------------------------------------------------
I implemented this task with a fairly simple approach but I believe it is an effective strategy
for the computer, the primary goal of the computer in this approach is that it will check each dice
per re-roll to see if is a match of a 1, 2 or 3 for that dice. If it finds that a dice is 3 or below,
it will re-roll it for a potentially better roll, if the dice is 4 or above then it will be left untouched.

This is effective because it gives the computer a good chance of achieving a high score per turn
through the use of its two available re-rolls. As once it has achieved a dice roll of 4 or above,
it will not risk re-rolling that dice again and will instead focus on re-rolling dices at 1 to 3.

Whilst this may result in a lower score for those dice, overall from my testing this approach
provides consistently effective results from the computer that requires the human player to be
effectively re-rolling as well to beat it.

Another check that the computer makes, is to see if its current score for this round and its total
score so far, will exceed the winning score. It also performs a check to see how the human
player's score is relative to its own score, it will check if the player is 10 points or more behind
in total score.

If the human player is 10 points or more behind, and the current combined score for the current set of
dice this round and the total score will result in a score greater or equal to the winning score.
In this scenario, the computer will decide not to re-roll their dice and will instead commit to the
current dice. As per the instructions given, the computer has no knowledge of what the current dice
of the human player is for this round, only the total score based on the prior round.

This strategy has some advantages and disadvantages. For example, when using the standard winning
score of 101 as a basis, if the computer is at 87 and the player is at 75. It is very likely that
the computer's score will safely beat the player's score. However, if the computer score is at 97
and the player score is 85. If the computer has a weak set of dice but the player has a very strong
set of dice. It is possible that the player's score could exceed the computer's with this approach.

However I believe that in most cases, this provides the computer with an effective way to secure
a win without risking getting a potentially worse set of dice that may cost them the game if they
were to have re-rolled instead.
---------------------------------------------------------------------------------------------------
 */