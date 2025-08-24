package ch.ohne.dachschaden.client.frontend.view;

import ch.ohne.dachschaden.client.gvb.Danger;
import ch.ohne.dachschaden.client.frontend.factory.DangerCardFactory;
import ch.ohne.dachschaden.client.frontend.components.VideoDialogComponent;
import ch.ohne.dachschaden.client.frontend.utility.AddressFormatter;
import ch.ohne.dachschaden.client.gvb.DangerService;
import ch.ohne.dachschaden.client.geoAdmin.EgidService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route("detail")
@PageTitle("Adressdetails | ohneDachschaden")
public class DetailView extends VerticalLayout implements HasUrlParameter<String> {

    private static final Logger log = LoggerFactory.getLogger(DetailView.class);

    private final DangerService dangerService;
    private final EgidService egidService;
    private final DangerCardFactory dangerCardFactory;
    private final VideoDialogComponent videoDialogComponent;

    // UI
    private final VerticalLayout content = new VerticalLayout();
    private final H1 title = new H1("Details der ausgewählten Adresse");
    private final Paragraph description = new Paragraph();
    private final VerticalLayout dangerCards = new VerticalLayout();

    @Autowired
    public DetailView(DangerService dangerService,
                      EgidService egidService) {
        this.dangerService = dangerService;
        this.egidService = egidService;
        this.dangerCardFactory = new DangerCardFactory();
        this.videoDialogComponent = new VideoDialogComponent();

        configureRoot();
        buildLayout();
    }

    // ---------- Vaadin navigation ----------

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String addressParam) {
        dangerCards.removeAll();

        if (addressParam == null || addressParam.isBlank()) {
            showNoAddressState();
            return;
        }

        String address = AddressFormatter.format(addressParam);
        title.setText("Risikendetails für die Adresse:\n" + address);
        description.setText("Unten finden Sie die ermittelten Risikokategorien sowie praktische Empfehlungen, die auf diese Adresse zugeschnitten sind.");

        try {
            String egid = egidService.findEgidByAddress(address);
            log.debug("Resolved EGID={} for address='{}'", egid, address);

            List<Danger> dangers = dangerService.getDangers(egid);
            if (dangers.isEmpty()) {
                Notification.show("Keine Naturgefahren-Daten gefunden.", 3000, Notification.Position.MIDDLE);
                return;
            }

            dangers.forEach(danger -> dangerCards.add(
                    dangerCardFactory.create(
                            danger,
                            // click on “images” button → navigate with query params
                            click -> UI.getCurrent().navigate("/images",
                                    new QueryParameters(Map.of(
                                            "danger", List.of(danger.getName()),
                                            "address", List.of(address)
                                    ))),
                            // text providers
                            () -> dangerService.getDangerExplanation(danger.toString()),
                            () -> dangerService.getRecommendations(egid, danger.toString(), address)
                    )
            ));

        } catch (Exception ex) {
            log.error("DetailView failed for address='{}'", address, ex);
            Notification.show("Fehler beim Laden der Details: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    // ---------- Layout building ----------

    private void configureRoot() {
        setWidthFull();
        setMinHeight("100%");
        setSpacing(false);
        setPadding(true);

        content.setWidthFull();
        content.getStyle().set("max-width", "1024px");
        content.getStyle().set("margin", "0 auto");
        content.getStyle().set("padding", "var(--lumo-space-l)");
        content.setSpacing(true);
        content.setPadding(false);

        dangerCards.setPadding(false);
        dangerCards.setSpacing(true);
        dangerCards.setWidthFull();
        dangerCards.getStyle().set("gap", "var(--lumo-space-m)");

        title.getStyle().set("font-weight", "800");
        title.getStyle().set("letter-spacing", "-0.01em");

        description.getStyle().set("color", "var(--lumo-secondary-text-color)");
        description.getStyle().set("margin", "0 0 var(--lumo-space-m) 0");
    }

    private void buildLayout() {
        var topBar = buildTopBar();
        var footer = videoDialogComponent.buildFooter(); // includes dialog + play button

        content.add(topBar, description, dangerCards, footer);
        add(content, videoDialogComponent.dialog());
    }

    private HorizontalLayout buildTopBar() {
        Button backButton = new Button(new Icon(VaadinIcon.ARROW_LEFT), e -> UI.getCurrent().navigate(""));
        backButton.getElement().setAttribute("aria-label", "Back to Home");
        backButton.getStyle().set("border-radius", "50%");
        backButton.getStyle().set("width", "40px");
        backButton.getStyle().set("height", "40px");
        backButton.getStyle().set("padding", "0");
        backButton.getStyle().set("box-shadow", "var(--lumo-box-shadow-xs)");
        backButton.getStyle().set("background-color", "#FFFFFF");

        var topBar = new HorizontalLayout(backButton, title);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setSpacing(true);
        return topBar;
    }

    private void showNoAddressState() {
        title.setText("Fehlende Adresse");
        description.setText("Keine Adresse ausgewählt. Bitte zur Home-Seite zurückkehren und eine Adresse wählen, um die Risikodetails zu sehen.");
        Notification.show("Keine Adresse übergeben.", 3000, Notification.Position.TOP_CENTER);
    }
}
