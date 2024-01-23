package com.tmobile.userapp.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getAllUserEntities(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserEntity(userEntity: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clear()
}