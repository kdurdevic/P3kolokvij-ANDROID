package durdevic.utakmice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Utakmica implements Serializable {

    private int sifra;
    private Date datum;
    private String opis;
    private String rezultat;

    public Utakmica(){

    }

    public Utakmica(int sifra, Date datum, String opis, String rezultat) {
        this.sifra = sifra;
        this.datum = datum;
        this.opis = opis;
        this.rezultat = rezultat;
    }

    public int getSifra() {
        return sifra;
    }

    public void setSifra(int sifra) {
        this.sifra = sifra;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getRezultat() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat = rezultat;
    }
}
