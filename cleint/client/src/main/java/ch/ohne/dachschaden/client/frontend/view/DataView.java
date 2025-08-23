package ch.ohne.dachschaden.client.frontend.view;

import ch.ohne.dachschaden.client.adminCodes.AdminCodes;
import ch.ohne.dachschaden.client.adminCodes.AdminCodesRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Route("data")
@PageTitle("Admin Codes | ohneDachschaden")
public class DataView extends VerticalLayout {

    private final Grid<AdminCodes> grid = new Grid<>(AdminCodes.class); // auto columns

    @Autowired
    public DataView(AdminCodesRepository repo) {
        setSizeFull();
        grid.setSizeFull();

        // Make auto-generated columns a bit nicer (optional)
        grid.getColumns().forEach(c -> c.setAutoWidth(true).setResizable(true));
        // (No control of order/headers/keys — Vaadin uses bean properties)

        // Lazy data provider (server-side paging + sorting)
        grid.setItems(DataProvider.fromCallbacks(
                // fetch
                (Query<AdminCodes, Void> q) -> {
                    int offset = q.getOffset();
                    int limit = q.getLimit();
                    int page = offset / Math.max(limit, 1);
                    Sort sort = toSpringSort(q.getSortOrders());
                    return repo.findAll(PageRequest.of(page, limit, sort)).stream();
                },
                // count
                q -> (int) repo.count()
        ));

        add(grid);
    }

    private static Sort toSpringSort(List<QuerySortOrder> sortOrders) {
        if (sortOrders == null || sortOrders.isEmpty()) return Sort.unsorted();
        Sort sort = Sort.unsorted();
        for (QuerySortOrder o : sortOrders) {
            var dir = (o.getDirection() == com.vaadin.flow.data.provider.SortDirection.ASCENDING)
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            // Vaadin uses the property name as the key for auto columns
            sort = sort.and(Sort.by(dir, o.getSorted()));
        }
        return sort;
    }
}
