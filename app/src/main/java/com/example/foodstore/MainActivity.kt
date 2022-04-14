package com.example.foodstore

import android.os.Bundle
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

class MainActivity : AppCompatActivity(),FoodAdapter.FoodEvent {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdpter:FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodList = arrayListOf<Food>(
            Food(
                "Pizza",
                "12",
                "3",
                "Tehran",
                "https://www.recipetineats.com/wp-content/uploads/2020/05/Pepperoni-Pizza_5-SQjpg.jpg",
                20,
                4.5f
            ),
            Food(
                "Lazania",
                "10",
                "5",
                "Karaj",
                "https://panamag.ir/wp-content/uploads/2021/11/lazania.jpg",
                10,
                3.5f
            ),
            Food(
                "Hamburger",
                "11",
                "7",
                "Isfahan",
                "https://insanelygoodrecipes.com/wp-content/uploads/2020/10/Hamburger-with-Sesame-Seeds-Cheese-and-Veggies.png",
                50,
                3f
            ),
            Food(
                "Falafel",
                "5",
                "2",
                "Mashhad",
                "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2019/5/13/0/FNK_Falafel_H1_s4x3.jpg.rend.hgtvcom.616.462.suffix/1557773603107.jpeg",
                100,
                4.25f
            ),
            Food(
                "Sandvich Khorak",
                "9",
                "10",
                "Kermanshah",
                "https://timchar.ir/zarinshahr//Opitures/116557901140210.jpg",
                65,
                2.5f
            ),
            Food(
                "Hot Dog",
                "13",
                "3",
                "Tehran",
                "https://www.thespruceeats.com/thmb/O8cBOSCu3233XKmts7kPiT-52F4=/1685x1264/smart/filters:no_upscale()/air-fryer-hot-dogs-4802499-07-b327e219937c429a81efaf61519724a5.jpg",
                55,
                4.6f
            ),
            Food(
                "Makaroni",
                "14",
                "2.5",
                "Kerman",
                "https://saten.ir/wp-content/uploads/2021/07/makarooni.jpg",
                68,
                4f
            )
        )
        myAdpter = FoodAdapter(foodList.clone() as ArrayList<Food>,this)
        binding.recyvlerMain.adapter = myAdpter
        binding.recyvlerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.btnAddNewFood.setOnClickListener {
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
                        textName,
                        textPrice,
                        textDistance,
                        textCity,
                        urlPic,
                        textRatingNumber,
                        ratingBarStar
                    )
                    myAdpter.addFood(newFood)

                    dialog.dismiss()
                    binding.recyvlerMain.scrollToPosition(0)

                } else {
                    Toast.makeText(this, "لفطفا همه فیلد ها را پر کنید", Toast.LENGTH_SHORT).show()
                }

            }

        }
        binding.editSearch.addTextChangedListener { edittextInput ->//for example 'h'

            if (edittextInput!!.isNotEmpty()){
                //filter data
                val cloneList= foodList.clone()as ArrayList<Food>
                val fliterList = cloneList.filter { foodItem->
                    foodItem.textSubject.contains(edittextInput)

                }
                myAdpter.setData(ArrayList(fliterList))


            }else{
                //show all data
                myAdpter.setData(foodList.clone() as ArrayList<Food>)
            }
        }


    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()

        val updateDialogBindin = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(updateDialogBindin.root)
        dialog.setCancelable(true)
        dialog.show()
        updateDialogBindin.dialogEditFoodName.setText(food.textSubject)
        updateDialogBindin.dialogEditFoodCity.setText(food.textCity)
        updateDialogBindin.dialogEditFoodDistance.setText(food.textDestance)
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

            ){
                val textName = updateDialogBindin.dialogEditFoodName.text.toString()
                val textPrice = updateDialogBindin.dialogEditFoodPrice.text.toString()
                val textDistance = updateDialogBindin.dialogEditFoodDistance.text.toString()
                val textCity = updateDialogBindin.dialogEditFoodCity.text.toString()

                val newFood = Food(textName,textPrice,textDistance,textCity,food.urlImage,food.numOfRating,food.rating)

                myAdpter.updateFood(newFood, position)
                dialog.dismiss()
            }else{
                Toast.makeText(this, "لطفا همه فیلد هارا پر کنید", Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onFoodLongClickend(food: Food, position: Int) {
        val dialog =AlertDialog.Builder(this).create()

        val dialogDeleteBinding= DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        dialogDeleteBinding.dialogBtnDeleteNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogDeleteBinding.dialogBtnDeleteYes.setOnClickListener {
            myAdpter.removeFood(food,position)
            dialog.dismiss()

        }
    }



}