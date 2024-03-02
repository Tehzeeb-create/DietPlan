package com.mine.dietplan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Called when the "Student Search" image is clicked
    fun onGoalClick(view: View) {
        startActivity(Intent(this, GoalCreationActivity::class.java))
    }

    // Called when the "Student Entry" image is clicked
    fun onMealClick(view: View) {
        startActivity(Intent(this, MealCreationActivity::class.java))
    }
}