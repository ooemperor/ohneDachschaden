package ch.ohne.dachschaden.client.adminApartment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "admin_apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminApartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp LoadDTS;
    private String EGID;
    private String EWID;
    private String EDID;
    private String WHGNR;
    private String WEINR;
    private String WSTWK;
    private String WBEZ;
    private String WMEHRG;
    private String WBAUJ;
    private String WABBJ;
    private String WSTAT;
    private String WAREA;
    private String WAZIM;
    private String WKCHE;
    private String WEXPDAT;

}
