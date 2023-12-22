package ezbuild.builder;

import ezbuild.util.IconView;
import ezbuild.util.ItemType;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java.lang.System.exit;
import static ezbuild.util.CONSTANTS.DEFAULT_ABILITY_SIZE;
import static ezbuild.util.CONSTANTS.DEFAULT_ITEM_SIZE;

/*
 * A BuildTemplate is a .tmp file that contains icon locations, sizes,
 * and types. You can create a BuildTemplate file from a Builder.main.Builder.BuildSheetData
 * instance, or manually.
 * The BuildTemplate file format is formatted as follows:
 *
 * Each file has two sections. The header and the data section.
 *
 * --- HEADER ---
 * The Header consists of one line with 4 integers, specifying
 *  - [integer > 0] background image width
 *  - [integer > 0] background image height
 * and optionally
 *  - [integer > 0] default icon size           100 by default
 *  - [integer > 0] default ability icon size   75 by default
 * each separated by a single space character
 *
 * --- DATA ---
 * Each data entry is described in a single line and represents
 * one icon in a template. Each entry contains:
 *  - [integer >= 0] x position of the top left corner of the icon
 *  - [integer >= 0] y position of the top left corner of the icon
 * and optionally
 *  - [string] type of Icon.                    GENERIC by default.
 *                                              It must be exactly a main.util.ItemType,
 *                                              specified at util/main.util.ItemType.java
 *  - [integer >= 0] icon size.                 Overrides default in Header
 * each separated by a single space character.
 *
 *
 * an example file can be found in main.resources/templates/DetailedTemplate.tmp
 *
 * Any empty line or line starting with '#' will be ignored
 * newline at end of file is optional.
 */
public final class BuildTemplate {
    int width, height, itemSize = DEFAULT_ITEM_SIZE, abilitySize = DEFAULT_ABILITY_SIZE;
    public ArrayList<IconView> icons;

    public BuildTemplate(int w, int h) {
        width = w;
        height = h;
        icons = new ArrayList<>();
    }
    public BuildTemplate(int w, int h, int s1, int s2) {
        width = w;
        height = h;
        itemSize = s1;
        abilitySize = s2;
        icons = new ArrayList<>();
    }
    public void addIcon(int x, int y) {
        addIcon(x,y,itemSize,ItemType.GENERIC);
    }
    public void addIcon(int x, int y, ItemType type) {
        if (type == ItemType.ABILITY)
            addIcon(x,y,abilitySize,type);
        else
            addIcon(x,y,itemSize,type);
    }
    public void addIcon(int x, int y, int size) {
        addIcon(x,y,size,ItemType.GENERIC);
    }
    public void addIcon(int x, int y, int size, ItemType type) {
        icons.add(new IconView(x,y,size,"",type));
    }
    public static BuildTemplate loadTemplate(String path) throws IOException {
        InputStream is = BuildTemplate.class.getResourceAsStream("../" + path);
        if (is == null) {
            System.out.println("Error reading from file ../" + path);
            exit(0);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String cmd;

        BuildTemplate template = null;


        int line_counter = 0;
        // -------------------- HEADER --------------------
        while (true) {
            cmd = reader.readLine();
            if (cmd == null) {
                System.out.println("Invalid BuildTemplate file format read at" + path + ". \n file is e");
                exit(0);
            }
            line_counter++;
            if (cmd.equals("") || cmd.charAt(0) == '#' || cmd.split("\\s").length == 0) continue;

            String[] header = cmd.split("\\s");

            if (header.length < 2) {
                System.out.println("Invalid BuildTemplate file format read at" + path + ". \n Header must contain at least a width and a height");
                exit(0);
            }

            int w = parseInt(header[0]);
            int h = parseInt(header[1]);

            if (w <= 0 || h <= 0) {
                System.out.println("Invalid BuildTemplate file format read at" + path + ". \n Header width and height must be greater than 0");
                exit(0);
            }


            if (header.length == 2) {
                template = new BuildTemplate(w,h);
            } else if (header.length == 3 || (header[3].equals(""))) {
                int s1 = parseInt(header[2]);
                if (s1 <= 0) {
                    System.out.println("Invalid BuildTemplate file format read at" + path + ". \n Header itemSize must be greater than 0");
                    exit(0);
                }
                template = new BuildTemplate(w,h,s1,DEFAULT_ABILITY_SIZE);
            } else {
                int s1 = parseInt(header[2]);
                int s2 = parseInt(header[3]);
                if (s1 <= 0 || s2 <= 0) {
                    System.out.println("Invalid BuildTemplate file format read at" + path + ". \n Header itemSize and abilitySize must be greater than 0");
                    exit(0);
                }
                template = new BuildTemplate(w,h,s1,s2);
            }

            break;
        }






        // -------------------- Data --------------------
        while (true) {
            cmd = reader.readLine();

            if (cmd == null) break;
            if (cmd.equals("")) continue;
            if (cmd.charAt(0) == '#') continue;
            line_counter++;

            String[] data = cmd.split("\\s");
            if (data.length < 2) {
                System.out.println("Invalid BuildTemplate file format read at " + path + " at line " +
                        line_counter + ". \n Data entry must contain an x and y position");
                exit(0);
            }

            int x,y,size;
            ItemType itemType = ItemType.GENERIC;

            x = parseInt(data[0]);
            y = parseInt(data[1]);
            if (x < 0 || y < 0) {
                System.out.println("Invalid BuildTemplate file format read at " + path + ". \n x and y must be positive");
                exit(0);
            }

            if (data.length > 2)
                itemType = ItemType.valueOf(data[2]);



            if (data.length > 3) {
                size = parseInt(data[3]);
                if (size <= 0) {
                    System.out.println("Invalid BuildTemplate file format read at " + path + ". \n size must be greater than 0");
                    exit(0);
                }
                template.addIcon(x,y,size,itemType);
            } else {
                template.addIcon(x,y,itemType);
            }
        }
        reader.close();
        return template;
    }

    public int saveTemplate() {
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView());
        int r = j.showSaveDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            StringBuilder str = new StringBuilder("# ----- Header -----\n");
            str.append(width).append(" ").append(height).append(" ").append(itemSize).append(" ").append(abilitySize);
            str.append("\n# ----- DATA -----\n");
            for (IconView icon : icons) {
                str.append(icon.x).append(" ").append(icon.y).append(" ").append(icon.type.toString());
                if (icon.type == ItemType.ABILITY) {
                    if (icon.size != abilitySize)
                        str.append(" ").append(icon.size);
                } else if (icon.size != itemSize) {
                    str.append(" ").append(icon.size);
                }
                str.append("\n");
            }


            try {
                FileWriter writer = new FileWriter(j.getSelectedFile().getAbsolutePath());
                writer.write(str.toString());
                writer.close();
            } catch (IOException ee) {
                System.out.println("Exception writing to file");
                return JFileChooser.ERROR_OPTION; // -1
            }
        } else if (r == JFileChooser.CANCEL_OPTION) {
            System.out.println("Save cancelled");
        } else {
            System.out.println("Error saving to file: " + j.getSelectedFile().getAbsolutePath());
        }
        return r;
    }
}
