package me.nelsoncastro.pokeapi.adapters

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_view_pokemon.view.*
import kotlinx.android.synthetic.main.list_element_pokemon.view.*
import me.nelsoncastro.pokeapi.R
import me.nelsoncastro.pokeapi.Pojos.Pokemon
import me.nelsoncastro.pokeapi.Pojos.PokemonExtraInfo
import me.nelsoncastro.pokeapi.pokeAdapter
import me.nelsoncastro.pokeapi.utilities.NetworkUtils
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class PokemonAdapter(var pokemons: List<Pokemon>, val clickListener: (PokemonExtraInfo) -> Unit): RecyclerView.Adapter<PokemonAdapter.ViewHolder>(), pokeAdapter {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemons[position], clickListener)
    }

    override fun changeDataSet(newDataSet: List<Pokemon>) {
        this.pokemons =newDataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pokemons.size


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var pokemonBase : PokemonExtraInfo

        fun bind(pokemon: Pokemon, clickListener: (PokemonExtraInfo) -> Unit) = with(itemView){
            tv_pokemon_nameUP.text = pokemon.name
            Glide.with(itemView.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.id + ".png")
                .placeholder(R.drawable.ic_launcher_background)
                .into(pokemon_cv)
            FetchPokemon().execute(pokemon.url)
            this.setOnClickListener { clickListener(pokemonBase) }
        }
        private inner class FetchPokemon : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String?): String {
                if (params.isNullOrEmpty()) return ""

                val builtUri = Uri.parse(params[0]).buildUpon().build()
                val url = URL(builtUri.toString())
                return try {
                    NetworkUtils().getResponseFromHttpUrl(url)
                } catch (e: IOException) {
                    "Error"
                }
            }

            override fun onPostExecute(pokemonInfo: String) {
                super.onPostExecute(pokemonInfo)
                if (!pokemonInfo.isEmpty()) {
                    val PokemonJson = JSONObject(pokemonInfo)
                    pokemonBase = PokemonExtraInfo(PokemonJson.getString("id"), PokemonJson.getString("name"), PokemonJson.getString("weight"),
                        PokemonJson.getString("height"), PokemonJson.getString("base_experience"), PokemonJson.getJSONObject("sprites").getString("front_default"))
                }
            }
        }
    }


}

