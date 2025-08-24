package ch.ohne.dachschaden.client.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Transactional
    public Picture savePicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Transactional(readOnly = true)
    public Picture getPicture(int id) {
        return pictureRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Picture[] getPictures(String danger) {
        if (danger == null || danger.isBlank()) {
            java.util.List<Picture> all = pictureRepository.findAll();
            return all.toArray(new Picture[0]);
        }
        return pictureRepository.findAllByDanger(danger);
    }
}
