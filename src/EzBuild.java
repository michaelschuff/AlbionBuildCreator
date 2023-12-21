import Display.BuildSheetData;
import Display.Window;
import Items.Tier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static util.ResourcePath.genResourcePath;


public class EzBuild {
    public static Tier defaultTier = Tier.T61;
    public static final String templatePrompt = """
                Which template would you like to load?
                1  AbilityBuild""";
    public static final String helpString = """
            To build a sheet, use the following command:
            build sheet weapon offhand head armor feet cape bag food potion abcd efg hi jk swap1 swap2 swap3 swap4
            """;

    public static final String[] templates = new String[] {
            "AbilityBuild"
    };



    BufferedReader reader;
    public EzBuild() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getTemplate() throws IOException {
        String template;
        System.out.println(templatePrompt);
        while (true) {
            String cmd = reader.readLine();
            int i = parseInt(cmd);
            if (0 < i && i <= templates.length) {
                template = templates[i-1];
                break;
            } else {
                System.out.println("Please enter a valid input.");
            }
        }
        return template;
    }
    public void run() {
        genResourcePath();

//        String t = getTemplate();
        String t = "AbilityBuild";

        BuildSheetData bs = new BuildSheetData();
        Window window = new Window(bs);
    }
}
