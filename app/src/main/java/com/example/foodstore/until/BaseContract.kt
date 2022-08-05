package com.example.foodstore.until

interface BasePresenter<T>{
    fun onAttach(view: T)
    fun onDetach()
}
interface BaseView{
    //for repetition functions views
}