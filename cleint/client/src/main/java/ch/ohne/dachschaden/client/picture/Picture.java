package ch.ohne.dachschaden.client.picture;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Picture {

    @Id
    @GeneratedValue
    public int id;

    private String name;
    private String danger;

    @Lob
    private byte[] picture;
}
