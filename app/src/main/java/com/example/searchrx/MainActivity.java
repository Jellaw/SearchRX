package com.example.searchrx;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
        SearchView searchView;
        RxSearchObservable rxSearchObservable;
        TextView textView;
        ListView listView;
        ArrayAdapter adapter;
        List mylist;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListView();
        RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(text -> {
                    if (text.isEmpty()) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    SearchInList(mylist,s);
                    textView.setText(s);
                });
    }
    private void init(){
        searchView = findViewById(R.id.searchView);
        textView = findViewById(R.id.textView);
        listView =findViewById(R.id.listView);
    }
    private void initListView(){
        mylist = new ArrayList<>();
        mylist.add("C");
        mylist.add("C++");
        mylist.add("C#");
        mylist.add("Java");
        mylist.add("Advanced java");
        mylist.add("Interview prep with c++");
        mylist.add("Interview prep with java");
        mylist.add("data structures with c");
        mylist.add("data structures with java");

        // Set adapter to ListView
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter);
    }
    private void SearchInList(List list, String query){
        if (list.contains(query)) {
            adapter.getFilter().filter(query);
            adapter.notifyDataSetChanged();
        }
        else {
            // Search query not found in List View
            Toast
                    .makeText(MainActivity.this,
                            "Not found",
                            Toast.LENGTH_LONG)
                    .show();
        }
    }
}