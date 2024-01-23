package com.tmobile.userapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tmobile.core.data.models.User
import com.tmobile.userapp.ui.UserUIModel

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    val email: String,
)

fun List<UserEntity>.mapToUiModelList() = map {
    UserUIModel(it.firstName, it.lastName, it.email, "")
}

fun User.mapToEntity() = UserEntity(this.id, this.first_name, this.last_name, this.email)