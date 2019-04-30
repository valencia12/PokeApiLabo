package me.nelsoncastro.pokeapi.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_element_pokemon.view.*
import kotlinx.android.synthetic.main.main_content_fragment_layout.*
import me.nelsoncastro.pokeapi.Pojos.Pokemon
import me.nelsoncastro.pokeapi.Pojos.PokemonExtraInfo
import me.nelsoncastro.pokeapi.R

class MainContentFragment : android.support.v4.app.Fragment(){

    var pokemon = PokemonExtraInfo()

    companion object {
        fun newInstance(pokemon: PokemonExtraInfo): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.main_content_fragment_layout, container, false)
        bindData(view)

        return view
    }

    fun bindData(view: View) {
        view.tv_pokemon_name.text = pokemon.name
        view.tv_pokemon_id.text = pokemon.id
        view.tv_pokemon_type.text = pokemon.peso

        Glide.with(view)
            .load(pokemon.Image)
            .placeholder(R.drawable.ic_pokemon_go)
            .into(view.tv_pokemon_img)

    }

}