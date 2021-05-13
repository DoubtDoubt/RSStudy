package com.example.finances.ui.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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
import androidx.loader.content.CursorLoader;
import androidx.preference.PreferenceManager;

import com.example.finances.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URI;
import java.util.ResourceBundle;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    public static final String APP_PREFERENCES_Path = "Nickname" ;
    private final int GALLERY_REQUEST = 1;
    public SharedPreferences profile;
    private String imagePath;
    private final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private Activity activityAccount;
    public ByteArrayOutputStream bos;
    public final String FilePath = "/data/data/com.example.finances/files/ProfileFoto";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        ImageButton PhotoButton = view.findViewById(R.id.FirstPhotoButton);


        activityAccount = getActivity();

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
        Bitmap bitmap = null;
        File f = new File(FilePath);
        if(f.isFile()) {
    try{
    CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.ProfileImage);
    profileImage.setImageBitmap( loadPicture("/data/data/com.example.finances/files/ProfileFoto", bitmap));}
    catch (Exception e){
        e.printStackTrace();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            View a = getView();
            CircleImageView profileImage = (CircleImageView) a.findViewById(R.id.ProfileImage);
            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromURI(selectedImageUri);
            Context c = getContext();
            Bitmap bitmap;
            //Сохраняем изображение в файл
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                savePicture("ProfileFoto", bitmap, c);
                //устанавливаем изображение
                profileImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void savePicture(String fileNameToSave,Bitmap bitmap,Context context ) { // File name like "image.png"

        //create a file to write bitmap data
        File f = new File(context.getFilesDir(), fileNameToSave);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar saveFileSnackbar  = Snackbar.make(view,"Error: photo saving failed", BaseTransientBottomBar.LENGTH_SHORT);
            saveFileSnackbar.show();
        }

        try {
//Convert bitmap to byte array
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 1 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
           fos.write(bitmapdata);
           fos.flush();
           fos.close();

            Snackbar saveFileSnackbar  = Snackbar.make(view,"Photo is saved", BaseTransientBottomBar.LENGTH_SHORT);
            saveFileSnackbar.show();
        }catch (Exception e){
            e.printStackTrace();
            Snackbar saveFileSnackbar  = Snackbar.make(view,"Error: photo saving failed", BaseTransientBottomBar.LENGTH_SHORT);
            saveFileSnackbar.show();
        }
    }




    private Bitmap loadPicture(String filepath, Bitmap b) {
        // Drawable myImage = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            b = BitmapFactory.decodeFile(filepath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b ;
 }
}