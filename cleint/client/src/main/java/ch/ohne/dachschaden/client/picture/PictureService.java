package ch.ohne.dachschaden.client.picture;

public class PictureService {
    private PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public void savePicture(Picture picture) {
        pictureRepository.save(picture);
    }

    public Picture getPicture(int id) {
        return pictureRepository.findById(id).orElse(null);
    }
}
