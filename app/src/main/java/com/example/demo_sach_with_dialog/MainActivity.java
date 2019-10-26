package com.example.demo_sach_with_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button chuyen;

    Dialog dialog;

    EditText id_Book, title, id_author;
    Button bt_save, bt_select, bt_exit, bt_update, bt_delete;
    GridView gv_display;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chuyen = (Button) findViewById(R.id.button_Chuyen);

        chuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });


    }

    private void showMenuDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_main_book);
        dialog.setTitle("Thông tin sách");

        id_Book = (EditText)dialog.findViewById(R.id.edt_ma);
        title = (EditText)dialog.findViewById(R.id.edt_tieude);
        id_author = (EditText)dialog.findViewById(R.id.edt_tacgia);
        gv_display = (GridView)dialog.findViewById(R.id.gv_ThongTin);
        dbHelper = new DBHelper(this);
        bt_delete=(Button)dialog.findViewById(R.id.btn_delete);
        bt_save = (Button)dialog.findViewById(R.id.btn_save);
        bt_update=(Button)dialog.findViewById(R.id.btn_update);
        bt_delete.setEnabled(false);
        bt_save.setEnabled(false);
        bt_update.setEnabled(false);

        id_Book.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bt_delete.setEnabled(false);
                bt_save.setEnabled(false);
                bt_update.setEnabled(false);
                String book_id = id_Book.getText().toString();

                if(book_id.length() >= 1)
                {
                    bt_delete.setEnabled(true);
                    bt_save.setEnabled(true);
                    bt_update.setEnabled(true);
                }


            }
        });


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Chưa nhập tiêu đề", Toast.LENGTH_SHORT).show();
                }
                else if (id_author.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Chưa nhập id tác giả", Toast.LENGTH_SHORT).show();
                }
                else{
                    Book book = new Book();
                    book.setId_book(Integer.parseInt(id_Book.getText().toString()));
                    book.setTitle(title.getText().toString());

                        book.setId_author(Integer.parseInt(id_author.getText().toString()));

                        if (dbHelper.insertBook(book))
                            Toast.makeText(getApplicationContext(), "Đã lưu ", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(getApplicationContext(), "Không thể lưu", Toast.LENGTH_SHORT).show();
                    }

                }


        });

        bt_select = (Button)dialog.findViewById(R.id.btn_select);
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                ArrayList<Book> books = new ArrayList<>();
                books = dbHelper.getAllBook();
                for (Book b : books) {
                    list.add(b.getId_book() + "");
                    list.add(b.getTitle() + "");
                    list.add(b.getId_author() + "");
                }
                Adapter adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                gv_display.setAdapter((ListAdapter) adapter);
                if (gv_display.getCount()==0){
                    Toast.makeText(getApplicationContext(),"không có danh sách",Toast.LENGTH_SHORT).show();
                }

            }
        });


        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean st=dbHelper.deleteBook(Integer.parseInt(id_Book.getText().toString()));

                if (st==true)
                    Toast.makeText(getApplicationContext(),"Đã xóa",Toast.LENGTH_SHORT).show();
                else
                {
                    Toast.makeText(getApplicationContext(),"Không thể xóa",Toast.LENGTH_SHORT).show();
                }


                ArrayList<String> list = new ArrayList<>();
                ArrayList<Book> books = new ArrayList<>();
                books = dbHelper.getAllBook();
                for (Book b : books) {
                    list.add(b.getId_book() + "");
                    list.add(b.getTitle() + "");
                    list.add(b.getId_author() + "");
                }
                Adapter adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                gv_display.setAdapter((ListAdapter) adapter);
                id_Book.setText("");
                title.setText("");
                id_author.setText("");
                bt_delete.setEnabled(false);
            }
        });


        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Chưa nhập tiêu đề", Toast.LENGTH_SHORT).show();
                }
                else if (id_author.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Chưa nhập id tác giả", Toast.LENGTH_SHORT).show();
                }
                else{
                    Book book = dbHelper.getBook(Integer.parseInt(id_Book.getText().toString()));
                    book.setId_book(Integer.parseInt(id_Book.getText().toString()));
                    book.setTitle(title.getText().toString());
                    book.setId_author(Integer.parseInt(id_author.getText().toString()));
                    if (dbHelper.updateBook(book))
                        Toast.makeText(getApplicationContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(getApplicationContext(), "Khôgn thể cập nhật", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bt_exit = (Button)dialog.findViewById(R.id.button_Exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dialog.show();


    }


}
