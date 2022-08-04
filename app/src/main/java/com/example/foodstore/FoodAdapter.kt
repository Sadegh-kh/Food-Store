package com.example.foodstore

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodstore.databinding.ItemFoodBinding
import com.example.foodstore.room.Food
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class FoodAdapter(private val data: ArrayList<Food>, private val foodEvents: FoodEvent) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    lateinit var binding: ItemFoodBinding

    inner class FoodViewHolder(binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imgMain = binding.itemImgMain
        private val textSubject = binding.itemTextSubject
        private val textCity = binding.itemTextCity
        private val textPrice = binding.itemTextPrice
        private val textDistance = binding.itemTextDistance
        private val Rating = binding.itemRatingMain
        private val textRating = binding.itemTxtRating

        fun bindData(position: Int) {
            textSubject.text = data[position].textSubject
            textCity.text = data[position].textCity
            textDistance.text = data[position].textDistance + " miles from you"
            textPrice.text = "$" + data[position].textPrice + " vip"
            Rating.rating = data[position].rating
            textRating.text = "(" + data[position].numOfRating.toString() + " Retings)"

            Glide.with(binding.root.context)
                .load(data[position].urlImage)
                .transform(RoundedCornersTransformation(16, 4))
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(imgMain)

            itemView.setOnClickListener {

                foodEvents.onFoodClicked(data[adapterPosition], adapterPosition)
            }
            itemView.setOnLongClickListener {
                foodEvents.onFoodLongClickend(data[adapterPosition], adapterPosition)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        Log.v("testApp", "onCreatViewHolder Called")
        binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        Log.e("testApp", "onBindViewHolder Called")
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addFood(newFood: Food) {
        //add food to list :

        data.add(0, newFood)
        notifyItemInserted(0)
    }

    fun removeFood(oldFood: Food, oldPosition: Int) {
        //remove item from list:

        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    fun updateFood(newFood: Food, position: Int) {
        data.set(position, newFood)
        notifyItemChanged(position)
    }

    fun setData(newList: ArrayList<Food>) {
        //set new data to list
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    interface FoodEvent {
        //1. create interface in adapter
        //2.get an objaect of interface in args of adapter
        //3.fill(call) object of interface with your data
        //4.implementation in MainActivity

        fun onFoodClicked(food: Food, position: Int)
        fun onFoodLongClickend(food: Food, position: Int)

    }

}