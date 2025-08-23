package ch.ohne.dachschaden.client;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("detail")
public class DetailView extends VerticalLayout implements HasUrlParameter<String> {

    private final H1 title = new H1("Address Details");
    private final Paragraph description = new Paragraph();
    private final VerticalLayout dangerCards = new VerticalLayout();
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout topBar = new HorizontalLayout();
    private final Button backButton = new Button(new Icon(VaadinIcon.ARROW_LEFT));
    private final DangerService service;
    private final EgidService egidService;
    private List<Danger> dangers;

    @Autowired
    public DetailView(
            DangerService dangerService,
            EgidService egidService) {
        this.service = dangerService;
        this.egidService = egidService;

        // Root layout styling
        setWidthFull();
        setMinHeight("100%");
        setSpacing(false);
        setPadding(false);
        getStyle().set("background", "linear-gradient(180deg, var(--lumo-base-color) 0%, var(--lumo-contrast-5pct) 100%)");

        // Centered content container for responsive layout
        content.setWidthFull();
        content.getStyle().set("max-width", "1024px");
        content.getStyle().set("margin", "0 auto");
        content.getStyle().set("padding", "var(--lumo-space-l)");
        content.setSpacing(true);
        content.setPadding(false);

        // Top bar with back button and title
        backButton.getElement().setAttribute("aria-label", "Back to Home");
        backButton.addClickListener(e -> UI.getCurrent().navigate(""));
        backButton.getStyle().set("border-radius", "50%");
        backButton.getStyle().set("width", "40px");
        backButton.getStyle().set("height", "40px");
        backButton.getStyle().set("padding", "0");
        backButton.getStyle().set("box-shadow", "var(--lumo-box-shadow-xs)");

        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setSpacing(true);
        topBar.add(backButton, title);

        // Title and description styling
        title.getStyle().set("font-weight", "800");
        title.getStyle().set("letter-spacing", "-0.01em");

        description.getStyle().set("color", "var(--lumo-secondary-text-color)");
        description.getStyle().set("margin", "0 0 var(--lumo-space-m) 0");

        // Cards container
        dangerCards.setPadding(false);
        dangerCards.setSpacing(true);
        dangerCards.setWidthFull();
        dangerCards.getStyle().set("gap", "var(--lumo-space-m)");

        // Video dialog setup
        Dialog videoDialog = new Dialog();
        videoDialog.setHeaderTitle("Schaden-Meldung Zentrale");
        videoDialog.setWidth("min(90vw, 900px)");
        videoDialog.setMaxWidth("90vw");
        videoDialog.setCloseOnEsc(true);
        videoDialog.setCloseOnOutsideClick(true);

        HtmlComponent video = new HtmlComponent("video");
        video.getElement().setAttribute("controls", true);
        // Do not autoplay on page load; only start on button press
        video.getElement().setAttribute("playsinline", true);
        video.getElement().setAttribute("preload", "metadata");
        video.getElement().getStyle().set("width", "100%");
        video.getElement().getStyle().set("max-height", "80vh");
        
        // Serve from Spring Boot static resources (classpath:/resources/)
        video.getElement().setAttribute("src", "/MerciSchadenMeldung.mp4");
        video.getElement().setAttribute("type", "video/mp4");
        videoDialog.add(video);

        // Pause/reset when dialog closes
        videoDialog.addOpenedChangeListener(ev -> {
            if (!ev.isOpened()) {
                video.getElement().executeJs("try{ this.pause(); this.currentTime = 0; }catch(e){}");
            }
        });

        // Footer with play button
        Button playVideoButton = new Button("Schaden Melden", new Icon(VaadinIcon.EXCLAMATION_CIRCLE));
        playVideoButton.addClickListener(e -> {
            videoDialog.open();
            // Start playback when ready; counts as user gesture
            video.getElement().executeJs(
                "const el = this; try { el.pause(); el.currentTime = 0; } catch(e) {}\n" +
                "if (el.readyState >= 3) { el.play(); } else {\n" +
                "  const onCanPlay = () => { el.play(); el.removeEventListener('canplay', onCanPlay); };\n" +
                "  el.addEventListener('canplay', onCanPlay, { once: true });\n" +
                "  el.load();\n" +
                "}"
            );
        });
        playVideoButton.getStyle().set("margin-top", "var(--lumo-space-l)");
        playVideoButton.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");

        HorizontalLayout footer = new HorizontalLayout(playVideoButton);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.setPadding(false);

        content.add(topBar, description, dangerCards, footer);
        add(content, videoDialog);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String address) {
        String formattedAddress = formatAddress(address);
        dangerCards.removeAll();
        if (address != null && !address.isBlank()) {
            title.setText("Risk details for " + formattedAddress);

            // Replace description content to include context
            description.setText("Below are identified risk categories and practical recommendations tailored for this address.");
            try {
                String egid = egidService.findEgidByAddress(formattedAddress);
                System.out.println("egid: " + egid);
                dangers = service.getDangers(egid);
                if (dangers != null) {
                    for (Danger danger : dangers) {
                        // Header for card
                        H2 header = new H2(danger.toString());
                        header.getStyle().set("margin", "0");
                        header.getStyle().set("font-weight", "700");

                        Paragraph dangerExplanationParagraphSubheader = new Paragraph("Explanation");
                        dangerExplanationParagraphSubheader.getStyle().set("margin", "0");
                        dangerExplanationParagraphSubheader.getStyle().set("color", "var(--lumo-secondary-text-color)");
                        dangerExplanationParagraphSubheader.getStyle().set("font-weight", "600");

                        Paragraph dangerExplanationParagraph = new Paragraph(service.getDangerExplanation(danger.toString()));

                        // Recommendations as a bulleted list
                        UnorderedList recommendations = new UnorderedList();
                        recommendations.getStyle().set("margin", "var(--lumo-space-s) 0 0 0");
                        recommendations.getStyle().set("padding-left", "1.25rem");
                        for (String recommendation : service.getRecommendations(egid, danger.toString(), address)) {
                            ListItem li = new ListItem(new Paragraph(recommendation));
                            li.getStyle().set("margin", "0 0 var(--lumo-space-xs) 0");
                            recommendations.add(li);
                        }

                        // Optional subheader
                        Paragraph sub = new Paragraph("Recommendations");
                        sub.getStyle().set("margin", "0");
                        sub.getStyle().set("color", "var(--lumo-secondary-text-color)");
                        sub.getStyle().set("font-weight", "600");

                        // Image Buttons
                        Button imageButton = new Button("See other solutions");
                        Map<String, List<String>> parameters = new HashMap<>();
                        parameters.put("danger", Collections.singletonList(danger.getName()));
                        parameters.put("address", Collections.singletonList(address));
                        imageButton.addClickListener(e -> UI.getCurrent().navigate("/images", new QueryParameters(parameters)));

                        // Card component (Details) with premium styling
                        Details card = new Details(header, dangerExplanationParagraphSubheader, dangerExplanationParagraph, sub, recommendations);
                        card.setOpened(false);
                        card.setWidthFull();
                        card.addThemeVariants(DetailsVariant.FILLED, DetailsVariant.SMALL);
                        card.getStyle().set("border-radius", "12px");
                        card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
                        card.getStyle().set("transition", "box-shadow 160ms ease");
                        card.getElement().addEventListener("mouseenter", e -> {
                            card.getStyle().set("box-shadow", "var(--lumo-box-shadow-m)");
                        });
                        card.getElement().addEventListener("mouseleave", e -> {
                            card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
                        });
                        card.getStyle().set("background-color", danger.getSeverity().getColor());

                        dangerCards.add(card);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            title.setText("Address Details");
            description.setText("No address provided. Return to the home view and choose a location to see tailored risk details.");
        }
    }

    private String formatAddress(String displayName) {
        if (displayName == null || displayName.isBlank()) return "";
        String[] raw = displayName.split(",");
        java.util.List<String> parts = new java.util.ArrayList<>();
        for (String r : raw) {
            String t = r.trim();
            if (!t.isEmpty()) parts.add(t);
        }
        if (parts.isEmpty()) return displayName;

        String street = null;
        String number = null;

        if (parts.size() >= 2) {
            String p0 = parts.get(0);
            String p1 = parts.get(1);
            if (p0.matches("\\d+[a-zA-Z]?$")) { // e.g., 4 or 4a
                number = p0;
                street = p1;
            } else if (p1.matches("\\d+[a-zA-Z]?$")) {
                street = p0;
                number = p1;
            }
        }

        String postal = null;
        int postalIndex = -1;
        for (int i = 0; i < parts.size(); i++) {
            String t = parts.get(i);
            if (t.matches("\\d{4,5}$")) {
                postal = t;
                postalIndex = i;
                break;
            }
        }

        String city = null;
        if (postalIndex > 0) {
            for (int i = postalIndex - 1; i >= 0; i--) {
                String cand = parts.get(i);
                String low = cand.toLowerCase();
                if (low.contains("administrative") || low.contains("region") || low.contains("district")
                        || low.contains("arrondissement") || low.contains("county") || low.contains("borough")
                        || low.contains("stadtteil") || low.contains("bezirk") || low.contains("kanton")
                        || low.matches("schweiz|switzerland|suisse|svizzera")) {
                    continue;
                }
                city = cand;
                break;
            }
        }

        if (street != null && number != null && city != null) {
            return street + " " + number + " " + city;
        }
        if (street != null && number != null) {
            if (postal != null && city != null) {
                return street + " " + number + " " + city;
            }
            return street + " " + number;
        }
        if (postal != null && city != null) {
            return city + " " + postal;
        }
        return displayName;
    }
}
