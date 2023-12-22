package ezbuild;

import ezbuild.builder.BuildSheetData;
import ezbuild.builder.BuildSheetView;

import javax.swing.*;
import java.awt.*;

public class EzBuildWindow extends JFrame {
    public static Point pos = null;
    public BuildSheetView v;

    public BuildMenu bm;

    public EzBuildWindow(BuildSheetData bs) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(bs.width, bs.height + 25);

        if (pos == null) {
            setLocationRelativeTo(null);
        } else {
            setLocation(pos);
            pos.translate(30,30);
        }



        v = new BuildSheetView(bs, this);
        add(v);

        bm = new BuildMenu(v);
        setMenuBar(bm.mb);

        setVisible(true);

    }
}
