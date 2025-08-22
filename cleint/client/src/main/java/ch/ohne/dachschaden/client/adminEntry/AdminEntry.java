package ch.ohne.dachschaden.client.adminEntry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "admin_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp LoadDTS;
    private String EGID;
    private String EDID;
    private String EGAID;
    private String DEINR;
    private String ESID;
    private String STRNAME;
    private String STRNAMK;
    private String STRINDX;
    private String STRSP;
    private String STROFFIZIEL;
    private String DPLZ4;
    private String DPLZZ;
    private String DPLZNAME;
    private String DKODE;
    private String DKODN;
    private String DOFFADR;
    private String DEXPDAT;

}
