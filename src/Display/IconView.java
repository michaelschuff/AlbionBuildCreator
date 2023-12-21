package Display;

import Items.ItemType;

import java.awt.*;

public class IconView {
    public int x, y;
    public int size;

    public String id;

    public ItemType type;

    public IconView(int x, int y, int size, String id, ItemType type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.id = id;
        this.type = type;
    }
}
