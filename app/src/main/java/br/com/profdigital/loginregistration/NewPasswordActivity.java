package br.com.profdigital.loginregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    EditText mEditTextNewPassword, mEditTextOtp;
    Button mButtonSubmitOtp;
    ProgressBar mProgressBarNewPassword;

    String mEmail;

    private void performSubmit(){

        // Toast.makeText(this,"ops falta algo aqui", Toast.LENGTH_SHORT).show();
        // https://developer.android.com/training/volley?hl=pt-br
        // https://www.codeseasy.com/google-volley-android/

        mProgressBarNewPassword.setVisibility(View.VISIBLE);

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        String mUrl = "http://192.168.0.13/app-login-register/new-password.php";

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBarNewPassword.setVisibility(View.GONE);
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "New password set", Toast.LENGTH_SHORT).show();
                            Intent mIntent = new Intent(getApplicationContext() ,  LoginActivity.class);
                            startActivity(mIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"That didn't work!", Toast.LENGTH_SHORT).show();
                mProgressBarNewPassword.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", mEmail);
                paramV.put("otp", mEditTextOtp.getText().toString());
                paramV.put("new-password", mEditTextNewPassword.getText().toString());
                return paramV;
            }
        };

        mQueue.add(mStringRequest);

    }

    public class ClickButtonSubmitNewPassword implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            performSubmit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        mEditTextNewPassword = findViewById(R.id.editText_new_password);
        mEditTextOtp = findViewById(R.id.editText_otp);
        mButtonSubmitOtp = findViewById(R.id.button_submit_otp);
        mButtonSubmitOtp.setOnClickListener(new ClickButtonSubmitNewPassword());

        mProgressBarNewPassword = findViewById(R.id.progressBar_new_password);

        mEmail = getIntent().getExtras().getString("email");

    }
}