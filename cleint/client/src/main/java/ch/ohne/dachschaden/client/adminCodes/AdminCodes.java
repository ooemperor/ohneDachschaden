package ch.ohne.dachschaden.client.adminCodes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "admin_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp LoadDTS;
    private String CECODID;
    private String CMERKM;
    private String CODTXTLD;
    private String CODTXTKD;
    private String CODTXTLF;
    private String CODTXTKF;
    private String CODTXTLI;
    private String CODTXTKI;
    private String CEXPDAT;

}
