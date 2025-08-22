package ch.ohne.dachschaden.client;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route("detail")
public class DetailView extends VerticalLayout implements HasUrlParameter<String> {

    private H1 title = new H1("Address Details");
    private Paragraph name = new Paragraph();
    private Paragraph description = new Paragraph();

    public DetailView() {
        add(title, name, description);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String address) {
        if (address != null) {
           name.setText(address);
        } else {
            name.setText("Address not found");
        }
    }
}
