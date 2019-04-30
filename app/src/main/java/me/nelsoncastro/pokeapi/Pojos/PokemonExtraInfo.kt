package me.nelsoncastro.pokeapi.Pojos

import android.os.Parcel
import android.os.Parcelable

data class PokemonExtraInfo(
    val id:String="N/A",
    val name: String="N/A",
    val peso: String="N/A",
    val Height: String="N/A",
    val Experience: String="N/A",
    val Image:String="N/A"
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(peso)
        parcel.writeString(Height)
        parcel.writeString(Experience)
        parcel.writeString(Image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonExtraInfo> {
        override fun createFromParcel(parcel: Parcel): PokemonExtraInfo {
            return PokemonExtraInfo(parcel)
        }

        override fun newArray(size: Int): Array<PokemonExtraInfo?> {
            return arrayOfNulls(size)
        }
    }
}