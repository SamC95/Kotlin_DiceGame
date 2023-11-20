package com.example.dicegame_w1854525

object TotalScore {
    var playerScore = 0
    var computerScore = 0

    private var totalScore: String = "H:$playerScore/C:$computerScore"
    // function that increments the score for number of wins by player/computer when a win/loss occurs
    fun incrementScore(playerWin: Boolean): String {
        if (playerWin)
            playerScore++
        else
            computerScore++

        totalScore = "H:$playerScore/C:$computerScore"

        return totalScore
    }
    // function to update the score on the MainActivity and GameActivity without incrementing it
    fun updateTotal(): String {
        return totalScore
    }
}