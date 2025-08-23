package ch.ohne.dachschaden.client.adminBuilding;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "admin_building")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBuilding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp LoadDTS;
    private String EGID;
    private String GDEKT;
    private String GGDENR;
    private String GGDENAME;
    private String EGRID;
    private String LGBKR;
    private String LPARZ;
    private String LPARZSX;
    private String LTYP;  // Typ des Grundstücks
    private String GEBNR;
    private String GBEZ;
    private String GKODE;
    private String GKODN;
    private String GKSCE;
    private String GSTAT;
    private String GKAT;  // Gebäudekategorie
    private String GKLAS;  // Gebäudeklasse
    private String GBAUJ;  // Baujahr
    private String GBAUM;
    private String GBAUP;
    private String GABBJ;
    private String GAREA;
    private String GVOL;
    private String GVOLNORM;
    private String GVOLSCE;
    private String GASTW;  // Anzahl Geschosse
    private String GANZWHG;
    private String GAZZI;
    private String GSCHUTZR;
    private String GEBF;
    private String GWAERZH1;
    private String GENH1;
    private String GWAERSCEH1;
    private String GWAERDATH1;
    private String GWAERZH2;
    private String GENH2;
    private String GWAERSCEH2;
    private String GWAERDATH2;
    private String GWAERZW1;
    private String GENW1;
    private String GWAERSCEW1;
    private String GWAERDATW1;
    private String GWAERZW2;
    private String GENW2;
    private String GWAERSCEW2;
    private String GWAERDATW2;
    private String GEXPDAT;

}
