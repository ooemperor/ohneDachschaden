package ch.ohne.dachschaden.client.frontend.components;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class VideoDialogComponent {

    private final Dialog dialog;
    private final HtmlComponent video;

    public VideoDialogComponent() {
        this.dialog = new Dialog();
        this.video = new HtmlComponent("video");
        configure();
    }

    public Dialog dialog() {
        return dialog;
    }

    public HorizontalLayout buildFooter() {
        Button playVideoButton = new Button("Schaden Melden", new Icon(VaadinIcon.EXCLAMATION_CIRCLE));
        playVideoButton.addClickListener(e -> {
            dialog.open();
            video.getElement().executeJs(
                    "const el = this; try { el.pause(); el.currentTime = 0; } catch(e) {}" +
                            "if (el.readyState >= 3) { el.play(); } else {" +
                            "  const onCanPlay = () => { el.play(); el.removeEventListener('canplay', onCanPlay); };" +
                            "  el.addEventListener('canplay', onCanPlay, { once: true }); el.load();" +
                            "}"
            );
        });
        playVideoButton.getStyle().set("margin-top", "var(--lumo-space-l)");
        playVideoButton.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
        playVideoButton.getStyle().set("background-color", "#FFFFFF");

        var footer = new HorizontalLayout(playVideoButton);
        footer.setWidthFull();
        footer.setJustifyContentMode(HorizontalLayout.JustifyContentMode.CENTER);
        footer.setPadding(false);
        return footer;
    }

    private void configure() {
        dialog.setHeaderTitle("Schaden-Meldung Zentrale");
        dialog.setWidth("min(90vw, 900px)");
        dialog.setMaxWidth("90vw");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        video.getElement().setAttribute("controls", true);
        video.getElement().setAttribute("playsinline", true);
        video.getElement().setAttribute("preload", "metadata");
        video.getElement().getStyle().set("width", "100%");
        video.getElement().getStyle().set("max-height", "80vh");
        video.getElement().setAttribute("src", "/MerciSchadenMeldung.mp4");
        video.getElement().setAttribute("type", "video/mp4");

        dialog.add(video);
        dialog.addOpenedChangeListener(ev -> {
            if (!ev.isOpened()) {
                video.getElement().executeJs("try{ this.pause(); this.currentTime = 0; }catch(e){}");
            }
        });
    }
}
