
package com.example.finances.ui.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.finances.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ResourceBundle;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    public static final String APP_PREFERENCES_Path = "Nickname" ;
    private final int GALLERY_REQUEST = 1;
    public SharedPreferences profile;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ImageButton PhotoButton = view.findViewById(R.id.FirstPhotoButton);

        //устанавливаем никнейм
        SharedPreferences accNickname = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String nickname = accNickname.getString("Nickname","") ;
        TextView Nickname = view.findViewById(R.id.name);
        Nickname.setText(nickname);
        if(nickname.isEmpty()){
            Nickname.setText("Nickname");
        }
        //устанавливаем email
        SharedPreferences accEmail = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String email = accNickname.getString("E-mail","") ;
        TextView Email = view.findViewById(R.id.email);
        Email.setText(email);
        if(email.isEmpty()){
           Email.setText("Not indicated");
        }

        //устанавливаем место учебы
        SharedPreferences eduPlace = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String EduPLace = accNickname.getString("Educational institution","") ;
        TextView eduplace = view.findViewById(R.id.eduInstitution);
        eduplace.setText(EduPLace);
        if(EduPLace.isEmpty()){
            eduplace.setText("Not indicated");
        }







        //устанавливаю строку из SharedPreferences
      //  TextView check = view.findViewById(R.id.tryText);
        SharedPreferences accountPhoto = getActivity().getSharedPreferences(APP_PREFERENCES_Path, Context.MODE_PRIVATE);
        String s = accountPhoto.getString("key1", "");

        if(!s.trim().isEmpty() && s != null) {
            try{



            //check.setText(s);
            CircleImageView profileImage =  view.findViewById(R.id.imageView); //находим изображение на layout
            Uri imageUri = Uri.parse(s); //преобазую строку в адрес
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                profileImage.setImageBitmap(bitmap); // устанавливаем изображение
            }}
            catch (Exception E){

            }
        }


        //кнопка выбора изображения
        PhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        View a = getView();
        CircleImageView profileImage = (CircleImageView) a.findViewById(R.id.imageView);
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        // Теперь сохраняем ссылку на файл
                        profile = getActivity().getSharedPreferences(APP_PREFERENCES_Path,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = profile.edit();
                        editor.putString("key1", String.valueOf(selectedImage)).apply();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profileImage.setImageBitmap(bitmap);
                }
        }

    }



}