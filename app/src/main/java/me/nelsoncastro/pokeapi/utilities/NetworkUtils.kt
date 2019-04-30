package me.nelsoncastro.pokeapi.utilities

import android.net.Uri
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class NetworkUtils {

    val POKEMON_API_BASEURL = "https://pokeapi.co/api/v2/pokemon"

    fun buildtSearchUrl(pokemonId: String) : URL {
        val builtUri = Uri.parse(POKEMON_API_BASEURL)
            .buildUpon()
            .appendPath(pokemonId)
            .build()
        return try {
            URL(builtUri.toString())

        }catch (e : MalformedURLException){
            URL("")
        }

    }

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL):String{
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream

            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            return if(hasInput){
                scanner.next()
            }else{
                ""
            }
        }finally {
            urlConnection.disconnect()
        }
    }

}