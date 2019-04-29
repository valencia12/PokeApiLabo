package com.naldana.ejemplo08.utilities

import android.net.Uri
import android.support.annotation.Nullable
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

object NetworkUtils {

    val POKEMON_API_BASE_URL = "https://pokeapi.co/api/v2/"

    private val TAG = NetworkUtils::class.java.simpleName

    fun buildUrl(POKEMON_INFO: String, @Nullable POKEMONID: String? = null): URL {
        val builtUri = Uri.parse(POKEMON_API_BASE_URL).buildUpon()
                .appendPath(POKEMON_INFO)

        if (POKEMONID != null) {
            builtUri.appendPath(POKEMONID)
        }
        builtUri.build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        Log.d(TAG, "Built URI " + url!!)

        return url
    }

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream

            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            return if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}
