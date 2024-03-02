package com.mine.dietplan

class Goal{
    var goalId: String? = null
    var goalName: String?= null
    var caloriesTarget: String?= null
    var carbohydrates: String?= null
    var protein: String?= null
    var fats: String?= null
    var isGlutenFree: Boolean?= null
    var selectedDate: String? = null
    constructor() // Default constructor required for Firebase

    constructor(
        goalId: String? ,
        goalName: String?,
        caloriesTarget: String?,
        carbohydrates: String?,
        protein: String?,
        fats: String? ,
        isGlutenFree: Boolean?,
        selectedDate: String?)
    {
        this.goalId = goalId
        this.goalName = goalName
        this.caloriesTarget = caloriesTarget
        this.carbohydrates = carbohydrates
        this.protein = protein
        this.fats = fats
        this.isGlutenFree = isGlutenFree
        this.selectedDate = selectedDate


    }    }



