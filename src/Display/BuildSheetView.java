package Display;

import util.ResourcePath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;

public class BuildSheetView extends Canvas implements MouseListener, MouseMotionListener  {
    private final BuildSheetData data;
    private final ArrayList<Image> images;


    private final int rule = AlphaComposite.SRC_OVER;
    private final Composite transparent = AlphaComposite.getInstance(rule, 0.5f);
    private final Composite opaque = AlphaComposite.getInstance(rule, 1f);
    private final Window window;

    public BuildSheetView(BuildSheetData d, Window w) {
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
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(
                data.background,
                0, 0,
                data.width,
                data.height,
                this
        );




        for (int i = 0; i < data.numItems(); i++) {
            IconView icon = data.icons.get(i);

            g2.drawImage(images.get(i), icon.x, icon.y, icon.size, icon.size, this);
        }
        g2.translate(0,25);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Graphics g = getGraphics();

        g.setColor(Color.red);

        int mx = e.getX(), my = e.getY();


        g.fillOval(mx, my, 5, 5);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        for (int i = 0; i < images.size(); i++) {
            if (inBounds(mx,my,data.icons.get(i))) {
                System.out.println(data.icons.get(i).id);
                String item = window.currItemSelected.getLabel();
                String tier = window.currTierSelected.getLabel();
                String url_path = getImageURLString(item, tier);
                try {
                    images.set(i,ImageIO.read(new URL(url_path)));
                } catch (Exception ee) {
                    System.out.println("Couldn't fetch image at: " + url_path);
                    // handle IOException
                }
                Graphics g = getGraphics();
                IconView icon = data.icons.get(i);
                g.drawImage(images.get(i), icon.x, icon.y, icon.size, icon.size, this);
            }
        }
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
                    im = ImageIO.read(ResourcePath.getURL(icon.id));
                } catch (IOException e) {
                    System.out.println("Could not read image at " + icon.id + ".png");
                    try {
                        im = ImageIO.read(ResourcePath.getURL("icons/empty/" + icon.type.toString() + ".png"));
                    } catch (IOException ee) {
                        System.out.println("Could not read empty image file at icons/empty/" + icon.type.toString() + ".png");
                        exit(0); return;
                    }
                }
            } else {
                try {
                    im = ImageIO.read(ResourcePath.getURL("icons/empty/" + icon.type.toString() + ".png"));
                } catch (IOException e) {
                    System.out.println("Could not read empty image file at icons/empty/" + icon.type.toString() + ".png");
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
}
