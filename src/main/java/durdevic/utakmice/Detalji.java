package durdevic.utakmice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Detalji extends AppCompatActivity {

    private Utakmica utakmica;
    private EditText datum;
    private EditText opis;
    private EditText rezultat;
    private Button nazad, dodaj, promjeni, obrisi;
    private RESTTask restTask;
    private Gson gson;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);

        String datum_n = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date());
        EditText date = (EditText)findViewById(R.id.datum);
        datum = (EditText) findViewById(R.id.datum);

        opis = findViewById(R.id.opis);
        rezultat = findViewById(R.id.rezultat);
        nazad = findViewById(R.id.nazad);
        dodaj = findViewById(R.id.dodaj);
        promjeni = findViewById(R.id.promjeni);
        obrisi = findViewById(R.id.obrisi);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'h:m:ssZ").create();

        Intent i = getIntent();
        boolean novaUtakmica = i.getBooleanExtra("novaUtakmica",false);
        if(!novaUtakmica){
            utakmica = (Utakmica)i.getSerializableExtra("utakmica");
            datum.setText(datum_n);
            opis.setText(utakmica.getOpis());
            rezultat.setText(utakmica.getRezultat());
            dodaj.setVisibility(View.INVISIBLE);
        }else {
            promjeni.setVisibility(View.INVISIBLE);
            obrisi.setVisibility(View.INVISIBLE);
            nazad.setBackgroundColor(R.color.ljubicasta);
        }


        nazad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nazad(true);
            }
        });

        dodaj.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dodaj();
            }
        });

        promjeni.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                promjeni();
            }
        });

        obrisi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                obrisi();
            }
        });

        restTask = new RESTTask();

         gson = new GsonBuilder().create();


    }

    private void obrisi() {
        restTask.execute(getString(R.string.REST_URL) + "/" + utakmica.getSifra(),"DELETE","");
    }

    private void promjeni() {
        utakmica.setDatum(utakmica.getDatum());
        utakmica.setOpis(opis.getText().toString());
        utakmica.setRezultat(rezultat.getText().toString());
        restTask.execute(getString(R.string.REST_URL)+ "/" + utakmica.getSifra(),"PUT",gson.toJson(utakmica));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dodaj() {
        utakmica = new Utakmica();

        Date datumTekme = new Date();
        try {
            datumTekme = new SimpleDateFormat("dd.MM.yyyy.").parse(datum.getText().toString());
        }catch (Exception e){
            //Failed to parse
        }
        utakmica.setDatum(datumTekme);
        utakmica.setOpis(opis.getText().toString());
        utakmica.setRezultat(rezultat.getText().toString());
        restTask.execute(getString(R.string.REST_URL) ,"POST",gson.toJson(utakmica));
    }

    void nazad(boolean ok){
        setResult(ok ? MainActivity.OK : MainActivity.GRESKA, null);
        finish();
    }


    private class RESTTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... parametri) {
            String stringUrl = parametri[0];
            String metoda=parametri[1];
            String json=parametri[2];
            Log.d("json", json);
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod(metoda);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches (false);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(json);
                writer.close();
                wr.close();

                InputStream is = connection.getInputStream();

                is = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    Log.d("--->",inputLine);
                }
                br.close();
                is.close();



               return metoda.equals("POST") && connection.getResponseCode()==201 ? true : connection.getResponseCode()==200 ? true : false;




            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Boolean ok) {

                nazad(ok);

        }
    }

}
