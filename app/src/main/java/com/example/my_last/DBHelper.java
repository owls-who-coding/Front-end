package com.example.my_last;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "data.db";  //DB이름
    final static int DB_VERSION = 2; //DB버전


    //생성자
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //테이블의 구조는 여기서 설계
        String qry = "CREATE TABLE student(num INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL)";
        sqLiteDatabase.execSQL(qry);


        //없으면 썰렁하니 아무 데이터라도 넣어주기
        qry = "INSERT INTO student(name) VALUES('황수고')";
        sqLiteDatabase.execSQL(qry);
        qry = "INSERT INTO student(name) VALUES('김영우')";
        sqLiteDatabase.execSQL(qry);
        qry = "INSERT INTO student(name) VALUES('이샹슈')";
        sqLiteDatabase.execSQL(qry);

    }


    //버전 업데이트 될때마다 호출 되는데 마지막에 onCreate도 같이 실행되기 때문에 여기서 먼저 DB에 존재하는 테이블들을 지워줘야함.
    //한마디로 초기화역할
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String qry = "DROP TABLE IF EXISTS student";
        sqLiteDatabase.execSQL(qry);

        onCreate(sqLiteDatabase);

    }
}
