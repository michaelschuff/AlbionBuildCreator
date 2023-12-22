package ezbuild.builder;

import ezbuild.util.ConvertItemType;
import ezbuild.EzBuildWindow;
import ezbuild.util.IconView;
import ezbuild.util.ItemType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;

public class BuildSheetView extends Canvas implements MouseListener, MouseMotionListener  {
    public Image buildImage;
    public final BuildSheetData data;
    public final ArrayList<Image> images;
    public EzBuildWindow window;
    public ItemType placeIcon = null;

    public BuildSheetView(BuildSheetData d, EzBuildWindow w) {
        window = w;
        data = d;
        images = new ArrayList<>();
        initImages();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        buildImage = createImage(data.width, data.height);
        Graphics2D imgG = (Graphics2D) buildImage.getGraphics();

        imgG.drawImage(
                data.background,
                0, 0,
                data.width,
                data.height,
                this
        );




        for (int i = 0; i < data.numItems(); i++) {
            IconView icon = data.icons.get(i);

            imgG.drawImage(images.get(i), icon.x, icon.y, icon.size, icon.size, this);
        }
        g.drawImage(buildImage, 0, 0, data.width, data.height, this);
        g.translate(0,25);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        System.out.println("(" + mx + ", " + my + ")");
        if (placeIcon != null) {
            data.addIcon(mx, my, placeIcon);
            IconView icon = data.icons.get(data.icons.size()-1);

            Image im;
            try {
                InputStream is = getClass().getResourceAsStream("../resources/icons/empty/" + icon.type.toString() + ".png");
                if (is == null) throw new IOException();

                im = ImageIO.read(is);
            } catch (IOException ee) {
                System.out.println("Could not read empty image file at resources/icons/empty/" + icon.type.toString() + ".png");
                exit(0); return;
            }
            images.add(im);

            Graphics imgG = buildImage.getGraphics();
            imgG.drawImage(im, icon.x, icon.y, icon.size, icon.size, this);
            if (!window.bm.addMultiple.getState()) placeIcon = null;
        } else {
            if (window.bm.currItemSelected != null) {
                for (int i = 0; i < images.size(); i++) {
                    if (inBounds(mx,my,data.icons.get(i))) {
                        System.out.println(data.icons.get(i).id);
                        String item = window.bm.currItemSelected.getLabel();
                        String tier = window.bm.currTierSelected.getLabel();
                        String url_path = getImageURLString(item, tier);
                        try {
                            images.set(i,ImageIO.read(new URL(url_path)));
                        } catch (Exception err) {
                            System.out.println("Couldn't fetch image at: " + url_path);
                        }
                        IconView icon = data.icons.get(i);
                        Graphics imgG = buildImage.getGraphics();
                        imgG.drawImage(images.get(i), icon.x, icon.y, icon.size, icon.size, this);
                        break;
                    }
                }
            }
        }
        redrawBuildSheet();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();

        g.setColor(Color.red);

        int x = e.getX();
        int y = e.getY();

        g.fillOval(x, y, 5, 5);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


    private boolean inBounds(int u, int v, IconView icon) {
        return !(u < icon.x || u > icon.x + icon.size || v < icon.y || v > icon.y + icon.size);
    }

    private void initImages() {
        for (int i = 0; i < data.numItems(); i++) {
            Image im;
            IconView icon = data.icons.get(i);

            if (!Objects.equals(icon.id, "")) {
                try {
                    InputStream is = getClass().getResourceAsStream("resources/icons/" + icon.id);
                    if (is == null) throw new IOException();
                    im = ImageIO.read(is);
                } catch (IOException e) {
                    System.out.println("Could not read image at " + icon.id + ".png");
                    try {
                        InputStream is = getClass().getResourceAsStream("../resources/icons/empty/" + icon.type.toString() + ".png");
                        if (is == null) throw new IOException();
                        im = ImageIO.read(is);
                    } catch (IOException ee) {
                        System.out.println("Could not read empty image file at resources/icons/empty/" + icon.type.toString() + ".png");
                        exit(0); return;
                    }
                }
            } else {
                try {
                    InputStream is = getClass().getResourceAsStream("../resources/icons/empty/" + icon.type.toString() + ".png");
                    if (is == null) throw new IOException();
                    im = ImageIO.read(is);
                } catch (IOException e) {
                    System.out.println("Could not read empty image file at resources/icons/empty/" + icon.type.toString() + ".png");
                    exit(0); return;
                }
            }

            images.add(im);
        }
    }

    private String getImageURLString(String name, String t) {
        String prefix = "",suffix = "";

        char tier = t.charAt(0);
        char enchant = t.charAt(t.length()-1);
        switch (tier) {
            case '1' -> prefix = "Beginner's ";
            case '2' -> prefix = "Novice's ";
            case '3' -> prefix = "Journeyman's ";
            case '4' -> prefix = "Adept's ";
            case '5' -> prefix = "Expert's ";
            case '6' -> prefix = "Master's ";
            case '7' -> prefix = "Grandmaster's ";
            case '8' -> prefix = "Elder's ";
        }

        if (enchant != '0') {
            suffix = "@" + enchant;
        }

        if (tier == 8) {
            switch (name) {
                case "Battleaxe" -> {
                    prefix = "";
                    name = "The Hand of Khor";
                }
                case "Tome of Spells" -> {
                    prefix = "";
                    name = "Rosalia's Diary";
                }
                case "Great Fire Staff" -> {
                    prefix = "";
                    name = "Vendetta's Wrath";
                }
            }
        }
        return ("https://render.albiononline.com/v1/item/" + prefix + name + suffix + ".png").replace(' ','+');
    }

    public void placeIconMode(String s) {
        placeIcon = ConvertItemType.fromString(s);
    }

    public void redrawBuildSheet() {
        Graphics g = getGraphics();
        g.drawImage(buildImage, 0, 0, data.width, data.height, this);
        g.translate(0,25);
    }

    public void saveTemplate() {
        data.toTemplate().saveTemplate();
    }
}
