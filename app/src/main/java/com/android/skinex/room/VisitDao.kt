package com.android.skinex.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*
import kotlin.collections.ArrayList as CollectionsArrayList

@Dao
interface VisitDao {
    @Query("SELECT * FROM visit")
    fun getAll(): List<Visit>

    @Query("SELECT * FROM visit WHERE name = :insertName")
    fun getNameAll(insertName : String?): List<Visit>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Query( "SELECT *  FROM visit WHERE name = :search")
    fun getBirthDay(search : String?) : Array<Visit>

    @Query( "SELECT name FROM visit WHERE birth_day = :insert")
    fun getName(insert : String?) : String

    @Insert
    fun insertAll(vararg visits: Visit)

    @Delete
    fun delete(visit: Visit)

}