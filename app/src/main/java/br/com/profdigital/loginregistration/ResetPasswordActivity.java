package br.com.profdigital.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView mTextViewAbandonReset;
    Button mButtonSubmit;
    EditText mEditTextEmailReset;
    ProgressBar mProgressBarReset;
    String mEmail;

    private void performAbandonReset(){
        Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    public class ClickAbandonReset implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            performAbandonReset();
        }
    }


    private boolean isValidEmail(String mEmail){
        if(mEmail == null || mEmail.isEmpty()){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(mEmail).matches();
    }

    private void performSubmit(){

        mEmail = String.valueOf(mEditTextEmailReset.getText());

        if(!isValidEmail(mEmail)){
            String mTextMessage = getString(R.string.text_email_not_valid);
            Toast.makeText(this, mTextMessage, Toast.LENGTH_SHORT).show();
            return;
        }


        // Toast.makeText(this,"ops falta algo aqui", Toast.LENGTH_SHORT).show();
        // https://developer.android.com/training/volley?hl=pt-br
        // https://www.codeseasy.com/google-volley-android/

        mProgressBarReset.setVisibility(View.VISIBLE);

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        String mUrl = "http://192.168.0.13/app-login-register/reset-password.php";

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBarReset.setVisibility(View.GONE);
                        if(response.equals("success")){
                            Intent mIntent = new Intent(getApplicationContext() ,  NewPasswordActivity.class);
                            mIntent.putExtra("email", mEditTextEmailReset.getText().toString());
                            startActivity(mIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"That didn't work!", Toast.LENGTH_SHORT).show();
                mProgressBarReset.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", mEditTextEmailReset.getText().toString());
                return paramV;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mQueue.add(mStringRequest);

        // https://www.youtube.com/watch?v=50XweIg_-YU&t=425s
        // 50:00
    }



    public class ClickButtonSubmit implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            performSubmit();
        }
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mTextViewAbandonReset = findViewById(R.id.textView_abandon_reset);
        mTextViewAbandonReset.setOnClickListener(new ClickAbandonReset());

        mButtonSubmit = findViewById(R.id.button_submit_reset_password);
        mButtonSubmit.setOnClickListener(new ClickButtonSubmit());

        mEditTextEmailReset = findViewById(R.id.editText_email_reset);

        mProgressBarReset = findViewById(R.id.progressBar_reset_password);


    }
}
