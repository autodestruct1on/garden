package gg.cristalix.growagarden.model.garden.vegetation;

public enum SeedCategory {
    BASE("§2Базовая", "§2", 1.0, 1.0),
    TREE("§6Дерево", "§6", 1.5, 5.0),
    EXOTIC("§5Экзотическая", "§5", 2.0, 10.0);

    private final String displayName;
    private final String colorCode;
    private final double priceMultiplier;
    private final double minPrice;

    SeedCategory(String displayName, String colorCode, double priceMultiplier, double minPrice) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.priceMultiplier = priceMultiplier;
        this.minPrice = minPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public double getMinPrice() {
        return minPrice;
    }
}
