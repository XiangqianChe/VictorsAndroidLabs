package algonquin.cst2335.victorsandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This is the main activity class of week 6 lab, to learn JavaDocs
 * @author chexq
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen */
    private TextView passwordTextView = null;
    /** This holds the editable text below */
    private EditText passwordEditText = null;
    /** This holds the button at the bottom of the screen */
    private Button loginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordTextView = findViewById(R.id.password_tv);
        passwordEditText = findViewById(R.id.password_et);
        loginButton = findViewById(R.id.login_btn);

        loginButton.setOnClickListener(clk -> {
            String password = passwordEditText.getText().toString();
            if(checkPasswordComplexity(password)) {
                passwordTextView.setText("Your password meets the requirements");
            } else {
                passwordTextView.setText("You shall not pass!");
            }
        });
    }

    /** This function checks if this string has an Upper Case letter, a lower case letter, a number,
     * and a special symbol (#$%^&amp;*!@?).
     * @param pw pw is a String that is being checked
     * @return Return true if the password is complex enough, and false if it is not complex enough
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (char c : pw.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if(!foundUpperCase) {
            Toast.makeText(getApplicationContext(),"Your password does not have an upper case letter.",Toast.LENGTH_SHORT).show();
            return false;
        } else if( ! foundLowerCase) {
            Toast.makeText(getApplicationContext(),"Your password does not have a lower case letter.",Toast.LENGTH_SHORT).show();
            return false;
        } else if( ! foundNumber) {
            Toast.makeText(getApplicationContext(),"Your password does not have a number.",Toast.LENGTH_SHORT).show();
            return false;
        } else if(! foundSpecial) {
            Toast.makeText(getApplicationContext(),"Your password does not have a special character.",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /** This function checks if this character is a special symbol (#$%^&amp;*!@?).
     * @param c is a character that is being checked
     * @return Return true if the character is in (#$%^&amp;*!@?), and false if it is not
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}