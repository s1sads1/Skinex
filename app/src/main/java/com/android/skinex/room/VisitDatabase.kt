package com.android.skinex.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [(Visit::class)], version = 1)
abstract class VisitDatabase : RoomDatabase() {
    private var INSTANCE: VisitDatabase? = null

    abstract fun VisitDao(): VisitDao


    //디비객체생성 가져오기
    fun getAppDatabase(context: Context?): VisitDatabase? {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context!!, VisitDatabase::class.java, "visit-db")
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

