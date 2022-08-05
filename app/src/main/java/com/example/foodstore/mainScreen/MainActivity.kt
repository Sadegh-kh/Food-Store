package com.example.foodstore.mainScreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstore.databinding.ActivityMainBinding
import com.example.foodstore.databinding.DialogAddNewItemBinding
import com.example.foodstore.databinding.DialogDeleteItemBinding
import com.example.foodstore.databinding.DialogUpdateItemBinding
import com.example.foodstore.model.Food
import com.example.foodstore.model.FoodDao
import com.example.foodstore.model.MyDatabase

class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvent {
    lateinit var foodDao: FoodDao
    lateinit var binding: ActivityMainBinding
    lateinit var myAdpter: FoodAdapter
    lateinit var foodList: ArrayList<Food>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = MyDatabase.getDatabase(this).foodDao

        val sharedPreferences = getSharedPreferences("FoodStore", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("first_run", true)) {
            firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }

        showAllData()

        binding.btnDeleteAllFood.setOnClickListener {
            removeAllFood()
        }



        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }

        binding.editSearch.addTextChangedListener { edittextInput ->//for example 'h'

            searchOnDatabase(edittextInput!!.toString())


        }


    }

    private fun searchOnDatabase(edittextInput: String) {
        if (edittextInput.isNotEmpty()) {
            val searchListFood = foodDao.searchFoods(edittextInput)
            myAdpter.setData(searchListFood as ArrayList<Food>)


        } else {
            //show all data
            val data = foodDao.getAllFood()
            myAdpter.setData(data as ArrayList<Food>)
        }
    }


    private fun addNewFood() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        dialogBinding.dialogBtnDone.setOnClickListener {
            if (
                dialogBinding.dialogEditFoodCity.length() > 0 &&
                dialogBinding.dialogEditFoodDistance.length() > 0 &&
                dialogBinding.dialogEditFoodName.length() > 0 &&
                dialogBinding.dialogEditFoodPrice.length() > 0

            ) {
                val textName = dialogBinding.dialogEditFoodName.text.toString()
                val textPrice = dialogBinding.dialogEditFoodPrice.text.toString()
                val textDistance = dialogBinding.dialogEditFoodDistance.text.toString()
                val textCity = dialogBinding.dialogEditFoodCity.text.toString()
                val textRatingNumber: Int = (1..150).random()
                val ratingBarStar: Float = (1 until 6).random().toFloat()
                val randomForUtl = (0 until 7).random()
                val urlPic = foodList[randomForUtl].urlImage

                val newFood = Food(
                    textSubject = textName,
                    textPrice = textPrice,
                    textDistance = textDistance,
                    textCity = textCity,
                    urlImage = urlPic,
                    numOfRating = textRatingNumber,
                    rating = ratingBarStar
                )
                myAdpter.addFood(newFood)
                foodDao.insertFood(newFood)

                dialog.dismiss()
                binding.recyvlerMain.scrollToPosition(0)

            } else {
                Toast.makeText(this, "لفطفا همه فیلد ها را پر کنید", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun removeAllFood() {
        foodDao.deleteAllFoods()

        showAllData()
    }

    private fun firstRun() {
        foodList = arrayListOf(
            Food(
                textSubject = "Pizza",
                textPrice = "12",
                textDistance = "3",
                textCity = "Tehran",
                urlImage = "https://www.recipetineats.com/wp-content/uploads/2020/05/Pepperoni-Pizza_5-SQjpg.jpg",
                numOfRating = 20,
                rating = 4.5f
            ),
            Food(
                textSubject = "Lazania",
                textPrice = "10",
                textDistance = "5",
                textCity = "Karaj",
                urlImage = "https://panamag.ir/wp-content/uploads/2021/11/lazania.jpg",
                numOfRating = 10,
                rating = 3.5f
            ),
            Food(
                textSubject = "Hamburger",
                textPrice = "11",
                textDistance = "7",
                textCity = "Isfahan",
                urlImage = "https://insanelygoodrecipes.com/wp-content/uploads/2020/10/Hamburger-with-Sesame-Seeds-Cheese-and-Veggies.png",
                numOfRating = 50,
                rating = 3f
            ),
            Food(
                textSubject = "Falafel",
                textPrice = "5",
                textDistance = "2",
                textCity = "Mashhad",
                urlImage = "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2019/5/13/0/FNK_Falafel_H1_s4x3.jpg.rend.hgtvcom.616.462.suffix/1557773603107.jpeg",
                numOfRating = 100,
                rating = 4.25f
            ),
            Food(
                textSubject = "Sandvich Khorak",
                textPrice = "9",
                textDistance = "10",
                textCity = "Kermanshah",
                urlImage = "https://timchar.ir/zarinshahr//Opitures/116557901140210.jpg",
                numOfRating = 65,
                rating = 2.5f
            ),
            Food(
                textSubject = "Hot Dog",
                textPrice = "13",
                textDistance = "3",
                textCity = "Tehran",
                urlImage = "https://www.thespruceeats.com/thmb/O8cBOSCu3233XKmts7kPiT-52F4=/1685x1264/smart/filters:no_upscale()/air-fryer-hot-dogs-4802499-07-b327e219937c429a81efaf61519724a5.jpg",
                numOfRating = 55,
                rating = 4.6f
            ),
            Food(
                textSubject = "Makaroni",
                textPrice = "14",
                textDistance = "2.5",
                textCity = "Kerman",
                urlImage = "https://saten.ir/wp-content/uploads/2021/07/makarooni.jpg",
                numOfRating = 68,
                rating = 4f
            )
        )

        foodDao.insertAllFood(foodList)
    }

    private fun showAllData() {
        val foodData = foodDao.getAllFood()

        myAdpter = FoodAdapter(ArrayList(foodData), this)
        binding.recyvlerMain.adapter = myAdpter
        binding.recyvlerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        Log.v("food_log", foodData.toString())
    }

    override fun onFoodClicked(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()

        val updateDialogBindin = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(updateDialogBindin.root)
        dialog.setCancelable(true)
        dialog.show()
        updateDialogBindin.dialogEditFoodName.setText(food.textSubject)
        updateDialogBindin.dialogEditFoodCity.setText(food.textCity)
        updateDialogBindin.dialogEditFoodDistance.setText(food.textDistance)
        updateDialogBindin.dialogEditFoodPrice.setText(food.textPrice)
        updateDialogBindin.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        updateDialogBindin.dialogUpdateBtnDone.setOnClickListener {
            if (
                updateDialogBindin.dialogEditFoodCity.length() > 0 &&
                updateDialogBindin.dialogEditFoodDistance.length() > 0 &&
                updateDialogBindin.dialogEditFoodName.length() > 0 &&
                updateDialogBindin.dialogEditFoodPrice.length() > 0

            ) {
                val textName = updateDialogBindin.dialogEditFoodName.text.toString()
                val textPrice = updateDialogBindin.dialogEditFoodPrice.text.toString()
                val textDistance = updateDialogBindin.dialogEditFoodDistance.text.toString()
                val textCity = updateDialogBindin.dialogEditFoodCity.text.toString()

                val newFood = Food(
                    id = food.id,
                    textSubject = textName,
                    textPrice = textPrice,
                    textDistance = textDistance,
                    textCity = textCity,
                    urlImage = food.urlImage,
                    numOfRating = food.numOfRating,
                    rating = food.rating
                )

                //update item in adapter=>
                myAdpter.updateFood(newFood, position)
                //update item in database
                foodDao.updateFood(newFood)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "لطفا همه فیلد هارا پر کنید", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onFoodLongClickend(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()

        val dialogDeleteBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        dialogDeleteBinding.dialogBtnDeleteNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogDeleteBinding.dialogBtnDeleteYes.setOnClickListener {
            myAdpter.removeFood(food, position)
            foodDao.deleteFood(food)
            dialog.dismiss()

        }
    }

}