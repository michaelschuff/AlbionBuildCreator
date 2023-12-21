package Display;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class Window extends JFrame {
    public CheckboxMenuItem currItemSelected = null;
    public CheckboxMenuItem currTierSelected = null;
    public CheckboxMenuItem sixPointOne = null;

    public Menu displayCurrItem = null, displayCurrTier = null;
    public Window(BuildSheetData bs) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(bs.width, bs.height + 25);
        setLocationRelativeTo(null);

        add(new BuildSheetView(bs, this));

        initMenu();

        setVisible(true);

    }

    private void initMenu() {
        String[] TopLevels = {"File", "Item: None", "Tier: 6.1"};
        String[][] SubLevels = {
                {"Export","Save","Save Template"},
                {"Warrior","Hunter","Mage","Armor"},
                {"1.0","2.0","3.0","T4","T5","T6","T7","T8"}
        };
        String[][][] SubSubLevels = {
                {{},{}, {}},
                {
                        {"Sword", "Axe", "Mace", "Hammer", "Gloves", "Crossbow", "Shield"},
                        {"Bow", "Dagger", "Spear", "Quarterstaff", "Shapeshifter", "Nature", "Torch"},
                        {"Fire", "Holy", "Arcane", "Frost", "Cursed", "Tome"},
                        {"Cloth Helmet", "Cloth Armor", "Cloth Shoes", "Leather Helmet", "Leather Armor", "Leather Shoes", "Plate Helmet", "Plate Armor", "Plate Shoes"}
                },
                {{},{},{}, {"4.0","4.1","4.2","4.3","4.4"}, {"5.0","5.1","5.2","5.3","5.4"}, {"6.0","6.1","6.2","6.3","6.4"}, {"7.0","7.1","7.2","7.3","7.4"}, {"8.0","8.1","8.2","8.3","8.4"}}
        };
        String[][][][] SubSubSubLevels = {
                {{},{}, {}},
                {
                        {{"Broadsword","Claymore","Dual Swords","Clarent Blade","Carving Sword","Galatine Pair","Kingmaker"},{"Battleaxe","Greataxe","Halberd","Carrioncaller","Infernal Scythe","Bear Paws","Realmbreaker"},{"Mace","Heavy Mace","Morning Star","Bedrock Mace","Incubus Mace","Camlann Mace","Oathkeepers"},{"Hammer","Great Hammer","Polehammer","Tombhammer","Forge Hammers","Grovekeeper","Hand of Justice"},{"Brawler Gloves","Battle Bracers","Spiked Guantlets","Ursine Maulers","Hellfire Hands","Ravenstrike Cestus","Fists of Avalon"},{"Crossbow","Heavy Crossbow","Light Crossbow","Weeping Repeater","Boltcasters","Siegebow","Energy Shaper"},{"Shield","Sarcophagus","Caitiff","Facebreaker","Astral Aegis"}},
                        {{"Bow","Warbow","Longbow","Whispering Bow","Wailing Bow","Bow of Badon","Mistpiercer"},{"Dagger","Dagger Pair","Claws","Bloodletter","Demonfang","Deathgivers","Bridled Fury"},{"Spear","Pike","Glaive","Heron Spear","Spirithunter","Trinity Spear","Daybreaker"},{"Quarterstaff","Iron-clad Staff","Double Bladed Staff","Black Monk Stave","Soulscythe","Staff of Balance","Grailseeker"},{"Prowling Staff","Rootbound Staff","Primal Staff","Bloodmoon Staff","Hellspawn Staff","Earthrune Staff","Lightcaller"},{"Nature Staff","Great Nature Staff","Wild Staff","Druidic Staff","Blight Staff","Rampant Staff","Ironroot Staff"},{"Torch","Mistcaller","Leering Cane","Cryptcandle","Sacred Scepter"}},
                        {{"Fire Staff","Great Fire Staff","Infernal Staff","Wildfire Staff","Brimstone Staff","Blazing Staff","Dawnsong"},{"Holy Staff","Great Holy Staff","Divine Staff","Lifetouch Staff","Fallen Staff","Redemption Staff","Hallowfall"},{"Arcane Staff","Great Arcane Staff","Enigmatic Staff","Witchwork Staff","Occult Staff","Locus","Evensong"},{"Frost Staff","Great Frost Staff","Glacial Staff","Hoarfrost Staff","Icicle Staff","Permafrost Prism","Chillhowl"},{"Cursed Staff","Great Cursed Staff","Demonic Staff","Lifecurse Staff","Cursed Skull","Damnation Staff","Shadowcaller"},{"Tome of Insight","Eye of Secrets","Musiak","Taproot","Celestial Censer"}},
                        {{"Scholar Cowl","Cleric Cowl","Mage Cowl","Royal Cowl","Druid Cowl","Fiend Cowl","Cultist Cowl","Cowl of Purity","Feyscale Hat"},{"Scholar Robe","Cleric Robe","Mage Robe","Royal Robe","Druid Robe","Fiend Robe","Cultist Robe","Robe of Purity","Feyscale Robe"},{"Scholar Sandals","Cleric Sandals","Mage Sandals","Royal Sandals","Druid Sandals","Fiend Sandals","Cultist Sandals","Sandals of Purity","Feyscale Sandals"},{"Mercenary Hood","Hunter Hood","Assassin Hood","Royal Hood","Stalker Hood","Hellion Hood","Specter Hood","Hood of Tenacity","Mistwalker Hood"},{"Mercenary Jacket","Hunter Jacket","Assassin Jacket","Royal Jacket","Stalker Jacket","Hellion Jacket","Specter Jacket","Jacket of Tenacity","Mistwalker Jacket"},{"Mercenary Shoes","Hunter Shoes","Assassin Shoes","Royal Shoes","Stalker Shoes","Hellion Shoes","Specter Shoes","Shoes of Tenacity","Mistwalker Shoes"},{"Soldier Helmet","Knight Helmet","Guardian Helmet","Royal Helmet","Graveguard Helmet","Demon Helmet","Judicator Helmet","Helmet of Valor","Duskweaver Helmet"},{"Soldier Armor","Knight Armor","Guardian Armor","Royal Armor","Graveguard Armor","Demon Armor","Judicator Armor","Armor of Valor","Duskweaver Armor"},{"Soldier Boots","Knight Boots","Guardian Boots","Royal Boots","Graveguard Boots","Demon Boots","Judicator Boots","Boots of Valor","Duskweaver Boots"}}
                },
                {{{}}, {{}},{{}},{{}},{{}},{{}}}
        };
        MenuBar mb = new MenuBar();
        GearListener itemListener = new GearListener();
        TierListener tierListener = new TierListener();

        for (int i = 0; i < TopLevels.length; i++) {
            Menu topLevel = new Menu(TopLevels[i]);
            if (i == 1) {
                displayCurrItem = topLevel;
            } else if (i == 2) {
                displayCurrTier = topLevel;
            }
            for (int j = 0; j < SubLevels[i].length; j++) {
                Menu subLevel = new Menu(SubLevels[i][j]);
                CheckboxMenuItem chxSubLevel = new CheckboxMenuItem(SubLevels[i][j]);
                if (i == 1) {
                    for (int k = 0; k < SubSubLevels[i][j].length; k++) {
                        Menu SubSubLevel = new Menu(SubSubLevels[i][j][k]);
                        for (int l = 0; l < SubSubSubLevels[i][j][k].length; l++) {
                            CheckboxMenuItem SubSubSubLevel = new CheckboxMenuItem(SubSubSubLevels[i][j][k][l]);
                            SubSubSubLevel.addItemListener(itemListener);
                            SubSubLevel.add(SubSubSubLevel);
                        }
                        subLevel.add(SubSubLevel);
                    }
                    topLevel.add(subLevel);
                } else if (i == 2 && j < 3) {

                    chxSubLevel.addItemListener(tierListener);
                    topLevel.add(chxSubLevel);
                } else {
                    for (int k = 0; k < SubSubLevels[i][j].length; k++) {
                        CheckboxMenuItem chxSubSubLevel = new CheckboxMenuItem(SubSubLevels[i][j][k],j == 5 && k == 1);
                        if (j == 5 && k == 1) {
                            sixPointOne = chxSubSubLevel;
                            currTierSelected = sixPointOne;
                        }
                        chxSubSubLevel.addItemListener(tierListener);
                        subLevel.add(chxSubSubLevel);
                    }
                    topLevel.add(subLevel);
                }

            }
            mb.add(topLevel);
        }
        setMenuBar(mb);
    }

    class GearListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (currItemSelected == null) {
                currItemSelected = (CheckboxMenuItem) e.getSource();
                displayCurrItem.setLabel("Item: " + e.getItem().toString());
            } else if (currItemSelected.equals(e.getSource())) {
                currItemSelected.setState(false);
                currItemSelected = null;
                displayCurrItem.setLabel("Item: None");
            } else {
                currItemSelected.setState(false);
                currItemSelected = (CheckboxMenuItem) e.getSource();
                displayCurrItem.setLabel("Item: " + e.getItem().toString());
            }
        }
    }
    class TierListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (currTierSelected == null) {
                currTierSelected = (CheckboxMenuItem) e.getSource();
                displayCurrTier.setLabel("Tier: " + e.getItem().toString());
            } else if (currTierSelected.equals(e.getSource())) {
                currTierSelected.setState(false);
                currTierSelected = sixPointOne;
                currTierSelected.setState(true);
                displayCurrItem.setLabel("Tier: " + currItemSelected.getLabel());
            } else {
                currTierSelected.setState(false);
                currTierSelected = (CheckboxMenuItem) e.getSource();
                displayCurrTier.setLabel("Tier: " + e.getItem().toString());
            }
        }
    }
}
