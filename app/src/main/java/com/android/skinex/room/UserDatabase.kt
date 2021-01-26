package com.android.skinex.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [(User::class)], version = 1)
abstract class UserDatabase : RoomDatabase() {
    private var INSTANCE: UserDatabase? = null

    abstract fun UserDao(): UserDao


    //디비객체생성 가져오기
    fun getAppDatabase(context: Context?): UserDatabase? {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context!!, UserDatabase::class.java, "user-db")
                .build()

            /*INSTANCE = Room.databaseBuilder(context, TodoDatabase.class , "user-db")
                    .allowMainThreadQueries() =>이걸 추가해서 AsyncTask를 사용안하고 간편하게할수있지만 오류가많아 실제 앱을 만들때 사용하면 안된다고한다.
                    .build();*/
        }
        return INSTANCE
    }

    //디비객체제거
    fun destroyInstance() {
        INSTANCE = null
    }
}

