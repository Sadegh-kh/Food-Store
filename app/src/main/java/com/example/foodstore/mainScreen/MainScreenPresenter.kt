package com.example.foodstore.mainScreen

import com.example.foodstore.model.Food
import com.example.foodstore.model.FoodDao


class MainScreenPresenter(
    private val foodDao: FoodDao
) : MainScreenContract.Presenter {
    var mainView: MainScreenContract.View? = null

    override fun firstRun() {

        val foodList = arrayListOf(
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

    override fun onAttach(view: MainScreenContract.View) {
        mainView = view

        val data = foodDao.getAllFood()
        mainView!!.showFoods(data)

    }

    override fun onDetach() {
        mainView = null
    }
    override fun onSearchFood(filter: String) {

        if (filter.isNotEmpty()) {

            // show filtered data
            val dataToShow = foodDao.searchFoods(filter)
            mainView!!.refreshFoods(dataToShow)

        } else {

            // show all data
            val dataToShow = foodDao.getAllFood()
            mainView!!.refreshFoods(dataToShow)

        }

    }
    override fun onAddNewFoodClicked(food: Food) {

        foodDao.insertOrUpdateFood(food)
        mainView!!.addNewFood(food)

    }
    override fun onDeleteAllClicked() {

        foodDao.deleteAllFoods()
        mainView!!.refreshFoods(foodDao.getAllFood())

    }
    override fun onUpdateFood(food: Food, pos: Int) {

        foodDao.insertOrUpdateFood(food)
        mainView!!.updateFood(food, pos)

    }
    override fun onDeleteFood(food: Food, pos: Int) {

        foodDao.deleteFood(food)
        mainView!!.deleteFood(food, pos)

    }

}