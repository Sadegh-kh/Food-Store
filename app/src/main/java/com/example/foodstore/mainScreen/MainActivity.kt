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
import com.example.foodstore.until.showToast

class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvent,MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter
    private lateinit var presenter: MainScreenContract.Presenter
    private lateinit var foodList:ArrayList<Food>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        presenter=MainScreenPresenter(MyDatabase.getDatabase(this).foodDao)
        setContentView(binding.root)
        presenter.onAttach(this)
        val sharedPreferences = getSharedPreferences("FoodStore", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            presenter.firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }


        binding.btnDeleteAllFood.setOnClickListener {
         presenter.onDeleteAllClicked()
        }

        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }

        binding.editSearch.addTextChangedListener { edittextInput ->//for example 'h'
            presenter.onSearchFood(edittextInput.toString())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
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
                presenter.onAddNewFoodClicked(newFood)
                dialog.dismiss()
                binding.recyvlerMain.scrollToPosition(0)

            } else {
               showToast("لفطفا همه فیلد ها را پر کنید")
            }

        }

    }

    //Adapter interfaces
    override fun onFoodClicked(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        updateDialogBinding.dialogEditFoodName.setText(food.textSubject)
        updateDialogBinding.dialogEditFoodCity.setText(food.textCity)
        updateDialogBinding.dialogEditFoodDistance.setText(food.textDistance)
        updateDialogBinding.dialogEditFoodPrice.setText(food.textPrice)
        updateDialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        updateDialogBinding.dialogUpdateBtnDone.setOnClickListener {
            if (
                updateDialogBinding.dialogEditFoodCity.length() > 0 &&
                updateDialogBinding.dialogEditFoodDistance.length() > 0 &&
                updateDialogBinding.dialogEditFoodName.length() > 0 &&
                updateDialogBinding.dialogEditFoodPrice.length() > 0

            ) {
                val textName = updateDialogBinding.dialogEditFoodName.text.toString()
                val textPrice = updateDialogBinding.dialogEditFoodPrice.text.toString()
                val textDistance = updateDialogBinding.dialogEditFoodDistance.text.toString()
                val textCity = updateDialogBinding.dialogEditFoodCity.text.toString()

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
                presenter.onUpdateFood(newFood,position)
                dialog.dismiss()
            } else {
                showToast("لطفا همه فیلد هارا پر کنید")
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
            presenter.onDeleteFood(food,position)
            dialog.dismiss()
        }
    }

    //Contract interface
    override fun showFoods(data: List<Food>) {
        foodList= ArrayList(data)
        myAdapter= FoodAdapter(ArrayList(data),this)
        binding.recyvlerMain.adapter=myAdapter
        binding.recyvlerMain.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }
    override fun refreshFoods(data: List<Food>) {
        myAdapter.setData(ArrayList(data))
    }
    override fun addNewFood(newFood: Food) {
        myAdapter.addFood(newFood)
    }
    override fun deleteFood(oldFood: Food, pos: Int) {
        myAdapter.removeFood(oldFood,pos)
    }
    override fun updateFood(editingFood: Food, pos: Int) {
        myAdapter.updateFood(editingFood,pos)
    }

}