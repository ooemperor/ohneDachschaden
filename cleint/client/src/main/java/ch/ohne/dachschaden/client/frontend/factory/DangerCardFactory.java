package ch.ohne.dachschaden.client.frontend.factory;

import ch.ohne.dachschaden.client.gvb.Danger;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;

import java.util.List;
import java.util.function.Supplier;

public class DangerCardFactory {

    /**
     * @param onImagesClick      listener for the “images” button
     * @param explanationSupplier supplies danger explanation text
     * @param recommendationsSupplier supplies list of recommendation strings
     */
    public Details create(Danger danger,
                          ComponentEventListener<ClickEvent<Button>> onImagesClick,
                          Supplier<String> explanationSupplier,
                          Supplier<List<String>> recommendationsSupplier) {

        // Header
        H2 header = new H2(danger.toString());
        header.getStyle().set("margin", "0");
        header.getStyle().set("font-weight", "700");

        // Explanation
        Paragraph explanationSub = subHeader("Erläuterung des Risikos");
        Paragraph explanation = new Paragraph(explanationSupplier.get());

        // Recommendations
        Paragraph recSub = subHeader("Empfehlung zur Risikominderung");
        UnorderedList recommendations = toList(recommendationsSupplier.get());

        // Images button
        Button imageButton = new Button("So sind andere Personen das Problem angegangen");
        imageButton.addClickListener(onImagesClick);

        // Card
        Details card = new Details(header, explanationSub, explanation, recSub, recommendations, imageButton);
        card.setOpened(false);
        card.setWidthFull();
        card.addThemeVariants(DetailsVariant.FILLED, DetailsVariant.SMALL);
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
        card.getStyle().set("transition", "box-shadow 160ms ease");
        card.getElement().addEventListener("mouseenter", e -> card.getStyle().set("box-shadow", "var(--lumo-box-shadow-m)"));
        card.getElement().addEventListener("mouseleave", e -> card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)"));
        card.getStyle().set("background-color", danger.getSeverity().getColor());

        return card;
    }

    private static Paragraph subHeader(String text) {
        Paragraph p = new Paragraph(text);
        p.getStyle().set("margin", "0");
        p.getStyle().set("color", "var(--lumo-secondary-text-color)");
        p.getStyle().set("font-weight", "600");
        return p;
    }

    private static UnorderedList toList(List<String> items) {
        UnorderedList ul = new UnorderedList();
        ul.getStyle().set("margin", "var(--lumo-space-s) 0 0 0");
        ul.getStyle().set("padding-left", "1.25rem");
        for (String item : items) {
            ListItem li = new ListItem(new Paragraph(item));
            li.getStyle().set("margin", "0 0 var(--lumo-space-xs) 0");
            ul.add(li);
        }
        return ul;
    }
}
