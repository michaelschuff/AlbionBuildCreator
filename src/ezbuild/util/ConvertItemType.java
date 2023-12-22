package ezbuild.util;

public final class ConvertItemType {
    public static ItemType fromString(String s) {
        return switch (s.toLowerCase()) {
            case "mainhand" -> ItemType.MAINHAND;
            case "offhand" -> ItemType.OFFHAND;
            case "head" -> ItemType.HEAD;
            case "feet" -> ItemType.FEET;
            case "chest" -> ItemType.CHEST;
            case "bag" -> ItemType.BAG;
            case "cape" -> ItemType.CAPE;
            case "potion" -> ItemType.POTION;
            case "food" -> ItemType.FOOD;
            case "mount" -> ItemType.MOUNT;
            case "ability" -> ItemType.ABILITY;
//            case "generic" -> main.util.ItemType.GENERIC;
            default -> ItemType.GENERIC;
        };
    }
}
