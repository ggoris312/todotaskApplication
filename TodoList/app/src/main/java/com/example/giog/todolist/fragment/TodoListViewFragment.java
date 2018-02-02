package com.example.giog.todolist.fragment;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giog.todolist.R;
import com.example.giog.todolist.model.TodoLab;
import com.example.giog.todolist.model.TodoModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wgoris31 on 2/1/2018.
 */

public class TodoListViewFragment extends Fragment {
    @BindView(R.id.todo_recycler_view)
    RecyclerView mTodoRecyclerView;

    @BindView(R.id.add_new_todo)
    Button mNewTodoButton;

    private TodoAdapter mAdapter;
    private Callbacks mCallback;

    /**     * Required interface for hosting activities.     */
    public interface Callbacks {
        void onTodoSelected(TodoModel todoModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (Callbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_listview, container, false);
        ButterKnife.bind(this, view);

        mTodoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoModel todoModel = new TodoModel();                                                  //Create new TimerConfiguration, see models/timerscreenmodel/TimerConfiguration
                TodoLab.get(getActivity()).addTimer(todoModel);                                //added it to the TimerConfigurationLab, see models/timerscreenmodel/TimerConfigurationLab.
                updateUI();                                                                 //update the UI.
                mCallback.onTodoSelected(todoModel);
            }
        });
        updateUI();                                                                         //updated UI.

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;                          //get rid of callback after detach.
    }

    public void updateUI() {
        TodoLab todoLab = TodoLab.get(getActivity());        //get the todoList.
        List<TodoModel> todoConfigurations = todoLab.getTodoList();              //get all the todoItems from database.
        if (mAdapter == null) {                                 //if there no adapter, make one and attach to recyclerview.
            mAdapter = new TodoAdapter(todoConfigurations);
            mTodoRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setTimerConfigurations(todoConfigurations);                         //add the List of timerConfigurations.
            mAdapter.notifyDataSetChanged();                    //notify that there was a change to the adapter.
        }
    }

    //viewholder that only looks at one item on the list at a time to implement what happens with in the item.
    public class TodoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //establish globals with in the item view.
        TodoModel mTodoModel;

        @BindView(R.id.todo_name)
        TextView mTodoNameTextView;

        @BindView(R.id.todo_date)
        TextView mTodoDateTextView;

        @BindView(R.id.todo_completed)
        CheckBox mTodoCompleted;

        @BindView(R.id.todo_image)
        ImageView mTodoImage;

        private TodoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

        }

        //gets the information of the timerConfiguration it receives and put the information to the textviews.
        private void bindTodo(TodoModel todoModel){
            mTodoModel = todoModel;
            mTodoNameTextView.setText(mTodoModel.getmName());
            mTodoDateTextView.setText(mTodoModel.getmDate().toString());
            mTodoCompleted.setChecked(mTodoModel.getmCompleted());
            if(!mTodoModel.getmImage().isEmpty()){
                File imgFile = new File(mTodoModel.getmImage());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mTodoImage.setImageBitmap(myBitmap);
                }else{
                    mTodoModel.setmImage("");
                }
            }
        }

        //do this when the item is clicked.
        @Override
        public void onClick(View v) {
            mCallback.onTodoSelected(mTodoModel);
        }
    }

    //class that implements the views that the recycler view holds.
    private class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {
        private List<TodoModel> mTodoModels;

        private TodoAdapter(List<TodoModel> todoModels) {
            mTodoModels = todoModels;
        }

        @Override
        public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_view_model, parent, false);   //get the layout that governs the item.
            return new TodoHolder(view);                                       //calls timerholder to implement the rules that govern the item.
        }

        //binds the view using the rulles made in TimerAdapter for as many item there are on the list given.
        @Override
        public void onBindViewHolder(TodoHolder holder, int position) {
            TodoModel todoModel = mTodoModels.get(position);
            // holder.mTitleTextView.setText(timerConfiguration.getTitle());
            holder.bindTodo(todoModel);

        }

        @Override
        public int getItemCount() {
            return mTodoModels.size();
        }

        private void setTimerConfigurations(List<TodoModel> todoModels) {
            mTodoModels = todoModels;
        }
    }




}
