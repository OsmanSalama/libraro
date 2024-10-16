package com.example.libraro.model

import android.os.Parcel
import android.os.Parcelable

data class Book(
    var id: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val coverImageUrl: String = "",
    val pdfUrl: String = "",
    val rating: Double = 0.0,
    val purchaseCount: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeString(category)
        parcel.writeString(coverImageUrl)
        parcel.writeString(pdfUrl)
        parcel.writeDouble(rating)
        parcel.writeInt(purchaseCount)
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book = Book(parcel)
        override fun newArray(size: Int): Array<Book?> = arrayOfNulls(size)
    }
}
