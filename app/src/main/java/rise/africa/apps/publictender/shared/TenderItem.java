package rise.africa.apps.publictender.shared;

/**
 * The {@link TenderItem} class.
 * <p>Defines the attributes for a restaurant menu item.</p>
 */
public class TenderItem {

    private final String id;
    private final String title;
    private final String source;
    private final String is_open;
    private final String published_date;

    public TenderItem(String id, String title, String source, String is_open, String published_date) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.is_open = is_open;
        this.published_date = published_date;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getIs_open() {
        return is_open;
    }

    public String getPublished_date() {
        return published_date;
    }

}
