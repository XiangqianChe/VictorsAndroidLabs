package algonquin.cst2335.victorsandroidlabs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView welcome = findViewById(R.id.welcome_message);
        welcome.setText("Welcome back " + emailAddress);

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        Button callBtn = findViewById(R.id.call_btn);
        callBtn.setOnClickListener(clk -> {
            String phoneNumber = editTextPhone.getText().toString();
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivityForResult(callIntent, 5432);
        });

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("PhoneNumber","");
        editTextPhone.setText(phoneNumber);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Button imageButton = findViewById(R.id.image_btn);
        imageButton.setOnClickListener(clk -> {
            startActivityForResult( cameraIntent, 3456);
        });

        File file = new File("/data/user/0/algonquin.cst2335.victorsandroidlabs/files/Picture.png");
        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile("/data/user/0/algonquin.cst2335.victorsandroidlabs/files/Picture.png");
            ImageView myImageView = findViewById(R.id.profile_image);
            myImageView.setImageBitmap( theImage );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profileImage = findViewById(R.id.profile_image);
        Bitmap thumbnail =null;
        if (requestCode == 3456) {
            if (resultCode == RESULT_OK) {
                thumbnail = data.getParcelableExtra("data");
                profileImage.setImageBitmap(thumbnail);

                FileOutputStream fOut;
                try {
                    fOut = openFileOutput( "Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", editTextPhone.getText().toString());
        editor.apply();
    }
}