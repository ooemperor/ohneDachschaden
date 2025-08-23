package ch.ohne.dachschaden.client;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * Home view with address input, Leaflet map, and logging button.
 */
@Route("")
@StyleSheet("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css")
@JavaScript("https://unpkg.com/leaflet@1.9.4/dist/leaflet.js")
@StyleSheet("https://unpkg.com/leaflet-control-geocoder/dist/Control.Geocoder.css")
@JavaScript("https://unpkg.com/leaflet-control-geocoder/dist/Control.Geocoder.js")
public class HomeView extends VerticalLayout {

    private final ComboBox<String> addressBox = new ComboBox<>("Adresse");
    private final Div mapDiv = new Div();
    private final Button logButton = new Button("Details anzeigen");

    private Double selectedLat;
    private Double selectedLng;
    private String selectedAddress;

    // Keep mapping of suggestion string to coordinates
    private final Map<String, double[]> suggestionCoords = new HashMap<>();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public HomeView() {
        setSizeFull();

        addressBox.setWidthFull();
        addressBox.setPlaceholder("Adresse suchen...");
        addressBox.setAllowCustomValue(true);
        addressBox.setClearButtonVisible(true);
        addressBox.setHelperText("Geben Sie eine Adresse in das Suchfeld ein oder klicken Sie auf die Karte, um einen Ort auszuwählen.");

        // Data provider for suggestions via Nominatim (OpenStreetMap)
        addressBox.setItems(query -> {
            // Vaadin requires accessing paging parameters to comply with the contract
            final int limit = query.getLimit();
            final int offset = query.getOffset();

            String filter = Objects.toString(query.getFilter().orElse(""), "").trim();
            if (filter.isEmpty()) {
                // Still return an empty stream but we have read limit/offset above
                return List.<String>of().stream();
            }
            try {
                List<String> suggestions = nominatimSearch(filter);
                return suggestions.stream().skip(offset).limit(limit);
            } catch (Exception e) {
                // Avoid UI updates inside data provider callbacks; just log
                System.out.println("[AddressLookup] Lookup failed: " + e.getMessage());
                return List.<String>of().stream();
            }
        });

        addressBox.addValueChangeListener(e -> {
            String value = e.getValue();
            if (value != null) {
                selectedAddress = value;
                double[] coords = suggestionCoords.get(value);
                if (coords != null) {
                    selectedLat = coords[0];
                    selectedLng = coords[1];
                    // Update map marker to selected address
                    mapDiv.getElement().executeJs("this._setMarkerLocation && this._setMarkerLocation($0,$1,true)", selectedLat, selectedLng);
                }
            }
        });

        mapDiv.setId("leafletMap");
        mapDiv.getStyle().set("width", "100%");
        mapDiv.getStyle().set("height", "400px");
        mapDiv.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        mapDiv.getStyle().set("borderRadius", "8px");
        // Make sure map container participates in stacking order with a low z-index
        mapDiv.getStyle().set("position", "relative");
        mapDiv.getStyle().set("z-index", "0");
        mapDiv.getElement().setProperty("title", "Leaflet Map");


        logButton.addClickListener(e -> {
            UI.getCurrent().navigate("detail/" + selectedAddress);
        });

        add(addressBox, mapDiv, logButton);
        setHorizontalComponentAlignment(Alignment.STRETCH, addressBox, mapDiv, logButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Initialize Leaflet map and handlers inside this element
        // Execute on the host (Home) element so that host.$server is available for @ClientCallable
        // Inject high z-index for vaadin overlays to ensure ComboBox dropdown renders above the Leaflet map
        getElement().executeJs(
                "(function(){" +
                "  let style = document.getElementById('overlay-zfix');" +
                "  if (!style) {" +
                "    style = document.createElement('style');" +
                "    style.id = 'overlay-zfix';" +
                "    style.textContent = 'vaadin-combo-box-overlay, vaadin-overlay { z-index: 6000 !important; }';" +
                "    document.head.appendChild(style);" +
                "  }" +
                "})();"
        );

        getElement().executeJs(
                "" +
                "const host = this;" +
                "const el = host.querySelector('#leafletMap');" +
                "if (!el) { return; }" +
                "if (el._map) { return; }" +
                "el._map = L.map(el, { zoomControl: true });" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
                "  attribution: '&copy; OpenStreetMap contributors'" +
                "}).addTo(el._map);" +
                "el._marker = L.marker([0,0], { draggable: false });" +
                "el._setMarkerLocation = function(lat, lng, pan){" +
                "  el._marker.setLatLng([lat, lng]).addTo(el._map);" +
                "  if (pan) { el._map.setView([lat, lng], 16); }" +
                "};" +
                "if (L.Control && L.Control.Geocoder) {" +
                "  L.Control.geocoder({ defaultMarkGeocode: false })" +
                "    .on('markgeocode', function(e){" +
                "      const center = e.geocode.center;" +
                "      el._setMarkerLocation(center.lat, center.lng, true);" +
                "      fetch('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + center.lat + '&lon=' + center.lng)" +
                "        .then(r=>r.json()).then(data=>{ const disp = data.display_name || (center.lat + ',' + center.lng); host.$server.updateAddress(disp, center.lat, center.lng); });" +
                "    })" +
                "    .addTo(el._map);" +
                "}" +
                "if (navigator.geolocation) {" +
                "  navigator.geolocation.getCurrentPosition(function(pos){" +
                "    const lat = pos.coords.latitude; const lng = pos.coords.longitude;" +
                "    el._setMarkerLocation(lat, lng, true);" +
                "    fetch('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + lat + '&lon=' + lng)" +
                "      .then(r=>r.json()).then(data=>{ const disp = data.display_name || (lat + ',' + lng); host.$server.updateAddress(disp, lat, lng); });" +
                "  }, function(){ el._map.setView([47.3769, 8.5417], 12); });" +
                "} else { el._map.setView([47.3769, 8.5417], 12); }" +
                "el._map.on('click', function(e){" +
                "  const lat = e.latlng.lat; const lng = e.latlng.lng;" +
                "  el._setMarkerLocation(lat, lng, false);" +
                "  fetch('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + lat + '&lon=' + lng)" +
                "    .then(r=>r.json()).then(data=>{ const disp = data.display_name || (lat + ',' + lng); host.$server.updateAddress(disp, lat, lng); });" +
                "});"
        );
    }

    @ClientCallable
    private void updateAddress(String address, double lat, double lng) {
        // Called from client-side JS to update server-side state
        this.selectedAddress = address;
        this.selectedLat = lat;
        this.selectedLng = lng;
        // Update ComboBox value without triggering item lookup
        addressBox.setValue(address);
    }

    private List<String> nominatimSearch(String query) throws IOException, InterruptedException {
        // Respect Nominatim usage policy by sending a UA header
        String url = "https://nominatim.openstreetmap.org/search?format=jsonv2&addressdetails=1&limit=10&q=" +
                URLEncoder.encode(query, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("User-Agent", "vaadin-leaflet-demo/1.0 (example)")
                .header("Accept", "application/json")
                .header("Accept-Language", "en;q=0.8, *;q=0.5")
                .header("Referer", "https://example.local/vaadin-home")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            return List.of();
        }
        String body = response.body();
        JsonArray arr = (JsonArray) Json.instance().parse(body);
        List<String> results = new ArrayList<>();
        suggestionCoords.clear();
        for (int i = 0; i < arr.length(); i++) {
            JsonObject obj = (JsonObject) arr.get(i);
            String display = obj.hasKey("display_name") ? obj.getString("display_name") : null;
            if (display != null) {
                double lat = Double.parseDouble(obj.getString("lat"));
                double lon = Double.parseDouble(obj.getString("lon"));
                results.add(display);
                suggestionCoords.put(display, new double[]{lat, lon});
            }
        }
        return results;
    }
}
