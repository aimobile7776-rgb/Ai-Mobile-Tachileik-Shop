package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites ORDER BY addedTime DESC")
    fun getAllFavourites(): Flow<List<FavouriteProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(product: FavouriteProduct)

    @Query("DELETE FROM favourites WHERE id = :productId")
    suspend fun deleteFavouriteById(productId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE id = :productId)")
    suspend fun isFavourite(productId: String): Boolean
}
