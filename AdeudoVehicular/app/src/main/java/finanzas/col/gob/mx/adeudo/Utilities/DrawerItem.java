package finanzas.col.gob.mx.adeudo.Utilities;

/**
 * Created by lnunez on 30/12/2015.
 */
public class DrawerItem {
    private String name;
    private Integer iconId;

    public DrawerItem(String name, Integer iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
