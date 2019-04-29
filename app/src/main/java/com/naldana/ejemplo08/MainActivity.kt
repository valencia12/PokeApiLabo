package com.naldana.ejemplo08

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.naldana.ejemplo08.models.Pokemon
import com.naldana.ejemplo08.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.IOException
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {
    var flagSearchTipo=false
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var pokemonMutableList: MutableList<Pokemon> = ArrayList()
    lateinit var mETIdNombre: EditText
    lateinit var mButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mETIdNombre= findViewById(R.id.etInputPokemonId)
        mButton= findViewById(R.id.btn_submit)
        mButton.setOnClickListener{
            flagSearchTipo=true
            Log.v("flag", "Si llego 1")
            FetchPokemonTask().execute()
        }
        FetchPokemonTask().execute()
    }


    fun initRecycler() {


        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemonMutableList, this)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
          //  var POKEMON_INFO="pokemon"
            val pokeAPI = elegirBusqueda()

            return try {
                Log.v("flag", "Hizo http "+pokeAPI)
                NetworkUtils.getResponseFromHttpUrl(pokeAPI)!!
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }
        fun elegirBusqueda() : URL {
            if(!flagSearchTipo){
                return NetworkUtils.buildUrl("pokemon")
            }else{
                Log.v("flag", "Entro a elegirBusqueda")
                return NetworkUtils.buildUrl("type", mETIdNombre.text.toString())

            }
        }


        override fun onPostExecute(pokemonInfo: String?) {

            if (pokemonInfo != null || pokemonInfo != "") {

                var jsonPokeInfo= JSONObject(pokemonInfo)
                var arrayPokemons: JSONArray
                var pokeType: String?
                pokemonMutableList.clear()

                if(flagSearchTipo){
                    pokeType= jsonPokeInfo.getString("name").toString()
                    mETIdNombre.setText(pokeType)
                    arrayPokemons= jsonPokeInfo.getJSONArray("pokemon")
                    for(i in 0 until arrayPokemons.length()){

                        var id= arrayPokemons.getJSONObject(i).getJSONObject("pokemon").getString("url").substring(34,(arrayPokemons.getJSONObject(i).getJSONObject("pokemon").getString("url").length-1))
                        var nuevoPok= Pokemon(i, arrayPokemons.getJSONObject(i).getJSONObject("pokemon").getString("name"), arrayPokemons.getJSONObject(i).getJSONObject("pokemon").getString("url"),id)
                        pokemonMutableList.add(nuevoPok)
                    }
                }else{
                    arrayPokemons= jsonPokeInfo.getJSONArray("results")
                    for(i in 0 until arrayPokemons.length()) {
                        var id= arrayPokemons.getJSONObject(i).getString("url").substring(34,(arrayPokemons.getJSONObject(i).getString("url").length-1))
                        var nuevoPok= Pokemon(i, arrayPokemons.getJSONObject(i).getString("name"), arrayPokemons.getJSONObject(i).getString("url"),id)
                        pokemonMutableList.add(nuevoPok)
                        //Log.v("Print", pokemonMutableList[i].toString())
                    }
                }
                //Log.v("Split", id)

                initRecycler()
            }
        }
    }
}
