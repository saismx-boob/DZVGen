package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CreationDao {

    @Query("SELECT * FROM creations ORDER BY createdAt DESC")
    fun getAllCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE type = :type ORDER BY createdAt DESC")
    fun getCreationsByType(type: String): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE id = :id LIMIT 1")
    fun getCreationById(id: Long): Flow<CreationEntity?>

    @Query("SELECT * FROM creations WHERE prompt LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchCreations(query: String): Flow<List<CreationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreation(creation: CreationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(creations: List<CreationEntity>)

    @Update
    suspend fun updateCreation(creation: CreationEntity)

    @Delete
    suspend fun deleteCreation(creation: CreationEntity)

    @Query("DELETE FROM creations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE creations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM creations")
    suspend fun getCount(): Int

    @Query("DELETE FROM creations")
    suspend fun clearAll()
}
