package com.example.bookmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.bookmanager.Model.Book;
import com.example.bookmanager.Model.User;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private static String DBNAME = "bookmanager";
    private String User_table = "Users";
    private String Book_table = "Books";
    private String Id = "Id";
    private String Username = "username";
    private String Password = "password";
    private String BookName = "bookName";
    private String Type = "type";
    private String Author = "author";
    private String Des = "description";
    private String Uri = "imageUri";
    Context context;

    public MyDatabase(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
        Log.d("Database", "MyDatabase: constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT)", User_table, Id, Username, Password);
        String createBookTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT,%s TEXT, %s TEXT, %s BLOB)",
                Book_table, Id, BookName, Author, Type, Des, Uri);
        db.execSQL(createBookTable);
        db.execSQL(createUserTable);
        Log.d("Database", "onCreate: successfull");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_book_table = String.format("drop table if exist %s", Book_table);
        String drop_user_table = String.format("drop table if exist %s", User_table);
        onCreate(db);
        db.close();
    }

    public void addUser(User user) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Username, user.getUsername());
        contentValues.put(Password, user.getPassword());
        database.insert(User_table, null, contentValues);
        database.close();
        Log.d("Database", "addUser: successfull");
    }
    public User findUserByName(String username){

        User user = new User("","");
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {Id,Username, Password};
        String clause = Username + "=?";
        String[] Args = new String[]{username};
        Cursor cursor = database.query(User_table, projection, clause, Args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        Log.d("Database", "findUserByName: "+user.getUsername()+" "+user.getPassword());
        return user;
    }
    public User getUser(int id) {
        User user = new User();
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {Id, Username, Password};
        String clause = Id + "=?";
        String[] Args = new String[]{String.valueOf(id)};
        Cursor cursor = database.query(User_table, projection, clause, Args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return user;
    }

    public void addBook(Book book) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Books VALUES (NULL,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,book.getBookName());
        statement.bindString(2,book.getAuthor());
        statement.bindString(3,book.getType());
        statement.bindString(4,book.getDescription());
       statement.bindBlob(5,book.getImageUri());
        Log.d("database", "addBook: ");
       statement.executeInsert();
    }

    public Book getBook(int id) {
        Book book = new Book();
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {Id, BookName, Author, Type, Des, Uri};
        String clause = Id + "=?";
        String[] Args = new String[]{String.valueOf(id)};
        Cursor cursor = database.query(Book_table, projection, clause, Args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setType(cursor.getString(3));
                book.setDescription(cursor.getString(4));
                book.setImageUri(cursor.getBlob(5));
            } while (cursor.moveToNext());
        }
        return book;
    }

    public ArrayList<Book> getAllBook() {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + Book_table;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do{
                Book book = new Book();
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setType(cursor.getString(3));
                book.setDescription(cursor.getString(4));
                book.setImageUri(cursor.getBlob(5));
                books.add(book);
            }while (cursor.moveToNext());
        }
        return books;
    }

    public ArrayList<Book> getBookByType(String type) {
        Log.d("database", "getBookByType: "+type);
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {Id, BookName, Author, Type, Des, Uri};
        String whereClause = Type + "=?";
        String[] whereArgs = new String[]{type};
        Cursor cursor = database.query(Book_table, projection, whereClause, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setType(cursor.getString(3));
                book.setDescription(cursor.getString(4));
                book.setImageUri(cursor.getBlob(5));
                books.add(book);
            } while (cursor.moveToNext());
        }
        return books;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookName, book.getBookName());
        values.put(Author, book.getAuthor());
        values.put(Type, book.getType());
        values.put(Des, book.getDescription());
        values.put(Uri, book.getImageUri());
        return database.update(Book_table, values, Id + "=" + book.getId(), null) > 0;
    }

    public boolean deleteBook(int id) {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(Book_table, Id + "=?" ,new String[]{String.valueOf(id)}) > 0;
    }

    public Book getBookByName(String bookName) {
        Book book = new Book();
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {Id, BookName, Author, Type, Des, Uri};
        String whereClause = BookName + "=?";
        String[] whereArgs = new String[]{bookName};
        Cursor cursor = database.query(Book_table, projection, whereClause, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setType(cursor.getString(3));
                book.setDescription(cursor.getString(4));
                book.setImageUri(cursor.getBlob(5));
            } while (cursor.moveToNext());
        }
        return book;
    }
}
