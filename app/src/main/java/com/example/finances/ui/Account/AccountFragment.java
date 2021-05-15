package com.example.finances.ui.Account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finances.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

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
    public String FilePath ="";
    public ProgressBar simpleProgressBar;
    public TextView progressText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        ImageButton PhotoButton = view.findViewById(R.id.FirstPhotoButton);


        simpleProgressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.progressText);


        //Получаем activity
        activityAccount = getActivity();

        //получаем путь к изображению
        SharedPreferences accountPhoto = getActivity().getSharedPreferences(APP_PREFERENCES_Path, Context.MODE_PRIVATE);
        FilePath= accountPhoto.getString("key1", "");

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
        Bitmap bitmap = null;
        File ff = new File(FilePath);
        if(ff.isFile()) {
            try{
                ImageView profileImage =  view.findViewById(R.id.ProfileImage);
                Context context = getContext();
                Picasso.with(context)
                        .load(new File(FilePath))
                        .transform(new CropSquareTransformation())
                        .into(profileImage);
                }
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
    public void onStart() {
        super.onStart();
        simpleProgressBar.setVisibility(View.INVISIBLE);
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            View a = getView();
            ImageView profileImage = (ImageView) a.findViewById(R.id.ProfileImage);
            Uri selectedImageUri = data.getData();
            Context c = getContext();
            Bitmap bitmap;
            //Сохраняем изображение в файл
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                new fileFromBitmap("ProfileFoto", bitmap, c).execute();
                //устанавливаем изображение

                Picasso.with(c)
                        .load(selectedImageUri)
                        .transform(new CropSquareTransformation())
                        .into(profileImage);

            } catch (IOException e) {
                e.printStackTrace();
            }



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






    File f;
    public class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;
        String fileNameToSave;

        public fileFromBitmap(String fileNameToSave,Bitmap bitmap,Context context) {
            this.bitmap = bitmap;
            this.context= context;
            this.fileNameToSave = fileNameToSave;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before executing doInBackground
            // update your UI
            // exp; make progressbar visible
        }



        @Override
        protected String doInBackground(Void... params) {

            f = new File(context.getFilesDir(), fileNameToSave);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //text = view.findViewById(R.id.progressText);

            try {
                //Convert bitmap to byte array
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,25 , bos); // YOU can also save it in JPEG
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                FilePath = f.getPath();
                profile = getActivity().getSharedPreferences(APP_PREFERENCES_Path,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = profile.edit();
                editor.putString("key1", String.valueOf(FilePath)).apply();
            }catch (Exception e){
                e.printStackTrace();
            }

            for (int i = 0; i <= 100; i += 1) {
                try {
                    Thread.sleep(10);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            publishProgress(100);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // back to main thread after finishing doInBackground
            // update your UI or take action after
            // exp; make progressbar gone


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simpleProgressBar.setVisibility(View.INVISIBLE);
            progressText.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressText.setVisibility(View.VISIBLE);
            simpleProgressBar.setVisibility(View.VISIBLE);
          simpleProgressBar.setProgress(values[0]);
            progressText.setText("Выполнено : " + values[0] + "/100");

        }
    }
}