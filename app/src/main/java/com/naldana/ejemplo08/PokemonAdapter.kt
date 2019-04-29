package com.naldana.ejemplo08

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.naldana.ejemplo08.models.Pokemon
import com.naldana.ejemplo08.utilities.ActivityPokemonFicha
import kotlinx.android.synthetic.main.list_element_pokemon.view.*

class PokemonAdapter(val items: List<Pokemon>, val context: Context) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_element_pokemon, parent, false)


        return ViewHolder(view, context)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        Glide.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+items[position].idPokeApi+".png").into(holder.pokeFaceItem)
    }


    class ViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        private lateinit var nombPok: String
        private lateinit var urlPok: String
        private lateinit var idPokeApi: String
        lateinit var pokeFaceItem: ImageView
        lateinit var metInputPokemonId: EditText

        init{
            itemView.setOnClickListener{
                //Log.v("click", "click en "+nombPok)
                val intent= Intent(context, ActivityPokemonFicha::class.java )
                intent.putExtra("URLPokemon", urlPok)
                intent.putExtra("IDPokemon", idPokeApi)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(item: Pokemon) = with(itemView) {
            nombPok= item.name
            urlPok= item.url
            idPokeApi= item.idPokeApi


            tv_pokemon_name.text = nombPok
            pokeFaceItem= pokeFace
        }

    }
}



