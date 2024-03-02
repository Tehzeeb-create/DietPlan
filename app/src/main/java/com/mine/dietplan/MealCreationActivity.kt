package com.mine.dietplan

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MealCreationActivity : AppCompatActivity() {
    private lateinit var verticalLayoutmealForm: LinearLayout
    private lateinit var mainlayout: LinearLayout
    private lateinit var datePickerDeadline: DatePicker
    private lateinit var editTextmealName: EditText
    private lateinit var spinnerRecipe1: Spinner
    private lateinit var spinnerRecipe2: Spinner
    private lateinit var spinnerRecipe3: Spinner
    private lateinit var spinnerRecipe4: Spinner
    private lateinit var spinnerRecipe5: Spinner
    private lateinit var btnAddmeal: Button
    private lateinit var btnSavemeal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_creation)

        verticalLayoutmealForm = findViewById(R.id.verticalLayoutmealForm)
        mainlayout = findViewById(R.id.mainlayout)
        datePickerDeadline = findViewById(R.id.datePickerDeadline)
        editTextmealName = findViewById(R.id.editTextmealName)
        spinnerRecipe1 = findViewById(R.id.spinnerRecipe1)
        spinnerRecipe2 = findViewById(R.id.spinnerRecipe2)
        spinnerRecipe3 = findViewById(R.id.spinnerRecipe3)
        spinnerRecipe4 = findViewById(R.id.spinnerRecipe4)
        spinnerRecipe5 = findViewById(R.id.spinnerRecipe5)
        btnAddmeal = findViewById(R.id.btnAddmeal)
        btnSavemeal = findViewById(R.id.btnSavemeal)

        checkForMealsInFirebase()

        btnAddmeal.setOnClickListener {
            showAddmealPanel()

        }

        btnSavemeal.setOnClickListener {
            saveMeal()
            hideAddmealPanel()
        }
        
    }

    private fun showAddmealPanel() {
        verticalLayoutmealForm.visibility = View.VISIBLE
        verticalLayoutmealForm.background = getDrawable(R.drawable.rounded_corners)
    }

    private fun hideAddmealPanel() {
        verticalLayoutmealForm.visibility = View.GONE
    }

    private fun checkForMealsInFirebase() {
        val database = FirebaseDatabase.getInstance()
        val mealsRef = database.getReference("meal")

        mealsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    retrievemealsFromFirebase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun saveMeal() {
        val mealId: String? = null
        val mealName = editTextmealName.text.toString()
        val selectedDate = getSelectedDate()

        // Get selected recipe names from the spinners
        val recipe1 = spinnerRecipe1.selectedItem.toString()
        val recipe2 = spinnerRecipe2.selectedItem.toString()
        val recipe3 = spinnerRecipe3.selectedItem.toString()
        val recipe4 = spinnerRecipe4.selectedItem.toString()
        val recipe5 = spinnerRecipe5.selectedItem.toString()

        // Get nutrient values for selected recipes
        val nutrientsMap = mapOf(
            "Cabbage" to 30,
            "Olive Oil" to 20,
            "Eggs" to 50,
            "Avocado" to 30,
            "Fish" to 80,
            "Nuts" to 40,
            "Yogurt" to 30,
            "Butter" to 50,
            "Grains" to 60
        )

        val nutrientContribution = listOf(
            nutrientsMap[recipe1] ?: 0,
            nutrientsMap[recipe2] ?: 0,
            nutrientsMap[recipe3] ?: 0,
            nutrientsMap[recipe4] ?: 0,
            nutrientsMap[recipe5] ?: 0
        ).sum()

        // Create and add the meal layout
        val mealLayout = createmealLayout(
            mealId,
            mealName,
            selectedDate,
            recipe1,
            recipe2,
            recipe3,
            recipe4,
            recipe5,
            nutrientContribution
        )

        // Save meal data to Firebase Realtime Database
        saveMealToFirebase(
            mealId,
            mealName,
            recipe1,
            recipe2,
            recipe3,
            recipe4,
            recipe5,
            selectedDate,
            nutrientContribution
        )

        mainlayout.addView(mealLayout)
    }

    private fun saveMealToFirebase(
        mealId: String?,
        mealName: String,
        recipe1: String?,
        recipe2: String?,
        recipe3: String?,
        recipe4: String?,
        recipe5: String?,
        selectedDate: String,
        nutrientContribution: Int
    ) {
        val database = FirebaseDatabase.getInstance()
        val mealsRef = database.getReference("meal")

        val mealKey = mealsRef.push().key
        val meal = Meal(
            mealId,
            mealName,
            selectedDate,
            recipe1,
            recipe2,
            recipe3,
            recipe4,
            recipe5,
            nutrientContribution
        )

        mealsRef.child(mealKey!!).setValue(meal)
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

    private fun retrievemealsFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val mealsRef = database.getReference("meal")

        mealsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mealSnapshot in snapshot.children) {
                    val meal = mealSnapshot.getValue(Meal::class.java)
                    if (meal != null) {
                        val mealLayout = createmealLayout(
                            meal.mealId,
                            meal.mealName,
                            meal.selectedDate,
                            meal.recipe1,
                            meal.recipe2,
                            meal.recipe3,
                            meal.recipe4,
                            meal.recipe5,
                            meal.nutrients
                        )
                        mainlayout.addView(mealLayout)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun createmealLayout(
        mealId: String?,
        mealName: String?,
        selectedDate: String?,
        recipe1: String?,
        recipe2: String?,
        recipe3: String?,
        recipe4: String?,
        recipe5: String?,
        nutrientContribution: Int
    ): LinearLayout {
        val mealLayout = LinearLayout(this)
        mealLayout.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.vertical_layout_margin)
        mealLayout.layoutParams = layoutParams
        mealLayout.setBackgroundResource(R.drawable.rounded_corners)
        mealLayout.setPadding(16, 16, 16, 16)
        mealLayout.addView(createTextView("Meal Name: $mealName"))
        mealLayout.addView(createTextView("Deadline: $selectedDate"))
        mealLayout.addView(createTextView("Nutrient Sum: $nutrientContribution"))
        mealLayout.addView(createTextView("Recipe 1: $recipe1"))
        mealLayout.addView(createTextView("Recipe 2: $recipe2"))
        mealLayout.addView(createTextView("Recipe 3: $recipe3"))
        mealLayout.addView(createTextView("Recipe 4: $recipe4"))
        mealLayout.addView(createTextView("Recipe 5: $recipe5"))

        val checkBoxDelete = CheckBox(this)
        checkBoxDelete.text = "Delete meal"
        checkBoxDelete.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mainlayout.removeView(mealLayout)
                deletemealFromFirebase(mealId)
            }
        }
        mealLayout.addView(checkBoxDelete)

        return mealLayout
    }

    private fun deletemealFromFirebase(mealId: String?) {
        if (mealId != null) {
            val database = FirebaseDatabase.getInstance()
            val mealsRef = database.getReference("meal")
            mealsRef.child(mealId).removeValue()
        }
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.textSize = 15f
        textView.setTypeface(null, Typeface.NORMAL)
        return textView
    }
}
