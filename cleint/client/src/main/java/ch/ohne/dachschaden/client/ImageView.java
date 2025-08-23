package ch.ohne.dachschaden.client;

import ch.ohne.dachschaden.client.picture.Picture;
import ch.ohne.dachschaden.client.picture.PictureService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Route("images")
@SuppressWarnings({"removal", "deprecation"})
public class ImageView extends VerticalLayout implements HasUrlParameter<String> {

    private final PictureService pictureService;

    private String danger;
    private String address;
    private final H2 title = new H2("Bilder");
    private final Paragraph subtitle = new Paragraph();
    private final Div gallery = new Div();

    @Autowired
    public ImageView(PictureService pictureService) {
        this.pictureService = pictureService;

        setWidthFull();
        setSpacing(false);
        setPadding(true);

        // Top bar with back arrow and centered upload button
        Button backBtn = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        backBtn.getElement().setAttribute("aria-label", "Zurück zur Detailansicht");
        backBtn.addClickListener(e -> UI.getCurrent().navigate("detail/" + address));
        backBtn.getStyle().set("border-radius", "50%");
        backBtn.getStyle().set("width", "40px");
        backBtn.getStyle().set("height", "40px");
        backBtn.getStyle().set("padding", "0");
        backBtn.getStyle().set("background-color", "#FFFFFF");
        backBtn.getStyle().set("box-shadow", "var(--lumo-box-shadow-xs)");

        Button uploadBtn = new Button("Foto aufnehmen/hochladen", new Icon(VaadinIcon.CAMERA));
        uploadBtn.getStyle().set("background-color", "#FFFFFF");
        uploadBtn.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");

        FileBuffer buffer = new FileBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false); // click-only
        upload.setUploadButton(uploadBtn);
        upload.setAcceptedFileTypes("image/*");
        upload.setMaxFileSize(2147483647);
        upload.getElement().setProperty("capture", "environment");

        upload.addSucceededListener(e -> {
            String fileName = e.getFileName();
            try (InputStream is = buffer.getInputStream()) {
                byte[] bytes = is.readAllBytes();
                if (bytes.length == 0) {
                    Notification.show("Leere Datei hochgeladen");
                    return;
                }
                Picture p = new Picture();
                p.setName(fileName);
                p.setDanger(Objects.toString(danger, ""));
                p.setPicture(bytes);
                pictureService.savePicture(p);
                Notification.show("Bild gespeichert: " + fileName);
                loadPictures();
                // Reset upload so another image can be selected immediately
                upload.clearFileList();
            } catch (IOException ex) {
                Notification.show("Fehler beim Hochladen: " + ex.getMessage());
            }
        });

        upload.addFileRejectedListener(e -> Notification.show("Datei abgelehnt: " + e.getErrorMessage()));
        upload.addFailedListener(e -> Notification.show("Upload fehlgeschlagen"));

        Div spacerLeft = new Div();
        Div spacerRight = new Div();
        spacerLeft.getStyle().set("min-width", "1px");
        spacerRight.getStyle().set("min-width", "1px");

        HorizontalLayout topBar = new HorizontalLayout(backBtn, spacerLeft, upload, spacerRight);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setSpacing(true);
        topBar.setJustifyContentMode(JustifyContentMode.START);
        topBar.setPadding(false);
        topBar.setMargin(false);
        topBar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        topBar.setFlexGrow(0, backBtn);
        topBar.setFlexGrow(1, spacerLeft, spacerRight);
        topBar.setFlexGrow(0, upload);

        title.getStyle().set("margin", "var(--lumo-space-m) 0 var(--lumo-space-xs) 0");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // Responsive gallery: max 3 items per row
        gallery.setWidthFull();
        gallery.getStyle().set("display", "flex");
        gallery.getStyle().set("flex-wrap", "wrap");
        gallery.getStyle().set("justify-content", "space-around");
        gallery.getStyle().set("gap", "12px");

        add(topBar, title, subtitle, gallery);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        this.danger = event.getLocation().getQueryParameters().getParameters().getOrDefault("danger", List.of("")).get(0);
        this.address = event.getLocation().getQueryParameters().getParameters().getOrDefault("address", List.of("")).get(0);
        title.setText("Lösungsvorschläge für " + (danger == null || danger.isBlank() ? "Allgemein" : danger));
        loadPictures();
    }

    private void loadPictures() {
        gallery.removeAll();
        Picture[] pics = pictureService.getPictures(Objects.toString(danger, ""));
        if (pics == null || pics.length == 0) {
            Paragraph empty = new Paragraph("Noch keine Bilder vorhanden.");
            gallery.add(empty);
            return;
        }
        for (Picture p : pics) {
            if (p == null || p.getPicture() == null) continue;
            StreamResource res = new StreamResource(
                    (p.getName() == null || p.getName().isBlank()) ? "image.jpg" : p.getName(),
                    () -> new ByteArrayInputStream(p.getPicture())
            );
            com.vaadin.flow.component.html.Image img = new com.vaadin.flow.component.html.Image(res, p.getName());
            img.setWidth(100, Unit.PERCENTAGE);
            img.getStyle().set("display", "block");
            img.getStyle().set("border-radius", "8px");
            img.getStyle().set("box-shadow", "var(--lumo-box-shadow-xs)");

            Div card = new Div(img);
            card.getStyle().set("padding", "4px");
            card.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
            card.getStyle().set("border-radius", "10px");
            card.getStyle().set("background", "var(--lumo-base-color)");
            card.getStyle().set("display", "flex");
            card.getStyle().set("justify-content", "center");
            card.getStyle().set("align-items", "center");
            // Ensure we can absolutely position overlay within the card
            card.getStyle().set("position", "relative");
            // Flex sizing: up to 3 per row, responsive wrap (min ~160px)
            card.getStyle().set("flex", "1 1 calc(33.333% - 12px)");
            card.getStyle().set("max-width", "calc(33.333% - 12px)");
            card.getStyle().set("min-width", "160px");

            // Randomly add a top-right logo overlay (10x10), centered within a small corner box
            boolean showLogo = ThreadLocalRandom.current().nextBoolean();
            if (showLogo) {
                Div overlay = new Div();
                overlay.getStyle().set("position", "absolute");
                overlay.getStyle().set("top", "0");
                overlay.getStyle().set("right", "0");
                overlay.getStyle().set("width", "20px");
                overlay.getStyle().set("height", "20px");
                overlay.getStyle().set("display", "flex");
                overlay.getStyle().set("align-items", "center");
                overlay.getStyle().set("justify-content", "center");
                overlay.getStyle().set("pointer-events", "none");

                com.vaadin.flow.component.html.Image logo = new com.vaadin.flow.component.html.Image("/logo.png", "logo");
                logo.setWidth("25px");
                logo.setHeight("25px");
                logo.getStyle().set("border-radius", "5px");
                // prevent the overlay image from affecting layout
                logo.getStyle().set("display", "block");

                overlay.add(logo);
                card.add(overlay);
            }

            gallery.add(card);
        }
    }
}
