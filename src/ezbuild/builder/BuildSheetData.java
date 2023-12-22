package ezbuild.builder;

import ezbuild.util.IconView;
import ezbuild.util.ItemType;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.System.exit;
import static ezbuild.util.CONSTANTS.*;

public class BuildSheetData {
    public int width, height, itemSize = DEFAULT_ITEM_SIZE, abilitySize = DEFAULT_ABILITY_SIZE;
    public Image background;
    public ArrayList<IconView> icons;

    public BuildSheetData(String templatePath, String backgroundPath) {
        try {
            BuildTemplate template = BuildTemplate.loadTemplate(templatePath);
            width = template.width;
            height = template.height;

            icons = template.icons;

        } catch (IOException e) {
            System.out.println("Error loading Template from file at: " + templatePath);
            exit(0);
        }

        try {
            InputStream is = getClass().getResourceAsStream("../" + backgroundPath);
            if (is == null) throw new IOException();
            background = ImageIO.read(is);
        } catch (IOException e) {
            System.out.println("Could not read background file at: " + backgroundPath);
        }
    }
    public BuildSheetData(BuildTemplate template) {
        width = template.width;
        height = template.height;

        icons = template.icons;
    }
    public BuildSheetData(String backgroundPath) {
        try {
            File image = new File(backgroundPath);
            InputStream is = new FileInputStream(image);
            background = ImageIO.read(is);
            width = background.getWidth(null);
            height = background.getHeight(null);
            System.out.println(width);
            System.out.println(height);


            icons = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Could not read background file at: " + backgroundPath);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
        }
    }

    public int numItems() {
        return icons.size();
    }

    void addIcon(int x, int y, ItemType type) {
        if (type == ItemType.ABILITY) {
            icons.add(new IconView(
                    x - abilitySize / 2,
                    y - abilitySize / 2,
                    abilitySize,
                    "", type
            ));
        } else {
            icons.add(new IconView(
                            x - itemSize / 2,
                            y - itemSize / 2,
                            itemSize,
                            "", type
            ));
        }

    }

    public BuildTemplate toTemplate() {
        BuildTemplate template = new BuildTemplate(width, height);
        template.icons = icons;
        return template;
    }
}
