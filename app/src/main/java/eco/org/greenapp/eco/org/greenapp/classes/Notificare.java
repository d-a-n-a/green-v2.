package eco.org.greenapp.eco.org.greenapp.classes;

import java.io.StringBufferInputStream;
import java.util.Date;

import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.enumerations.TipNotificare;

/**
 * Created by danan on 5/2/2018.
 */

public class Notificare {
    private int id;
    private String descriere;
    private Date data;
    private TipNotificare tip;
    private boolean citit;

    public Notificare(){}

    public Notificare(String descriere, Date data, TipNotificare tip, boolean citit) {
        this.descriere = descriere;
        this.data = data;
        this.tip = tip;
        this.citit = citit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipNotificare getTip() {
        return tip;
    }

    public void setTip(TipNotificare tip) {
        this.tip = tip;
    }

    public boolean isCitit() {
        return citit;
    }

    public void setCitit(boolean citit) {
        this.citit = citit;
    }

    @Override
    public String toString() {
        return "Notificare{" +
                "descriere='" + descriere + '\'' +
                ", data=" + data +
                ", tip=" + tip +
                ", citit=" + citit +
                '}';
    }
}
