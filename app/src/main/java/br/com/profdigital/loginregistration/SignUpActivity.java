package br.com.profdigital.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditTextFullName;
    private EditText mEditTextEmail;
    private EditText mEditTextUserName;
    private EditText mEditTextPasswordSignUp;
    private Button mButtonSignUp;
    private TextView mTextViewAlreadyLogin;
    private ProgressBar mProgressBar;

    String mName, mEmail, mPassword, mFullName;

    private boolean isRequired(){
        if(
                TextUtils.isEmpty(mEditTextFullName.getText()) ||
                        TextUtils.isEmpty(mEditTextEmail.getText()) ||
                        TextUtils.isEmpty(mEditTextUserName.getText()) ||
                        TextUtils.isEmpty(mEditTextPasswordSignUp.getText())
        ) {
            return true;
        }
        else {
            return false;
        }
    }

    private void performActivityLogin(){
        Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void postDataUsingVolley(){
        if(isRequired()){
            //Toast.makeText(this, "Mandatory information", Toast.LENGTH_SHORT).show();
            String mTextMessage = getString(R.string.text_error_all_fields_required);
            Toast.makeText(this, mTextMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        mName = String.valueOf(mEditTextUserName.getText());
        mEmail = String.valueOf(mEditTextEmail.getText());
        mPassword = String.valueOf(mEditTextPasswordSignUp.getText());
        mFullName = String.valueOf(mEditTextFullName.getText());

        mProgressBar.setVisibility(View.VISIBLE);
        // url to post our data - use IPCONFIG para obter ip 192...

        String mUrl = "http://192.168.0.13/app-login-register/register.php";
        //String mUrl = "https://datafication.me/app-login-register/register.php";
        // creating a new object for our request queue => (I). a line or sequence of people or vehicles awaiting their turn to be attended to or to proceed. line, row, column, file, chain, string, stream, traffic jam, jam, tailback stream, gridlock (II). a list of data items, commands, etc., stored so as to be retrievable in a definite order, usually the order of insertion.
        RequestQueue mQueue = Volley.newRequestQueue(this);
        // https://www.geeksforgeeks.org/how-to-post-data-to-api-using-volley-in-android/
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressBar.setVisibility(View.GONE);
                if(response.equals("success")){
                    Toast.makeText(getApplicationContext(), "Registrations successful", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                mProgressBar.setVisibility(View.GONE);
                error.printStackTrace();
                //Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                 //Toast.makeText(getApplicationContext(),  error.toString(), Toast.LENGTH_SHORT).show();


             /*  https://www.programcreek.com/java-api-examples/?api=com.android.volley.ServerError
                https://androidclarified.wordpress.com/category/volley/
                https://androidclarified.wordpress.com/2017/07/20/android-volley-example-with-error-handling/#more-704
This logic is OK!!
                String mMessageError = "Impossible detect error!";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    mMessageError = "This indicates that the request has either time out or there is no connection";
                } else if (error instanceof AuthFailureError) {
                    mMessageError = "Error indicating that there was an Authentication Failure while performing the request";

                } else if (error instanceof ServerError) {
                    mMessageError = "Indicates that the server responded with a error response";

                } else if (error instanceof NetworkError) {
                    mMessageError = "Indicates that there was network error while performing the request";

                } else if (error instanceof ParseError) {
                    mMessageError = "Indicates that the server response could not be parsed";

                }
                Toast.makeText(getApplicationContext(), mMessageError, Toast.LENGTH_LONG).show();*/

            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> mParams = new HashMap<String, String>();
                // on below line we are passing our key
                // and value pair to our parameters.
                mParams.put("fullname", mFullName);
                mParams.put("username", mName);
                mParams.put("password", mPassword);
                mParams.put("email", mEmail);
                // below line is to make
                // a json object request.
                return mParams;
            }
        };


        //https://stackoverflow.com/questions/55220721/how-to-fix-volley-timeout-error-in-android
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        // below line is to make
        // a json object request.
        mQueue.add(mStringRequest);
    }

    public class ClickButtonSignUp implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            postDataUsingVolley();
        }
    }

    public class ClickTextViewAlreadyLogin implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            performActivityLogin();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mEditTextEmail = findViewById(R.id.editText_email);
        mEditTextUserName = findViewById(R.id.editText_user_name);
        mEditTextFullName = findViewById(R.id.editText_full_name);
        mEditTextPasswordSignUp = findViewById(R.id.editText_password_sign_up);

        mTextViewAlreadyLogin = findViewById(R.id.textView_already);
        mTextViewAlreadyLogin.setOnClickListener(new ClickTextViewAlreadyLogin());

        mProgressBar = findViewById(R.id.progressBarSignUp);

        mButtonSignUp = findViewById(R.id.button_sign_up);
        mButtonSignUp.setOnClickListener(new ClickButtonSignUp());

    }
}
