package com.example.foodstore.model

import androidx.room.*

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateFood(food: Food)

    @Insert
    fun insertFood(food: Food)

    @Insert
    fun insertAllFood(foodList: ArrayList<Food>)

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)

    @Query("DELETE FROM `Table Food`")
    fun deleteAllFoods()

    @Query("SELECT * FROM `Table Food`")
    fun getAllFood(): List<Food>

    @Query("SELECT *FROM `Table Food` WHERE  textSubject like '%' || :searching || '%'")
    fun searchFoods(searching: String): List<Food>
}