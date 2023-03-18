package br.com.profdigital.loginregistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    Button mButtonLogout;
    SharedPreferences mSharedPreferences;

    private void verifyNotLogged(){
        if(mSharedPreferences.getString("logged", "false").equals("false")){
            showLogin();
        }
    }

    private void showLogin(){
        Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void performLogout(){

        String mUrl = "http://192.168.0.13/app-login-register/logout.php";
        //String mUrl = "https://datafication.me/app-login-register/logout.php";
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success")) {
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putString("logged", "");
                    mEditor.putString("user", "");
                    mEditor.putString("email", "");
                    mEditor.putString("apiKey", "");
                    mEditor.apply();
                    showLogin();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
               //Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> mParams = new HashMap<String, String>();
                mParams.put("email", mSharedPreferences.getString("email", ""));
                mParams.put("apiKey", mSharedPreferences.getString("apiKey", ""));
                return mParams;
            }
        };

        //https://stackoverflow.com/questions/55220721/how-to-fix-volley-timeout-error-in-android
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(mStringRequest);

    }

    public class ClickButtonLogout implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            performLogout();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mButtonLogout = findViewById(R.id.button_logout);
        mButtonLogout.setOnClickListener(new ClickButtonLogout());

        mSharedPreferences = getSharedPreferences("MyAppName" ,  MODE_PRIVATE);
        verifyNotLogged();

    }
}
