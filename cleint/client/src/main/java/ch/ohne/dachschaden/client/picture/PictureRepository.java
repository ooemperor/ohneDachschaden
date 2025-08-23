package ch.ohne.dachschaden.client.picture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    Picture[] findAllByDanger(String danger);
}
