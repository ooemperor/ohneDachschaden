package ch.ohne.dachschaden.client;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("") // root URL
public class HelloView extends VerticalLayout {

    public HelloView() {
        Button helloButton = new Button("Say Hello",
                event -> Notification.show("Hello, world!"));

        Button goodbyeButton = new Button("Say Goodbye",
                event -> Notification.show("Goodbye, world!"));

        add(helloButton, goodbyeButton);
    }
}
