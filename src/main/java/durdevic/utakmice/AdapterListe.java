package durdevic.utakmice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterListe extends RecyclerView.Adapter<AdapterListe.Red> {

    private List<Utakmica> podaci;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // Podatke proslijedimo kroz konstruktor
    public AdapterListe(Context context) {
        this.mInflater = LayoutInflater.from(context);
        podaci = new ArrayList<>();
    }

    // napuni predložak red (datoteka red_liste.xml)
    @Override
    public Red onCreateViewHolder(ViewGroup roditelj, int viewType) {
        View view = mInflater.inflate(R.layout.red_liste, roditelj, false);
        return new Red(view);
    }

    // Veže podatke za svaki red
    @Override
    public void onBindViewHolder(Red red, int position) {
        Utakmica o = podaci.get(position);
        String datum_n = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date());
        red.datum.setText(datum_n);
        red.rezultat.setText(o.getRezultat());
    }

    // Ukupan broj redova (mora biti implementirano)
    @Override
    public int getItemCount() {
        return podaci==null ? 0 : podaci.size();
    }


    // Pohranjuje i reciklira pogled kako se prolazi kroz listu
    public class Red extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView datum;
        private TextView rezultat;

        Red(View itemView) {
            super(itemView);
            datum = itemView.findViewById(R.id.datum);
            rezultat = itemView.findViewById(R.id.rezultat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // klikom na listu dobijemo samo poziciju koju stavku liste smo odabrali.
    // Ova metoda pomaže da na osnovu pozicije dobijemo cijeli objekt u toj stavci
    public Utakmica getItem(int id) {
        return podaci.get(id);
    }

    public void setPodaci(List<Utakmica> itemList) {
        this.podaci = itemList;
    }

    // dopusti hvatanje odabira (klik/dotakni)
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // potrebno kako bi mogli hvatati klikove/dodire
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
