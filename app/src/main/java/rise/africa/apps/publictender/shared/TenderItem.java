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
    private final String closing_date;
    private final String days_left;

    public TenderItem(String id, String title, String source, String is_open, String closing_date, String days_left) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.is_open = is_open;
        this.closing_date = closing_date;
        this.days_left = days_left;
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

    public String getClosing_date() {
        return closing_date;
    }
    public String getDays_left() {
        return days_left;
    }

}
