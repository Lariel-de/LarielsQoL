package de.lariel.qualityoflife.shopkeeper;

import net.minecraft.world.item.ItemStack;

public class CurrencyData {

    private final CurrencyType type;

    // Scoreboard
    private final String scoreboardObjective;

    // Item
    private final ItemStack currencyItem;

    // Custom
    private final String customKey;

    public CurrencyData(CurrencyType type, String scoreboardObjective, ItemStack currencyItem, String customKey) {
        this.type = type;
        this.scoreboardObjective = scoreboardObjective;
        this.currencyItem = currencyItem;
        this.customKey = customKey;
    }

    public CurrencyType type() { return type; }
    public String scoreboardObjective() { return scoreboardObjective; }
    public ItemStack currencyItem() { return currencyItem; }
    public String customKey() { return customKey; }
}
