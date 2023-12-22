package ezbuild;

import ezbuild.builder.BuildSheetData;



public class EzBuild {
    public EzBuild() { }

    public void run() {

        BuildSheetData bs = new BuildSheetData("resources/examples/templates/DetailedTemplate.tmp", "resources/examples/examplebackground.png");
        EzBuildWindow window = new EzBuildWindow(bs);
    }

}
