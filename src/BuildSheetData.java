//import util.ResourcePath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class BuildSheetData {
    int width, height;
    public Image background;
    public ArrayList<IconView> icons;

    public BuildSheetData(String path) {
        icons = new ArrayList<>();
    }
    public BuildSheetData() {
        try {

            InputStream is = getClass().getResourceAsStream("resources/example/examplebackground.png");

            background = ImageIO.read(is);
//            background = ImageIO.read(ResourcePath.getURL("example/examplebackground.png"));
        } catch (IOException e) {
            System.out.println("Could not read from file");
        }
        icons = new ArrayList<>();
        icons.add(new IconView(50, 70, 100, "", ItemType.BAG));
        icons.add(new IconView(140, 70, 100, "", ItemType.HEAD));
        icons.add(new IconView(230, 70, 100, "", ItemType.CAPE));
        icons.add(new IconView(50, 160, 100, "HolyStaff.png", ItemType.MAINHAND));
        icons.add(new IconView(140, 160, 100, "", ItemType.CHEST));
        icons.add(new IconView(230, 160, 100, "", ItemType.OFFHAND));
        icons.add(new IconView(50, 250, 100, "", ItemType.POTION));
        icons.add(new IconView(140, 250, 100, "", ItemType.FEET));
        icons.add(new IconView(230, 250, 100, "", ItemType.FOOD));

        width = 640;
        height = 430;

    }

    public int numItems() {
        return icons.size();
    }
}
