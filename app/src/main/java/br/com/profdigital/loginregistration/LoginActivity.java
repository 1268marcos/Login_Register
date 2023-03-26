package br.com.profdigital.loginregistration;

/*
* Os códigos apresentados são um exemplo de uma atividade de login para uma aplicação Android. Eles implementam funcionalidades básicas, como a obtenção de dados do usuário a partir de campos de edição de texto, a autenticação com uma API externa usando o Volley e a armazenagem de informações de autenticação em SharedPreferences. A lógica de programação e a estrutura geral do código são claras e estão de acordo com as boas práticas de desenvolvimento Android. No entanto, é sempre recomendável revisar o código para garantir que ele esteja seguro e eficiente.
* */

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

   TextView mTextViewNewUser, mTextViewForgotPassword;
   Button mButtonLogin;
    EditText mEditTextEmail, mEditTextPassword;
    ProgressBar mProgressBar;
    String mUser, mPassword, mEmail, mApiKey, mIpV4;
    SharedPreferences mSharedPreferences;

    /* chat.openai.com/chat
    * Esta é uma porção de código Java que define algumas variáveis usadas em uma atividade Android. As variáveis incluem uma TextView para mostrar texto na tela, um Button para fazer login, dois EditText para inserir email e senha do usuário, uma ProgressBar para mostrar o progresso de uma tarefa e algumas variáveis de string para armazenar informações do usuário, como email, senha e uma chave API. Também há uma variável SharedPreferences que é usada para armazenar informações de preferência compartilhadas no aplicativo.
    * */

    private boolean isRequired(){
//        if(TextUtils.isEmpty(mEditTextEmail.getText()) || TextUtils.isEmpty(mEditTextPassword.getText())) {
//            return true;
//        }
//        else {
//            return false;
//        }
        return  TextUtils.isEmpty(mEditTextPassword.getText());
    }

    /*
    * Este é um método em Java que verifica se o email e a senha inseridos pelo usuário estão vazios. Ele usa a classe TextUtils do pacote android.text para verificar se os textos dos objetos mEditTextEmail e mEditTextPassword são vazios. Se ambos os campos estiverem vazios, o método retorna true, indicando que é necessário inserir um email e uma senha. Caso contrário, o método retorna false, indicando que ambos os campos foram preenchidos.
    * */

    private boolean isValidEmail(String mEmail){
        if(mEmail == null || mEmail.isEmpty()){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(mEmail).matches();
    }


    private void showOrder(){
        Intent mIntent = new Intent(getApplicationContext(), OrderActivity.class);
        startActivity(mIntent);
        finish();
    }

    /*
    *Este é um método em Java que inicia uma nova atividade e exibe a tela de pedidos. Ele cria uma nova instância de Intent e especifica a atividade OrderActivity como a atividade a ser iniciada. Em seguida, o método usa o método startActivity para iniciar a nova atividade e o método finish para encerrar a atividade atual.

Este método é provavelmente chamado pelo método verifyLogged quando o usuário já está logado e, portanto, já pode ver a tela de pedidos.
    *
    * */


    private void verifyLogged(){
        if(mSharedPreferences.getString("logged", "false").equals("true")){
            showOrder();
        }
    }


    /*
    * Este é um método em Java que verifica se o usuário já está logado. Ele pega o valor do SharedPreferences armazenado com a chave "logged" e verifica se ele é igual a "true". Se for, o método showOrder() é chamado. Caso contrário, o usuário ainda não está logado e o método não fará nada.

Este método pode ser usado para verificar se o usuário já fez login em uma sessão anterior e, se sim, redirecioná-lo diretamente para a tela de pedidos.
    * */


    private void getIpV4(){
        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int vIp = mWifiInfo.getIpAddress();
        //String ipAddress = Formatter.formatIpAddress(ip);
        mIpV4 = Formatter.formatIpAddress(vIp);
    }

    /*
         Define um método chamado getIpV4
         Cria um objeto WifiManager para acessar informações sobre a rede Wi-Fi do dispositivo
         Obtém as informações da conexão Wi-Fi atual
         Obtém o endereço IP da conexão Wi-Fi atual como um valor inteiro
         Converte o endereço IP inteiro em uma string no formato IPv4 e atribui ao atributo mIpV4

        Essa função utiliza a classe WifiManager para obter informações sobre a conexão Wi-Fi atual do dispositivo. Em seguida, ela usa a classe WifiInfo para obter o endereço IP como um valor inteiro e a classe Formatter para converter esse valor inteiro em uma string no formato IPv4. Finalmente, a string convertida é atribuída à variável mIpV4.
     */



   private void postDataUsingVolley(){

       /*
        * Este é um trecho de código em Java que obtém o email e a senha inseridos pelo usuário. Ele usa o método String.valueOf para converter o objeto retornado por mEditTextEmail.getText() e mEditTextPassword.getText() em strings. Em seguida, as strings são armazenadas nas variáveis mEmail e mPassword. Por fim, o trecho de código faz a barra de progresso visível, definindo a visibilidade do objeto mProgressBar como View.VISIBLE. Isso indica que alguma operação está sendo realizada no momento e que a barra de progresso está animada para indicar ao usuário que ele deve esperar.
        * */

       mEmail = String.valueOf(mEditTextEmail.getText());
       mPassword = String.valueOf(mEditTextPassword.getText());

       if(!isValidEmail(mEmail)){
           String mTextMessage = getString(R.string.text_email_not_valid);
           Toast.makeText(this, mTextMessage, Toast.LENGTH_SHORT).show();
           return;
       }

       if(isRequired()){
           //Toast.makeText(this, "Mandatory information", Toast.LENGTH_SHORT).show();
           String mTextMessage = getString(R.string.text_error_fill_mandatory_information);
           Toast.makeText(this, mTextMessage, Toast.LENGTH_SHORT).show();
           return;
       }


       /*
        * Este é um trecho de código em Java que faz a barra de progresso visível, definindo a visibilidade do objeto mProgressBar como View.VISIBLE. Isso indica que alguma operação está sendo realizada no momento e que a barra de progresso está animada para indicar ao usuário que ele deve esperar.
        * */

       mProgressBar.setVisibility(View.VISIBLE);


//       Context context = requireContext().getApplicationContext();
//       WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//       String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//   https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code


//       String mUrl = "http://192.168.0.13/app-login-register/login.php";
        String mUrl = "http://"+mIpV4+"/app-login-register/login.php";
      // String mUrl = "https://datafication.me/app-login-register/login.php";

       /*
       * Este é uma linha de código em Java que declara uma variável mUrl que armazena uma string representando uma URL. A URL aponta para um script de login em PHP no servidor https://datafication.me/app-login-register/login.php. É provável que o aplicativo faça uma solicitação HTTP para esse script com as credenciais do usuário (email e senha) e o script retorne uma resposta indicando se as credenciais são válidas ou não. Em seguida, o aplicativo pode tomar uma ação adequada, como exibir uma mensagem de erro ou redirecionar o usuário para outra tela.
       * */

       RequestQueue mQueue = Volley.newRequestQueue(this);
       /*
        * Este é uma linha de código em Java que instancia um objeto RequestQueue usando a biblioteca Volley. A biblioteca Volley é usada para fazer solicitações HTTP de maneira fácil e eficiente em aplicativos Android. A variável mQueue representa a fila de solicitações HTTP que serão executadas pelo aplicativo. O método Volley.newRequestQueue é usado para criar uma nova fila de solicitações HTTP, passando o contexto atual do aplicativo (this) como parâmetro. Isso permite que a fila de solicitações seja associada ao aplicativo atual.
        * */

       StringRequest mStringRequest = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               mProgressBar.setVisibility(View.GONE);
              // https://www.geeksforgeeks.org/how-to-post-data-to-api-using-volley-in-android/
               try {
                   // on below line we are parsing the response to json object to extract data from it.
                   JSONObject mJsonObject = new JSONObject(response);
                   // below are the strings which we extract from our json object.
                   // String name = mJsonObject.getString("name");   String job = mJsonObject.getString("job");
                   String mStatus = mJsonObject.getString("status");
                   String mMessage = mJsonObject.getString("message");
                   if(mStatus.equals("success")){
                       mUser = mJsonObject.getString("user");
                       mEmail = mJsonObject.getString("email");
                       mApiKey = mJsonObject.getString("apiKey");
                       SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                       mEditor.putString("logged", "true");
                       mEditor.putString("user", mUser);
                       mEditor.putString("email", mEmail);
                       mEditor.putString("apiKey", mApiKey);
                       mEditor.apply();
                      showOrder();
                   } else {
                       Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_LONG).show();
                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               // method to handle errors.
               mProgressBar.setVisibility(View.GONE);
               error.printStackTrace();
               Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
               //Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
           }
       }) {
           @Override
           protected Map<String, String> getParams(){
               // below line we are creating a map for
               // storing our values in key and value pair.
               Map<String, String> mParams = new HashMap<String, String>();
               // on below line we are passing our key
               // and value pair to our parameters.
               mParams.put("email", mEmail);
               mParams.put("password", mPassword);
               // below line is to make
               // a json object request.
               return mParams;
           }
       };


       //https://stackoverflow.com/questions/55220721/how-to-fix-volley-timeout-error-in-android
       mStringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       //ver isso é gratis
       //https://www.mongodb.com/pricing

       //somee leomar


       // below line is to make
       // a json object request.
       mQueue.add(mStringRequest);

   }

   /*
   *O código acima está fazendo uma solicitação Volley POST para a API especificada no URL mUrl. O objeto StringRequest usa 3 argumentos, o tipo de método HTTP (POST), a URL (mUrl) e um ouvinte de resposta. O ouvinte de resposta tem um método onResponse que é chamado quando uma resposta é recebida da API.

A resposta está na forma de uma string JSON e está sendo analisada em um JSONObject para extrair os valores de status, mensagem, usuário, email e apiKey. Se o status recebido for "sucesso", o código salva esses valores em SharedPreferences e chama o método showOrder.

O StringRequest também possui um ouvinte de erro com um método onErrorResponse que é chamado quando ocorre um erro durante a chamada da API. Esse método registra o erro e exibe uma mensagem do sistema para o usuário.

O método getParams é substituído para incluir os parâmetros de e-mail e senha na solicitação. Esses valores são obtidos dos textos de edição mEditTextEmail e mEditTextPassword.

Por fim, o mStringRequest é adicionado à fila de solicitações do Volley para ser executado.
   *
   * */


   public class ClickButtonLogin implements View.OnClickListener{
       @Override
       public void onClick(View view) {
           postDataUsingVolley();
       }
   }

   /*
   * Este é um trecho de código em Java que define uma classe anônima chamada ClickButtonLogin, que implementa a interface View.OnClickListener. A classe é usada como um ouvinte de cliques para o botão de login na interface do usuário. Quando o botão é clicado, o método onClick é chamado, que por sua vez chama o método postDataUsingVolley. Este método provavelmente faz a solicitação HTTP para o script de login descrito na URL, enviando as credenciais do usuário (email e senha) e processando a resposta para verificar se as credenciais são válidas ou não.
   * */

   private void showSignUp(){
       Intent mIntent = new Intent(getApplicationContext(), SignUpActivity.class);
       startActivity(mIntent);
       finish();
   }

   /*
   * Este é um trecho de código em Java que define o método showSignUp. Este método é usado para iniciar uma nova atividade chamada SignUpActivity. A atividade é iniciada usando uma instância da classe Intent, que é configurada com o contexto da aplicação atual (getApplicationContext) e a classe da atividade que será iniciada (SignUpActivity.class). Em seguida, o método startActivity é chamado com a intenção para iniciar a nova atividade. Por fim, o método finish é chamado para encerrar a atividade atual. Portanto, ao chamar o método showSignUp, o usuário é redirecionado para a tela de registro do aplicativo.
   * */

   public class ClickNewUser implements View.OnClickListener{
       @Override
       public void onClick(View view) {
            showSignUp();
       }
   }


   private void showForgotPassword(){
//       Toast.makeText(this, "Ops... algo ainda não concluido", Toast.LENGTH_SHORT).show();
       Intent mIntent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
       startActivity(mIntent);
       finish();
   }

    public class ClickForgotPassword implements  View.OnClickListener{
        @Override
        public void onClick(View view) {
            showForgotPassword();
        }
    }


   /*
   *Este é um trecho de código em Java que define uma classe anônima chamada ClickNewUser, que implementa a interface View.OnClickListener. A classe é usada como um ouvinte de cliques para o botão "Novo usuário" na interface do usuário. Quando o botão é clicado, o método onClick é chamado, que por sua vez chama o método showSignUp. Este método provavelmente inicia uma nova atividade para o registro de novos usuários no aplicativo.
   *
   * */

    public class EditTextAction implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                postDataUsingVolley();
            }
            return false;
        }
    }

    /*
    *Este é um trecho de código em Java que define uma classe anônima chamada EditTextAction, que implementa a interface TextView.OnEditorActionListener. A classe é usada como um ouvinte de ação de edição para o EditText na interface do usuário. Quando o usuário pressiona o botão "Done" ou alguma outra ação é executada no teclado virtual, o método onEditorAction é chamado. O método verifica se a ação é EditorInfo.IME_ACTION_DONE, e se for, chama o método postDataUsingVolley. Portanto, esse código permite que o usuário envie os dados digitados no EditText ao clicar no botão "Done".
    * */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mTextViewNewUser = findViewById(R.id.textView_new_user);
        mTextViewNewUser.setOnClickListener(new ClickNewUser());

        mTextViewForgotPassword = findViewById(R.id.textView_forgot_password);
        mTextViewForgotPassword.setOnClickListener(new ClickForgotPassword());

        mButtonLogin = findViewById(R.id.button_log_in);
        mButtonLogin.setOnClickListener(new ClickButtonLogin());

        mEditTextEmail = findViewById(R.id.editText_email_login);
        mEditTextPassword = findViewById(R.id.editText_password_login);
        mEditTextPassword.setOnEditorActionListener(new EditTextAction());

        mProgressBar = findViewById(R.id.progressBarLogin);

        mSharedPreferences = getSharedPreferences("MyAppName" , MODE_PRIVATE);

        getIpV4();

        verifyLogged();

    }


    /*
    * Esse código define a activity de login em uma aplicação Android. A activity inclui uma TextView para mostrar a opção de cadastro para novos usuários, um botão de login, dois EditText para inserir email e senha, uma ProgressBar para indicar a operação de login em andamento, algumas variáveis para armazenar informações de usuário e senha, e um SharedPreferences para salvar o estado de login do usuário.

A activity verifica se os campos de email e senha foram preenchidos antes de prosseguir com o login. Se estiverem vazios, exibe uma mensagem Toast informando que informações obrigatórias não foram preenchidas.

A activity também verifica se o usuário já está logado antes de exibir a tela de login, caso contrário, o usuário é direcionado para a tela de pedido.

A activity define também listeners para o botão de login e para o texto de novo usuário, que direcionam para a tela de login e tela de cadastro, respectivamente. Além disso, define um listener para a edição da senha, que permite o usuário fazer login ao pressionar "Done" na tecla virtual.

Ao clicar no botão de login, a função postDataUsingVolley() é chamada para fazer a requisição de login a uma API específica "https://datafication.me/app-login-register/login.php". O método onCreate() é chamado ao iniciar a activity e define o layout da tela, inicializa os componentes da tela, verifica o estado de login e esconde o teclado virtual.
    *
    * */

}
