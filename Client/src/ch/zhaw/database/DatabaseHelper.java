package ch.zhaw.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static DatabaseHelper instance;

//	   private SQLiteDatabase db;
	private DatabaseHelper databaseHelper = null;

	   // Konstruktor
	   public DatabaseHelper(Context activity) {
	      super(activity, "budgetDatabase", null, 1); 
//	      db = getWritableDatabase();
	   }
	   
	   public static DatabaseHelper getInstance(Context activity) {
		    if (DatabaseHelper.instance == null) {
		    	DatabaseHelper.instance = new DatabaseHelper(activity);
		      }
		      return DatabaseHelper.instance;
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	     try {
	        // Tabelle anlegen 
	        String sql = "CREATE TABLE IF NOT EXISTS expenses "  +
	                      "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                      "serverId UNSIGNED INTEGER, " +
	                      "userId INTEGER NOT NULL, " + 
	                      "description VARCHAR(255) NOT NULL, " +
	                      "value decimal(10,2) NOT NULL, " + 
	                      "date DATE not null)";
	        System.out.println(sql); 
	        db.execSQL(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS groups "  +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "serverId UNSIGNED INTEGER NOT NULL, " +
                    "userId INTEGER NOT NULL, " + 
                    "groupName VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(40) NOT NULL, " +
                    "budget decimal(10,2) NOT NULL)";
	        System.out.println(sql);
	        db.execSQL(sql); 
	        
	        sql = "CREATE TABLE IF NOT EXISTS users "  +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
	        		"serverId UNSIGNED INTEGER NOT NULL, " +
                    "groupId INTEGER, " +
                    "username VARCHAR(255) NOT NULL, " + 
                    "password VARCHAR(40) NOT NULL)";
	        System.out.println(sql);
	        db.execSQL(sql); 
	     }
	     catch(Exception ex) {
	       System.out.println("carpelibrum" + ex.getMessage());
	     }
	   }

	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	                        int newVersion) {
	      // auf Versionswechsel reagieren
	   }
	   
//	   @Override
//	   public synchronized void close() {
//		   if (myDataBase != null) {
//			   .close();
//		   }
//		   
//		   super.close();
//	   }
	}
