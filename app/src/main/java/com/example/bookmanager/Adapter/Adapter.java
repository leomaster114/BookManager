package com.example.bookmanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmanager.Model.Book;
import com.example.bookmanager.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Book> books;
    Context context;
    private int resource;

    public Adapter( Context context, int resource,ArrayList<Book> books) {
        Log.d("Adapter", "Adapter: on constructor");
        this.books = books;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        Log.d("Adapter", "Adapter: getcount"+books.size());
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,null);

            viewHolder = new ViewHolder();
            viewHolder.book_name = convertView.findViewById(R.id.item_name);
            viewHolder.book_type = convertView.findViewById(R.id.item_type);
            viewHolder.imgBook = convertView.findViewById(R.id.item_book);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

       Book book = books.get(position);
        if(book!=null){
            byte[] img = book.getImageUri();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
            viewHolder.imgBook.setImageBitmap(bitmap);
            viewHolder.book_name.setText(book.getBookName());
            Log.d("Adapter", "getView: book name = "+book.getBookName());
            viewHolder.book_type.setText(book.getType());
        }
        return convertView;
    }
    public class ViewHolder{
        ImageView imgBook;
        TextView book_name;
        TextView book_type;
    }
}
