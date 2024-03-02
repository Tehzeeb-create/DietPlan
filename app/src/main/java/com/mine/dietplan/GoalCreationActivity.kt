package com.mine.dietplan

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class GoalCreationActivity : AppCompatActivity() {
    private lateinit var verticalLayoutGoalForm: LinearLayout
    private lateinit var mainlayout: LinearLayout
    private lateinit var datePickerDeadline: DatePicker
    private lateinit var editTextGoalName: EditText
    private lateinit var editTextCaloriesTarget: EditText
    private lateinit var editTextCarbohydrates: EditText
    private lateinit var editTextProtein: EditText
    private lateinit var editTextFats: EditText
    private lateinit var checkBoxGlutenFree: CheckBox


    private lateinit var btnAddGoal: Button
    private lateinit var btnSaveGoal: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_creation)

        verticalLayoutGoalForm = findViewById(R.id.verticalLayoutGoalForm)
        mainlayout = findViewById(R.id.mainlayout)
        datePickerDeadline = findViewById(R.id.datePickerDeadline)
        editTextGoalName = findViewById(R.id.editTextGoalName)
        editTextCaloriesTarget = findViewById(R.id.editTextCaloriesTarget)
        editTextCarbohydrates = findViewById(R.id.editTextCarbohydrates)
        editTextProtein = findViewById(R.id.editTextProtein)
        editTextFats = findViewById(R.id.editTextFats)
        checkBoxGlutenFree = findViewById(R.id.checkBoxGlutenFree)

        btnAddGoal = findViewById(R.id.btnAddGoal)
        val btnSaveGoal: Button = findViewById(R.id.btnSaveGoal)

        checkForGoalsInFirebase()

        btnAddGoal.setOnClickListener {
            showAddGoalPanel()
        }

        btnSaveGoal.setOnClickListener {
            saveGoal()
            hideAddGoalPanel()
        }
    }

    private fun showAddGoalPanel() {
        // Make the verticalLayoutGoalForm visible
        verticalLayoutGoalForm.visibility = View.VISIBLE

        // Set the background of verticalLayoutGoalForm with white background and rounded corners
        verticalLayoutGoalForm.background = getDrawable(R.drawable.rounded_corners)
    }

    private fun hideAddGoalPanel() {
        // Hide the last added Goal Layout (assuming it's the one to be hidden)
        verticalLayoutGoalForm.visibility = View.GONE

    }
    private fun checkForGoalsInFirebase() {
        val database = FirebaseDatabase.getInstance()
        val goalsRef = database.getReference("goal")

        // Attach a listener to check if there are any goals
        goalsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // If goals exist, retrieve them from Firebase
                    retrieveGoalsFromFirebase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }


    private fun saveGoal() {
        // Retrieve values from your UI components
        val goalId: String? = null // Replace with the actual logic to get the goalId
        val goalName = editTextGoalName.text.toString()
        val caloriesTarget = editTextCaloriesTarget.text.toString()
        val carbohydrates = editTextCarbohydrates.text.toString()
        val protein = editTextProtein.text.toString()
        val fats = editTextFats.text.toString()
        val isGlutenFree = checkBoxGlutenFree.isChecked

        // Retrieve the selected date from the DatePicker
        val selectedDate = getSelectedDate()

        // Create and add the goal layout
        val goalLayout = createGoalLayout(
            goalId,
            goalName,
            caloriesTarget,
            carbohydrates,
            protein,
            fats,
            isGlutenFree,
            selectedDate
        )

        // Save goal data to Firebase Realtime Database
        saveGoalToFirebase(
            goalId,
            goalName,
            caloriesTarget,
            carbohydrates,
            protein,
            fats,
            isGlutenFree,
            selectedDate
        )

        mainlayout.addView(goalLayout)

        // Hide the panel after saving the goal
//        verticalLayoutGoalForm.visibility = View.GONE
    }

    private fun saveGoalToFirebase(
        goalId: String?,
        goalName: String,
        caloriesTarget: String,
        carbohydrates: String,
        protein: String,
        fats: String,
        isGlutenFree: Boolean,
        selectedDate: String
    ) {
        // Get a reference to your Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val goalsRef = database.getReference("goal") // "goal" is the name of the node in the database

        // Create a unique key for the goal
        val goalKey = goalsRef.push().key

        // Create a Goal object with the goal data
        val goal = Goal(
            goalKey,
            goalName,
            caloriesTarget,
            carbohydrates,
            protein,
            fats,
            isGlutenFree,
            selectedDate
        )

        // Save the goal to the database
        goalsRef.child(goalKey!!).setValue(goal)
    }

    private fun getSelectedDate(): String {
        val day = datePickerDeadline.dayOfMonth
        val month = datePickerDeadline.month
        val year = datePickerDeadline.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun createGoalLayout(
        goalId: String?,
        goalName: String?,
        caloriesTarget: String?,
        carbohydrates: String?,
        protein: String?,
        fats: String?,
        isGlutenFree: Boolean?,
        selectedDate: String?
    ): LinearLayout {
        // Create a new Vertical Layout for the Goal
        val goalLayout = LinearLayout(this)
        goalLayout.orientation = LinearLayout.VERTICAL

        // Set layout parameters for the Vertical Layout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Set bottom margin for all layouts except the last one
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.vertical_layout_margin)

        goalLayout.layoutParams = layoutParams

        // Set background and padding
        goalLayout.setBackgroundResource(R.drawable.rounded_corners)
        goalLayout.setPadding(16, 16, 16, 16)

        // Add TextViews to display Goal details
        goalLayout.addView(createTextView("Goal Name: $goalName"))
        goalLayout.addView(createTextView("Calories Target: $caloriesTarget"))
        goalLayout.addView(createTextView("Carbohydrates: $carbohydrates"))
        goalLayout.addView(createTextView("Protein: $protein"))
        goalLayout.addView(createTextView("Fats: $fats"))
        goalLayout.addView(createTextView("Gluten Free: $isGlutenFree"))
        goalLayout.addView(createTextView("Selected Date: $selectedDate"))

        // Add CheckBox to delete the Goal
        val checkBoxDelete = CheckBox(this)
        checkBoxDelete.text = "Delete Goal"
        checkBoxDelete.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Remove the goal from the UI
                mainlayout.removeView(goalLayout)

                // Remove the goal from the Firebase Realtime Database
                deleteGoalFromFirebase(goalId)
            }
        }
        goalLayout.addView(checkBoxDelete)

        return goalLayout
    }


    private fun deleteGoalFromFirebase(goalId: String?) {
        if (goalId != null) {
            val database = FirebaseDatabase.getInstance()
            val goalsRef = database.getReference("goal")

            // Remove the goal from the database
            goalsRef.child(goalId).removeValue()
        }
    }
    private fun retrieveGoalsFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val goalsRef = database.getReference("goal")

        // Attach a listener to retrieve the goals
        goalsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through the snapshot and add goals to the UI
                for (goalSnapshot in snapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    if (goal != null) {
                        val goalLayout = createGoalLayout(
                            goal.goalId,
                            goal.goalName,
                            goal.caloriesTarget,
                            goal.carbohydrates,
                            goal.protein,
                            goal.fats,
                            goal.isGlutenFree,
                            goal.selectedDate
                        )
                        mainlayout.addView(goalLayout)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }


    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.textSize = 15f
        textView.setTypeface(null, Typeface.NORMAL)
        return textView
    }
}