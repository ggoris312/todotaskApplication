package com.example.giog.todolist;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.giog.todolist.fragment.TodoListViewDetailFragment;
import com.example.giog.todolist.fragment.TodoListViewFragment;
import com.example.giog.todolist.model.TodoLab;
import com.example.giog.todolist.model.TodoModel;

public class MainActivity extends AppCompatActivity implements TodoListViewFragment.Callbacks, TodoListViewDetailFragment.Callbacks{

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.listview_fragment_container);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.listview_fragment_container, fragment)
                    .commit();
        }
    }

    protected Fragment createFragment() {
        return new TodoListViewFragment();
    }

    @Override
    public void onTodoSelected(TodoModel todoModel) {
        Fragment newDetail = TodoListViewDetailFragment.newInstance(todoModel.getmId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listview_detail_fragment_container, newDetail)
                .commit();
    }

    @Override
    public void onTodoUpdated(TodoModel todoModel) {
        TodoListViewFragment listFragment = (TodoListViewFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.listview_fragment_container);
        listFragment.updateUI();

        if(TodoLab.get(this).getTodo(todoModel.getmId()) == null){
            TodoListViewDetailFragment timerScreenFragment = (TodoListViewDetailFragment)
                    getSupportFragmentManager()
                            .findFragmentById(R.id.listview_detail_fragment_container);
            getSupportFragmentManager().beginTransaction().remove(timerScreenFragment).commit();
        }
    }
}
