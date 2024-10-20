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
    val purchaseCount: Int = 0,
    val totalWords: Int = 0,
    val hoursToRead: Int = 0,
    val numberOfPages: Int = 0,

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
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
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
        parcel.writeInt(totalWords)
        parcel.writeInt(hoursToRead)
        parcel.writeInt(numberOfPages)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (title != other.title) return false
        if (author != other.author) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (category != other.category) return false
        if (coverImageUrl != other.coverImageUrl) return false
        if (pdfUrl != other.pdfUrl) return false
        if (rating != other.rating) return false
        if (purchaseCount != other.purchaseCount) return false
        if (totalWords != other.totalWords) return false
        if (hoursToRead != other.hoursToRead) return false
        if (numberOfPages != other.numberOfPages) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + coverImageUrl.hashCode()
        result = 31 * result + pdfUrl.hashCode()
        result = 31 * result + rating.hashCode()
        result = 31 * result + purchaseCount
        result = 31 * result + totalWords
        result = 31 * result + hoursToRead.hashCode()
        result = 31 * result + numberOfPages
        return result
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book = Book(parcel)
        override fun newArray(size: Int): Array<Book?> = arrayOfNulls(size)
    }
}
