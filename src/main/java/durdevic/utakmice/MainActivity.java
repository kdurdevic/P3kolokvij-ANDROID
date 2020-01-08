package durdevic.utakmice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterListe.ItemClickListener {

    private AdapterListe adapter;
    private RESTTask ayncTask;
    public static final int DETALJI=1;
    public static final int OK=2;
    public static final int GRESKA=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterListe(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ayncTask = new RESTTask();
        ayncTask.execute( getString(R.string.REST_URL));

        Button novo = findViewById(R.id.novo);
        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novaUtakmica();
            }
        });
    }

    private void novaUtakmica() {

        Intent i = new Intent(this, Detalji.class);
        i.putExtra("novaUtakmica", true);
        startActivityForResult(i,DETALJI);
    }

    @Override
    public void onItemClick(View view, int position) {
        Utakmica o = adapter.getItem(position);
        Intent i = new Intent(this, Detalji.class);
        i.putExtra("utakmica", o);
        startActivityForResult(i,DETALJI);
        //Toast.makeText(this, "Odabrali ste Utakmicu s šifrom " + o.getSifra(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case OK:
                ayncTask = new RESTTask();
                ayncTask.execute( getString(R.string.REST_URL));
                break;
            case GRESKA:
                Toast.makeText(this, "Dogodila se pogreška, akcija nije izvedena", Toast.LENGTH_SHORT).show();
                break;
        }
        if(resultCode==OK){

        }



    }


    //Zadatak
    //implementirati različite animacije između dviju aktivnosti
    //https://stackoverflow.com/questions/10243557/how-to-apply-slide-animation-between-two-activities-in-android


    // kao inner klasa, čitati kako napraviti u odvojenoj datoteci: https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
    private class RESTTask extends AsyncTask<String, Void, List<Utakmica>> {
        protected List<Utakmica> doInBackground(String... adresa) {
            String stringUrl = adresa[0];
            List<Utakmica> vrati=null;
            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                Type listType = new TypeToken<ArrayList<Utakmica>>(){}.getType();
                vrati = new Gson().fromJson(reader, listType);
                reader.close();
                streamReader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return vrati;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(List<Utakmica> podaci) {
            adapter.setPodaci(podaci);
            adapter.notifyDataSetChanged();
        }
    }

}
