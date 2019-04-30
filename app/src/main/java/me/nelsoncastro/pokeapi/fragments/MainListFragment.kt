package me.nelsoncastro.pokeapi.fragments

import android.app.Fragment
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pokemon_list_fragment.*
import kotlinx.android.synthetic.main.pokemon_list_fragment.view.*
import me.nelsoncastro.pokeapi.AppConstants
import me.nelsoncastro.pokeapi.Pojos.Pokemon
import me.nelsoncastro.pokeapi.Pojos.PokemonExtraInfo
import me.nelsoncastro.pokeapi.R
import me.nelsoncastro.pokeapi.adapters.PokemonAdapter
import me.nelsoncastro.pokeapi.adapters.PokemonSimpleListAdapter

import me.nelsoncastro.pokeapi.pokeAdapter
import java.lang.RuntimeException

class MainListFragment : android.support.v4.app.Fragment() {

    private lateinit var pokemons : ArrayList<Pokemon>
    private lateinit var pokeAdapter: pokeAdapter
    var listenerTool : SearchNewPokemonListener? = null

    companion object {
        fun newInstance(dataset: ArrayList<Pokemon>): MainListFragment{
            val newFragment = MainListFragment()
            newFragment.pokemons = dataset
            return newFragment
        }
    }

    interface SearchNewPokemonListener{
        fun searchPokemonType(pokemonName:String)

        fun managePortraitItemClick(pokemon: PokemonExtraInfo)

        fun manageLandScapeItemClick(pokemon: PokemonExtraInfo)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.pokemon_list_fragment, container, false)

        if(savedInstanceState != null) pokemons = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_LIST_KEY)!!

        initRecyclerView(resources.configuration.orientation, view)
        initSearchButton(view)

        return view
    }

    fun initSearchButton(view: View) { view.searchbarbutton.setOnClickListener {
        listenerTool?.searchPokemonType(searchbar.text.toString())
    }
    }


    fun initRecyclerView(orientation: Int, container: View) {
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pokeAdapter = PokemonAdapter(pokemons, {pokemon: PokemonExtraInfo -> listenerTool?.managePortraitItemClick(pokemon)})
            container.rv_pokemon_list.adapter = pokeAdapter as PokemonAdapter

        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokeAdapter = PokemonSimpleListAdapter(pokemons, {pokemon: PokemonExtraInfo -> listenerTool?.manageLandScapeItemClick(pokemon) })
            container.rv_pokemon_list.adapter = pokeAdapter as PokemonSimpleListAdapter
        }
        container.rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }
    fun updatePokemonAdapter(pokemonList : ArrayList<Pokemon>){
        pokeAdapter.changeDataSet(pokemonList)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is SearchNewPokemonListener){
            listenerTool =context

        }else{
            throw RuntimeException("Se necesta implementar la interfaz")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_LIST_KEY, pokemons)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }
}