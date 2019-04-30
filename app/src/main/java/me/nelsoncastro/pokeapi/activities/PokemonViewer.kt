package me.nelsoncastro.pokeapi.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewer_element_pokemon.*
import me.nelsoncastro.pokeapi.Pojos.PokemonExtraInfo
import me.nelsoncastro.pokeapi.R

class PokemonViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_element_pokemon)
        setSupportActionBar(toolbarviewer)
        collapsingtoolbarviewer.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingtoolbarviewer.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        val reciever: PokemonExtraInfo = intent?.extras?.getParcelable("POKEMON") ?: PokemonExtraInfo()
        init(reciever)
    }

    fun init(pokemon: PokemonExtraInfo){
        Glide.with(this)
            .load(pokemon.Image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(app_bar_image_viewer)
        collapsingtoolbarviewer.title = pokemon.name
        app_bar_rating_viewer.text = pokemon.id
        plot_viewer.text = pokemon.Height
        director_viewer.text = pokemon.peso
        actors_viewer.text = pokemon.Experience
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {onBackPressed();true}
            else -> super.onOptionsItemSelected(item)
        }
    }

}