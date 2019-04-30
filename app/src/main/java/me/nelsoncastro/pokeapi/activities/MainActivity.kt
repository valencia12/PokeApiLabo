package me.nelsoncastro.pokeapi.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import me.nelsoncastro.pokeapi.AppConstants
import me.nelsoncastro.pokeapi.R
import me.nelsoncastro.pokeapi.Pojos.Pokemon
import me.nelsoncastro.pokeapi.Pojos.PokemonExtraInfo
import me.nelsoncastro.pokeapi.fragments.MainContentFragment
import me.nelsoncastro.pokeapi.fragments.MainListFragment
import me.nelsoncastro.pokeapi.utilities.NetworkUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity(), MainListFragment.SearchNewPokemonListener{

    private lateinit var mainFragment : MainListFragment
    private lateinit var mainContentFragment: MainContentFragment

    private var pokemonList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokemonList = savedInstanceState?.getParcelableArrayList(AppConstants.datase_savinstance_key) ?: ArrayList()
        FetchPokemonTask().execute()
        initMainFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.datase_savinstance_key, pokemonList)
        super.onSaveInstanceState(outState)
    }

    fun initMainFragment (){
        mainFragment = MainListFragment.newInstance(pokemonList)
        val resource = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else{
            mainContentFragment = MainContentFragment.newInstance(PokemonExtraInfo())
            changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
            R.id.land_main_fragment
        }

        changeFragment(resource, mainFragment)

    }

    fun addPokemonToList(pokemon: Pokemon) {
        pokemonList.add(pokemon)
        mainFragment.updatePokemonAdapter(pokemonList)
        Log.d("Number", pokemonList.size.toString())
    }


    override fun searchPokemonType(pokemonType: String){
        FetchPokemonTask().execute(pokemonType)
        mainFragment.updatePokemonAdapter(pokemonList)
    }
    override fun managePortraitItemClick(pokemon: PokemonExtraInfo) {
        val pokemonBundle = Bundle()
        pokemonBundle.putParcelable("POKEMON", pokemon)
        startActivity(Intent(this, PokemonViewer::class.java).putExtras(pokemonBundle))
    }

    private fun changeFragment(id: Int, frag : android.support.v4.app.Fragment){
        supportFragmentManager.beginTransaction().replace(id, frag).commit()
    }

    override fun manageLandScapeItemClick(pokemon: PokemonExtraInfo) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
    }

    /*
    private fun pokemonItemClicked(item: Pokemon){
        startActivity(Intent(this, PokemonViewer::class.java).putExtra("CLAVIER", item.url))
    }
*/
    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {
        var flag = false
        override fun doInBackground(vararg params: String): String {
            var typePokemon: String = ""
            if (!params.isNullOrEmpty()) {
                flag = true
                typePokemon = params[0]
            }

            // val pokemonName = params[0]
            val pokemonUrl = if (!flag) {
                NetworkUtils().buildtSearchUrl(typePokemon)
            } else {
                URL((Uri.parse("https://pokeapi.co/api/v2/type").buildUpon().appendPath(typePokemon)).toString())
            }
            Log.v("http a", pokemonUrl.toString())
            return try {
                NetworkUtils().getResponseFromHttpUrl(pokemonUrl)
            } catch (e: IOException) {
                ""
            }
        }

        override fun onPostExecute(pokemonInfo: String) {
            super.onPostExecute(pokemonInfo)

            if (!pokemonInfo.isEmpty()) {
                val pokemonJSON: JSONArray
                if (!flag) {
                    pokemonJSON= JSONObject(pokemonInfo).getJSONArray("results")
                    for (i in 0 until pokemonJSON.length()) {

                        val obj = pokemonJSON.getJSONObject(i)
                        var id = obj.getString("url").substring(34, (obj.getString("url").length - 1))
                        val pokemon = Pokemon(id, obj.getString("name"), obj.getString("url"))
                        pokemonList.add(pokemon)
                    }
                }else{
                    pokemonList.clear()
                    pokemonJSON=JSONObject(pokemonInfo).getJSONArray("pokemon")
                    for (i in 0 until pokemonJSON.length()) {

                        val obj = pokemonJSON.getJSONObject(i)
                        var id = obj.getJSONObject("pokemon").getString("url").substring(34, (obj.getJSONObject("pokemon").getString("url").length - 1))
                        val pokemon = Pokemon(id, obj.getJSONObject("pokemon").getString("name"), obj.getJSONObject("pokemon").getString("url"))
                        pokemonList.add(pokemon)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "A ocurrido un error,", Toast.LENGTH_LONG).show()
            }
        }
    }




}
