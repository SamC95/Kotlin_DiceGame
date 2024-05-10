package com.example.dicegame_w1854525

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

// Creates an AlertDialog for the About button
private lateinit var about: AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sets the buttons to variables
        val aboutButton = findViewById<Button>(R.id.about)
        val gameButton = findViewById<Button>(R.id.newGame)

        // Creates variables for the total number of wins by the player and computer
        val totalTracker = findViewById<TextView>(R.id.totalWinLoss)

        totalTracker.text = TotalScore.updateTotal()

        // Creates an alert box that will appear when About is pressed
        val builder = AlertDialog.Builder(this)
        // Includes the title, appropriate information and an option to close the box
        builder.setTitle("About")
            .setMessage(
                "Name: Sam Clark\n" +
                        "ID: w1854525\n" +
                        "-------------------\n" +
                        "I confirm that I understand what plagiarism is and have read and\n" +
                        "understood the section on Assessment Offences in the Essential\n" +
                        "Information for Students. The work that I have submitted is\n" +
                        "entirely my own. Any work from other authors is duly referenced\n" +
                        "and acknowledged."
            )
            .setPositiveButton("Close") {
                    about, aboutButton -> }

        // Assigns the information from the builder variable into the about class variable
        about = builder.create()

        // Shows the alert box upon clicking About button
        aboutButton.setOnClickListener {
            about.show()
        }
        /*Upon clicking 'New Game' the program changes to a new window for the game,
        existing start page is closed as we restart this activity on a back press in GameActivity*/
        gameButton.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java)
            startActivity(gameIntent)
            finish();
        }
    }

    // Stores the activity state of the About alertDialog box so that it will remain open on orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (about.isShowing) {
            outState.putBoolean("alertDialog", true)
            about.dismiss()
        }
    }

    // Restores the About alertDialog box state prior to orientation change
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState.getBoolean("alertDialog", false))
            about.show()
    }
}