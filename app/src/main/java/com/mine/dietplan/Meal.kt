package com.mine.dietplan

class Meal {
    var mealId: String? = null
    var mealName: String? = null
    var recipe1: String? = null
    var recipe2: String? = null
    var recipe3: String? = null
    var recipe4: String? = null
    var recipe5: String? = null
    var nutrients: Int = 0
    var selectedDate: String? = null

    constructor() // Default constructor required for Firebase

    constructor(
        mealId: String?,
        mealName: String?,
        selectedDate: String?,
        recipe1: String?,
        recipe2: String?,
        recipe3: String?,
        recipe4: String?,
        recipe5: String?,
        nutrients: Int
    ) {
        this.mealId = mealId
        this.mealName = mealName
        this.selectedDate = selectedDate
        this.recipe1 = recipe1
        this.recipe2 = recipe2
        this.recipe3 = recipe3
        this.recipe4 = recipe4
        this.recipe5 = recipe5
        this.nutrients = nutrients
    }
}
