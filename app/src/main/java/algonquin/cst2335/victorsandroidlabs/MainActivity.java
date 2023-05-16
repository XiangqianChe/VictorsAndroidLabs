package algonquin.cst2335.victorsandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mytext = findViewById(R.id.textview);
        EditText myedit = findViewById(R.id.myedittext);
        Button mybutton = findViewById(R.id.mybutton);

        mybutton.setOnClickListener(vw -> mytext.setText("Your edit text has: " + myedit.getText().toString()));

        CheckBox mycheckbox = findViewById(R.id.mycheckbox);
        Switch myswitch = findViewById(R.id.myswitch);
        RadioButton myradio = findViewById(R.id.myradio);

        mycheckbox.setOnCheckedChangeListener(
                (btn, isChecked) -> Toast.makeText(
                        getApplicationContext(),"You clicked on the " + btn.getText().toString() + " and it is now: " + isChecked, Toast.LENGTH_LONG).show());
        myswitch.setOnCheckedChangeListener(
                (btn, isChecked) -> Toast.makeText(
                        getApplicationContext(),"You clicked on the " + btn.getText().toString() + " and it is now: " + isChecked, Toast.LENGTH_SHORT).show());
        myradio.setOnCheckedChangeListener(
                (btn, isChecked) -> Toast.makeText(
                        getApplicationContext(),"You clicked on the " + btn.getText().toString() + " and it is now: " + isChecked, Toast.LENGTH_SHORT).show());

        ImageView myimage = findViewById(R.id.logo_algonquin);
        ImageButton imgbtn = findViewById(R.id.myimagebutton);

        imgbtn.setOnClickListener(vw -> Toast.makeText(getApplicationContext(), "The width = " + vw.getWidth() + " and height = " + vw.getHeight(), Toast.LENGTH_LONG).show());
    }
}