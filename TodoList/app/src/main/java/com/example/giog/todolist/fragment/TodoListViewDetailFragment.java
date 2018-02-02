package com.example.giog.todolist.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giog.todolist.R;
import com.example.giog.todolist.model.TodoLab;
import com.example.giog.todolist.model.TodoModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wgoris31 on 2/1/2018.
 */

public class TodoListViewDetailFragment extends Fragment {

    private static final String ARG_TIMER_ID = "timer_id";
    private static final int PHOTO_PICKER_ID = 0;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;

    private TodoModel mTodoModel;
    private Callbacks mCallbacks;

    @BindView(R.id.todo_date)
    TextView mTodoDate;

    @BindView(R.id.edit_text_name)
    EditText mNameEdit;

    @BindView(R.id.todo_completed)
    CheckBox mCompletedCheck;

    @BindView(R.id.edit_text_notes)
    EditText mNoteEdit;

    @BindView(R.id.todo_image)
    ImageView mTodoImage;

    @BindView(R.id.todo_take_image)
    Button mTodoImageButton;

    @BindView(R.id.delete_todo_button)
    Button mDeleteButton;
    private String mCurrentPhotoPath;

    public interface Callbacks {
        void onTodoUpdated(TodoModel todoModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    private void updateTodo() {
        TodoLab.get(getActivity()).updateTodo(mTodoModel);
        mCallbacks.onTodoUpdated(mTodoModel);
    }

    private void eraseTodo(){
        TodoLab.get(getActivity()).eraseTimer(mTodoModel);
        mCallbacks.onTodoUpdated(mTodoModel);
    }

    public static TodoListViewDetailFragment newInstance(UUID timerID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIMER_ID, timerID);
        TodoListViewDetailFragment fragment = new TodoListViewDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID todoId = (UUID) getArguments().getSerializable(ARG_TIMER_ID);
        mTodoModel = TodoLab.get(getActivity()).getTodo(todoId);
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_listview_detail, container, false);
        ButterKnife.bind(this, v);

        mTodoDate.setText(mTodoModel.getmDate().toString());

        mNameEdit.setText(mTodoModel.getmName());

        mNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTodoModel.setmName(s.toString());
                updateTodo();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCompletedCheck.setChecked(mTodoModel.getmCompleted());
        mCompletedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTodoModel.setmCompleted(isChecked);
                updateTodo();

            }
        });

        mNoteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTodoModel.setmNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!mTodoModel.getmImage().isEmpty()){
            File file = new File(mTodoModel.getmImage());
            if(file.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                mTodoImage.setImageBitmap(myBitmap);
            }else{
                mTodoModel.setmImage("");
            }
        }

        mTodoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseTodo();
            }
        });

        return v;
    }

    public void dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Upload From");
        dialog.setContentView(R.layout.dialog_pop_up_gallery_camera);

        dialog.setTitle("Select an Option...");
        TextView txt_gallry=(TextView)dialog.findViewById(R.id.textView_gallery);
        TextView txt_camera=(TextView)dialog.findViewById(R.id.textView_camera);

        txt_gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_ID);
            }
        });
        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_IMAGE);
                }

            }
        });
        dialog.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] projection = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    cursor.close();
                    File file = new File(picturePath);
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    mTodoImage.setImageBitmap(myBitmap);
                    mTodoModel.setmImage(file.getAbsolutePath());
                    updateTodo();
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    File mFile = new File(selectedImage.getPath());
                    mTodoModel.setmImage(mFile.getAbsolutePath());
                    mTodoImage.setImageURI(selectedImage);
                    updateTodo();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TodoLab.get(getActivity())
                .updateTodo(mTodoModel);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
