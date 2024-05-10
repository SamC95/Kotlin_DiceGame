package com.example.dicegame_w1854525

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

class GameActivity: AppCompatActivity() {
    // Creates variable for the GameLogic class
    private val gameLogic = GameLogic()

    private var playerDiceButtons = listOf<ImageButton>()
    private var compDiceButtons = listOf<ImageButton>()

    // Creates class level variables for saving instance state
    private lateinit var computerTitle: TextView
    private lateinit var playerTitle: TextView

    private lateinit var scoreTracker: TextView
    private lateinit var totalTracker: TextView

    // Variable for winning score so that it can be altered by the player, set to 101 by default
    private var winningScore = 101

    // Variable for the computer's remaining rolls that turn
    private var compRolls = 3

    // Variables to track the total score and score in a specific round
    private var totalCompScore = 0
    private var totalPlayScore = 0
    private var roundCompScore = 0
    private var roundPlayScore = 0

    // arrayList for storing total scores to be returned after score function call
    private lateinit var arrayTotals: MutableList<Int>

    private lateinit var diceActive: MutableList<Int>

    // Sets the dice images into a set
    private val images = linkedSetOf(
        R.drawable.die_face_1_t, R.drawable.die_face_2_t, R.drawable.die_face_3_t,
        R.drawable.die_face_4_t, R.drawable.die_face_5_t, R.drawable.die_face_6_t
    )

    /*  Variables for the EditText widget for choosing a custom winning score
     *  as well as the toggle button and related text */
    private lateinit var setScore: EditText
    private lateinit var scoreText: TextView
    private lateinit var toggleButton: ToggleButton
    private lateinit var toggleText: TextView

    // Variable to determine if snack-bar should be shown or not
    private var snackBarShow = 0

    private var btnText = "Throw"
    private var compStrategy = 0

    // Sets the alert dialog boxes to be initialised so they can be retained on orientation change
    private lateinit var win: AlertDialog
    private lateinit var loss: AlertDialog
    private lateinit var noThrow: AlertDialog
    private lateinit var noRolls: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Variables for the Throw and Score buttons
        val throwButton = findViewById<Button>(R.id.throwBtn)
        val scoreButton = findViewById<Button>(R.id.scoreBtn)

        // Assigns computer/player text to variables
        computerTitle = findViewById(R.id.computerText)
        playerTitle = findViewById(R.id.playerText)

        // Assigns all the Dice image buttons to variables
        val computerDice1 = findViewById<ImageButton>(R.id.computerDice1)
        val computerDice2 = findViewById<ImageButton>(R.id.computerDice2)
        val computerDice3 = findViewById<ImageButton>(R.id.computerDice3)
        val computerDice4 = findViewById<ImageButton>(R.id.computerDice4)
        val computerDice5 = findViewById<ImageButton>(R.id.computerDice5)

        val playerDice1 = findViewById<ImageButton>(R.id.playerDice1)
        val playerDice2 = findViewById<ImageButton>(R.id.playerDice2)
        val playerDice3 = findViewById<ImageButton>(R.id.playerDice3)
        val playerDice4 = findViewById<ImageButton>(R.id.playerDice4)
        val playerDice5 = findViewById<ImageButton>(R.id.playerDice5)

        /* Assigns the EditText and TextView for the user to set a custom winning score
         * and the Toggle Button and Text for the computer's strategy */
        setScore = findViewById(R.id.setScoreNum)
        scoreText = findViewById(R.id.setScoreText)
        toggleButton = findViewById(R.id.toggleButton)
        toggleText = findViewById(R.id.toggleText)

        // Places each button into a list
        compDiceButtons = mutableListOf(
            computerDice1, computerDice2, computerDice3,
            computerDice4, computerDice5
        )

        playerDiceButtons = mutableListOf(
            playerDice1, playerDice2, playerDice3,
            playerDice4, playerDice5
        )

        // Sets them to be initially invisible
        computerTitle.isVisible = false
        playerTitle.isVisible = false

        /*Sets each button to be invisible and disabled upon window start
        until throw is pressed for the first time*/
        compDiceButtons.forEach {
            it.isEnabled = false
            it.isVisible = false
        }

        playerDiceButtons.forEach {
            it.isEnabled = false
            it.isVisible = false
        }

        diceActive = mutableListOf(0, 0, 0, 0, 0)

        // Variable for the text that keeps track of the player and computer total scores
        scoreTracker = findViewById(R.id.scoreTracker)
        totalTracker = findViewById(R.id.totalWinLoss)

        // Updates the value of totalTracker to show the appropriate wins by the player/computer
        totalTracker.text = TotalScore.updateTotal()

        // Variable for checking current round score between re-rolls
        var currentCompRound = 0

        // Creates Dialog box that appears if the player wins
        val winBuilder = AlertDialog.Builder(this)
        val winText = getString(R.string.winMessage)
        winBuilder.setMessage(
            Html.fromHtml(
                "<font color='#32CD32'><b><big><big>$winText</big></big></b>",
                Html.FROM_HTML_MODE_LEGACY
            )
        )
            .setPositiveButton("Close") { win, _ ->
                win.dismiss()
            }
        win = winBuilder.create()

        // Creates Dialog box that appears if the computer wins
        val lossBuilder = AlertDialog.Builder(this)
        val loseText = getString(R.string.loseMessage)
        lossBuilder.setMessage(
            Html.fromHtml(
                "<font color='#b22222'><b><big><big>$loseText</big></big></b>",
                Html.FROM_HTML_MODE_LEGACY
            )
        )
            .setPositiveButton("Close") { lose, _ ->
                lose.dismiss()
            }
        loss = lossBuilder.create()

        val noRollsBuilder = AlertDialog.Builder(this)
        val reRollText = getString(R.string.reRollMsg)
        noRollsBuilder.setMessage(Html.fromHtml(reRollText, Html.FROM_HTML_MODE_LEGACY)
        )
            .setPositiveButton("Close") { noRolls, _ ->
                noRolls.dismiss()
            }
        noRolls = noRollsBuilder.create()

        val noThrowBuilder = AlertDialog.Builder(this)
        val throwText = getString(R.string.throwMsg)
        noThrowBuilder.setMessage(Html.fromHtml(throwText, Html.FROM_HTML_MODE_LEGACY)
        )
            .setPositiveButton("Close") { noThrow, _ ->
                noThrow.dismiss()
            }
        noThrow = noThrowBuilder.create()

        // Toggles the version of computer re-roll strategy that is called
        toggleButton.setOnClickListener {
            if (compStrategy == 0)
                compStrategy = 1
            else if (compStrategy == 1)
                compStrategy = 0
        }

        // Restores the current state of the score variables, as well as the visibility of certain elements
        if (savedInstanceState != null) {
            winningScore = savedInstanceState.getInt("winning_score")
            totalPlayScore = savedInstanceState.getInt("total_play_score")
            totalCompScore = savedInstanceState.getInt("total_comp_score")
            roundPlayScore = savedInstanceState.getInt("round_play_score")
            roundCompScore = savedInstanceState.getInt("round_comp_score")

            // Restores the amount of rolls the computer has remaining, and if the SnackBar message has been shown yet
            compRolls = savedInstanceState.getInt("compRolls")
            snackBarShow = savedInstanceState.getInt("snackBar")

            val compVisibility = savedInstanceState.getBoolean("compTitleVisibility")
            if (compVisibility)
                computerTitle.isVisible = true

            val playVisibility = savedInstanceState.getBoolean("playTitleVisibility")
            if (playVisibility)
                playerTitle.isVisible = true

            // function that restores the dice set to being visible and set to the correct dice image
            fun restoreDice(keyVisibility: String, keyTags: String, buttons: List<ImageButton>) {
                // For length of dice
                for (i in 0 until 5) {
                    // get the visibility state and a list of tag strings that were saved
                    val dice = savedInstanceState.getBoolean(keyVisibility)
                    val tags = savedInstanceState.getStringArray(keyTags)

                    /* If the tags are not empty, replace the brackets and split the array into
                    * a list of Integers ids or null values*/
                    if (tags != null) {
                        val tagIds = tags[0]?.replace("[", "")
                            ?.replace("]", "")?.split(", ")?.map { it.toIntOrNull() }

                        /* If the tag ids are not null, the tagId determines the set image
                        *  and then the tags are they reattached to the buttons for future use */
                        if (tagIds != null)
                            buttons.forEachIndexed { index, button ->
                                tagIds[index]?.let { button.setImageResource(it) }
                                buttons[index].tag = tagIds[index]
                            }
                    }
                    // If keyVisibility is true then button is set to be visible
                    if (dice) {
                        buttons[i].isVisible = true
                    }
                }
            }
            // Restores dice visibility and current state (which dice images were set) for player dice
            restoreDice(keyVisibility = "playDiceVisibility", keyTags = "playTags", playerDiceButtons)
            animateToggle(playerDiceButtons, diceActive)
            // Restores dice visibility and current state (which dice images were set) for computer dice
            restoreDice(keyVisibility = "compDiceVisibility", keyTags = "compTags", compDiceButtons)

            val editTextVisible = savedInstanceState.getBoolean("scoreEdit")
            if (!editTextVisible)
                setScore.isVisible = false

            val scoreTextVisible = savedInstanceState.getBoolean("scoreText")
            if (!scoreTextVisible)
                scoreText.isVisible = false

            val toggleVisible = savedInstanceState.getBoolean("toggleBtn")
            if (!toggleVisible)
                toggleButton.isVisible = false

            val togTxtVisibility = savedInstanceState.getBoolean("toggleText")
            if (!togTxtVisibility)
                toggleText.isVisible = false

            val savedScore = savedInstanceState.getString("score_track")
            scoreTracker.text = savedScore

            val throwBtn = savedInstanceState.getString("throwBtn")
            throwButton.text = throwBtn

            // Checks each of the Alert Dialogs to see if they were active upon activity resetting
            if (savedInstanceState.getBoolean("winDialog", false))
                win.show()
            else if (savedInstanceState.getBoolean("lossDialog", false))
                loss.show()
            else if (savedInstanceState.getBoolean("noThrowDialog", false))
                noThrow.show()
            else if (savedInstanceState.getBoolean("noRollsDialog", false)) {
                noRolls.show()
                onScoreOrNoRolls(throwButton, currentCompRound)
            }
        }

        if (throwButton.text != "Throw") {
            playerDiceButtons.forEach {
                it.isEnabled = true
            }
        }

        // Checks the current text of the Throw button (Throw, Re-roll (2), Re-roll (1))
        btnText = throwButton.text.toString()

        throwButton.setOnClickListener { it ->
            // Resets round scores
            roundCompScore = 0
            roundPlayScore = 0

            // Sets the dice to be visible upon the first throw
            if (!compDiceButtons[0].isVisible && !playerDiceButtons[0].isVisible) {
                compDiceButtons.forEach {
                    it.isEnabled = true
                    it.isVisible = true
                }

                playerDiceButtons.forEach {
                    it.isEnabled = true
                    it.isVisible = true
                }
                // Sets the computer/player titles to be visible upon the first throw
                computerTitle.isVisible = true
                playerTitle.isVisible = true

                // Converts the text in the set score text field to an Integer and stores it in winningScore
                winningScore = Integer.parseInt(setScore.text.toString())

                /* Hides the UI for setting a score and computer strategy
                    once the user presses throw for the first time*/
                setScore.isVisible = false
                scoreText.isVisible = false
                toggleButton.isVisible = false
                toggleText.isVisible = false
            }

            /*Throw Button functionality:
            * Throw will rename to Re-roll (2), Re-roll (1)
            * to indicate how many re-rolls are remaining, when Re-roll (1) is pressed
            * Throw will automatically score with the roll from that click

            * Throw Button will function on these conditions
            * Neither the Player or Computer have reached a winning score
            * If the player and computer are above the winning score and tied,
            * throw will automatically score and no re-rolls are allowed*/
            if (((totalCompScore < winningScore) && (totalPlayScore < winningScore))) {
                if (throwButton.text != "Re-roll (1)") {
                    when (throwButton.text) {
                        "Throw" -> throwButton.text = "Re-roll (2)"
                        "Re-roll (2)" -> throwButton.text = "Re-roll (1)"
                    }
                    playerDiceButtons.forEach {
                        it.isEnabled = true
                    }

                    // On first click of throw, shows a message for how to re-roll dice
                    if ((throwButton.text == "Re-roll (2)") && (snackBarShow == 0)) {
                        Snackbar.make(it, "Click on the dice you want to keep when re-rolling",
                            Snackbar.LENGTH_LONG).show()
                        // Sets variable to 1 so that the message will not be repeatedly shown each turn
                        snackBarShow = 1
                    }

                    // Re-checks the text of the Button after it has been changed
                    btnText = throwButton.text.toString()

                    // Calls function to randomise dice and calculate the score for that round
                        if (throwButton.text == "Re-roll (2)") {
                            roundCompScore = gameLogic.randomise(compDiceButtons, player = false, roundCompScore,
                                images, diceActive)
                            compRolls--
                            currentCompRound = roundCompScore

                        }
                        else {
                            roundCompScore =
                                if (compStrategy == 0)
                                    gameLogic.computerRandom(compDiceButtons, images, compRolls)
                                else
                                    gameLogic.computerLogical(compDiceButtons, images, compRolls,
                                        totalPlayScore, totalCompScore, winningScore,
                                        scorePressed = false, currentCompRound)
                            compRolls--
                            currentCompRound = roundCompScore
                        }

                    roundPlayScore =
                        gameLogic.randomise(playerDiceButtons, player = true, roundPlayScore, images, diceActive)

                    animateToggle(playerDiceButtons, diceActive)
                    diceReset(playerDiceButtons, diceActive)
                }
                else if ((totalCompScore < winningScore && totalPlayScore < winningScore)) {
                        // Calls function to randomise dice and calculate the score for that round

                    roundCompScore =
                        if (throwButton.text == "Re-roll (2)") {
                            gameLogic.randomise(compDiceButtons, player = false, roundCompScore, images, diceActive)
                    }
                        else {
                            if (compStrategy == 0)
                                gameLogic.computerRandom(compDiceButtons, images, compRolls)
                            else
                                gameLogic.computerLogical(compDiceButtons, images, compRolls,
                                totalPlayScore, totalCompScore, winningScore,
                                scorePressed = false, currentCompRound)
                    }
                    compRolls--
                    currentCompRound = roundCompScore

                    roundPlayScore =
                        gameLogic.randomise(playerDiceButtons, player = true, roundPlayScore, images, diceActive)

                    noRolls.setOnDismissListener {
                        // Sets behaviour for when the noRolls alert is closed
                        onScoreOrNoRolls(throwButton, currentCompRound)
                        }

                        // Show alert to indicate no remaining rolls
                        noRolls.show()
                        animateToggle(playerDiceButtons, diceActive)
                        diceActive = diceReset(playerDiceButtons, diceActive)
                    }
                }
            /* Condition for if both the human player and computer have reached a winning score
               but are tied, the throw button will instead roll the dice once and automatically
               apply this score for each player, without allowing re-rolls.
               This block of code will continue to trigger until a tie break occurs */
            else if (((totalPlayScore >= winningScore && totalCompScore >= winningScore))
                && (totalPlayScore == totalCompScore)
            ) {
                // Randomises the dice when throw is clicked, dice remain un-clickable
                roundCompScore =
                    gameLogic.randomise(compDiceButtons, player = false, roundCompScore, images, diceActive)
                roundPlayScore =
                    gameLogic.randomise(playerDiceButtons, player = true, roundPlayScore, images, diceActive)

                // Instantly takes the randomised rolls and scores them for each player
                val (list, string) = gameLogic.score(throwButton, totalCompScore, totalPlayScore,
                    winningScore, scoreTracker, roundCompScore, roundPlayScore)

                arrayTotals = list.toMutableList()
                totalTracker.text = string

                totalPlayScore = arrayTotals[0]
                totalCompScore = arrayTotals[1]

                arrayTotals.clear()
            }
        }

        scoreButton.setOnClickListener {
            /*Score will only function on these conditions:
             * Player or Computer has not yet reached the winning score
             * Player & Computer are above the winning score and are tied
             * Throw Button has been pressed at least one time*/
            if ((throwButton.text == "Throw") && ((totalPlayScore < winningScore) && (totalCompScore < winningScore))) {
                noThrow.show()
            }
            else if ((totalPlayScore < winningScore) && (totalCompScore < winningScore)
                || (totalCompScore == totalPlayScore)
            ) {
                // Calls functions for score functionality and resetting of dice animation state
                onScoreOrNoRolls(throwButton, currentCompRound)
                diceReset(playerDiceButtons, diceActive)
            }
        }
    }

    /*Returns to a fresh activity of the start page on back button press
    * I was unable to figure out how to implement a non-deprecated version of this*/
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    // Saves variable states so that they are retained upon orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        // MutableLists to store the current image tags of the dice so they can be restored
        val playTags: MutableList<String> = MutableList(5) { "" }
        val compTags: MutableList<String> = MutableList(5) { "" }

        fun storeButtons(buttons: List<ImageButton>, keyVisibility: String, keyTags: String, tags: MutableList<String>) {
            buttons.forEach {
                outState.putBoolean(keyVisibility, it.isVisible)

                for (i in 0 until 5) {
                    if (buttons[i].tag != null)
                        tags[i] = buttons[i].tag.toString()
                }
                outState.putStringArray(keyTags, arrayOf(tags.toString()))
            }
        }

        // Stores the winning score that the user entered
        outState.putInt("winning_score", winningScore)
        outState.putInt("compRolls", compRolls)
        outState.putInt("snackBar", snackBarShow)
        outState.putString("throwBtn", btnText)

        // Stores the total scores of the match so far
        outState.putInt("total_play_score", totalPlayScore)
        outState.putInt("total_comp_score", totalCompScore)

        // Stores the scores of the current round
        outState.putInt("round_play_score", roundPlayScore)
        outState.putInt("round_comp_score", roundCompScore)

        // Stores the text for the score tracker
        outState.putString("score_track", scoreTracker.text.toString())

        // Stores the visibility state of certain elements
        outState.putBoolean("compTitleVisibility", computerTitle.isVisible)
        outState.putBoolean("playTitleVisibility", playerTitle.isVisible)
        outState.putBoolean("scoreText", scoreText.isVisible)
        outState.putBoolean("scoreEdit", setScore.isVisible)
        outState.putBoolean("toggleBtn", toggleButton.isVisible)
        outState.putBoolean("toggleText", toggleText.isVisible)

        storeButtons(playerDiceButtons, keyVisibility = "playDiceVisibility", keyTags = "playTags",
                    playTags)

        storeButtons(compDiceButtons, keyVisibility = "compDiceVisibility", keyTags = "compTags",
                    compTags)

        if (win.isShowing) {
            outState.putBoolean("winDialog", true)
            win.dismiss()
        } else if (loss.isShowing) {
            outState.putBoolean("lossDialog", true)
            loss.dismiss()
        } else if (noThrow.isShowing) {
            outState.putBoolean("noThrowDialog", true)
            noThrow.dismiss()
        } else if (noRolls.isShowing) {
            outState.putBoolean("noRollsDialog", true)
            noRolls.dismiss()
        }
        super.onSaveInstanceState(outState)
    }
    /* Sets the current state of the dice (clicked or not clicked) for re-rolls*/
     private fun animateToggle(playerDiceButtons: List<ImageButton>,
                               diceActive: MutableList<Int>) {
            diceClick(playerDiceButtons[0]) { isActive ->
                diceActive[0] = isActive
            }
            diceClick(playerDiceButtons[1]) { isActive ->
                diceActive[1] = isActive
            }
            diceClick(playerDiceButtons[2]) { isActive ->
                diceActive[2] = isActive
            }
            diceClick(playerDiceButtons[3]) { isActive ->
                diceActive[3] = isActive
            }
            diceClick(playerDiceButtons[4]) { isActive ->
                diceActive[4] = isActive
            }
        }
    // Sets the behaviour for if score is pressed or no re-rolls are remaining
    private fun onScoreOrNoRolls(throwButton: Button, currentRoundScore: Int) {
            // If computer has remaining rolls, it uses them
            if ((compRolls != 0) && compStrategy == 0)
                roundCompScore = gameLogic.computerRandom(compDiceButtons, images, compRolls)
            else if (compRolls != 0 && compStrategy == 1)
                roundCompScore = gameLogic.computerLogical(compDiceButtons, images, compRolls,
                    totalPlayScore, totalCompScore, winningScore,
                    scorePressed = true, currentRoundScore)
            /* Score button logic is called, returns total scores for player/computer as list,
            * as well as the current text of the throw button to be updated and a variable
            * that is updated to determine if a win occurred for the player or computer that turn*/
            val (list, string, winCheck) = gameLogic.score(
                throwButton, totalCompScore, totalPlayScore,
                winningScore, scoreTracker, roundCompScore, roundPlayScore)

            /* Performs a check to see if a winning score was reached in the GameLogic class
               If winCheck = 0 (Default value) nothing happens, if 1 then You Win! dialog displayed
               If 2 then You lose dialog displayed */
            if (winCheck == 1)
                win.show()
            else if (winCheck == 2)
                loss.show()

            btnText = throwButton.text.toString()

            arrayTotals = list.toMutableList()
            totalTracker.text = string

            // Sets totals from score function into variables to maintain correct total
            totalPlayScore = arrayTotals[0]
            totalCompScore = arrayTotals[1]

            // Clears arrayTotals list for future use
            arrayTotals.clear()

            playerDiceButtons.forEach {
                it.isClickable = false
            }
            compRolls = 3
        }
    // Animates the dice button that is clicked and sets an integer to represent if it is active or not
    private fun diceClick(diceButton: ImageButton, defaultDir: Int = 1, onStateChange: (Int) -> Unit) {
        var dir = defaultDir
        var isActive = 0

        diceButton.setOnClickListener {
            dir *= -1
            diceButton.isClickable = false
            diceButton.animate().setDuration(300).translationYBy(dir * 100f).withEndAction {
                isActive =
                    if (isActive == 0)
                        1
                    else
                        0
                onStateChange(isActive)
                diceButton.isClickable = true
            }
        }
    }
    // Sets the dice back to their default positions
    private fun diceReset(diceButton: List<ImageButton>, diceActive: MutableList<Int>): MutableList<Int> {
        diceButton.forEachIndexed { index, button ->
            if (diceActive[index] == 1) {
                button.animate().setDuration(300).translationY(0f)
                diceActive[index] = 0
            }
        }
        return diceActive
    }
}