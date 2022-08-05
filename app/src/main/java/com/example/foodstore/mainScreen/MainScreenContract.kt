package com.example.foodstore.mainScreen

import com.example.foodstore.model.Food


interface MainScreenContract {

    interface Presenter{
        fun firstRun()
        fun onAttach(view: View)
        fun onDetach()
        fun onSearchFood(filter: String)
        fun onAddNewFoodClicked(food: Food)
        fun onDeleteAllClicked()
        fun onUpdateFood(food: Food, pos: Int)
        fun onDeleteFood(food: Food, pos: Int)
    }

    interface View  {
        fun showFoods(data: List<Food>)
        fun refreshFoods(data: List<Food>)
        fun addNewFood(newFood: Food)
        fun deleteFood(oldFood: Food, pos: Int)
        fun updateFood(editingFood: Food, pos: Int)
    }

}