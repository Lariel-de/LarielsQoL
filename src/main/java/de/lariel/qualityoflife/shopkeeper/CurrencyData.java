package de.lariel.qualityoflife.shopkeeper;

import net.minecraft.world.item.ItemStack;

public class CurrencyData {

    private final CurrencyType type;
    private final ItemStack currencyItem;
    private final String customKey;

    public CurrencyData(String scoreboardObjective) {
        this.type = CurrencyType.SCOREBOARD;
        this.customKey = scoreboardObjective;
        this.currencyItem = null;
    }

    public CurrencyData(ItemStack currencyItem) {
        this.type = CurrencyType.ITEM;
        this.currencyItem = currencyItem;
        this.customKey = null;
    }

    public CurrencyData(String customKey, CurrencyType type) {
        this.type = CurrencyType.SCOREBOARD;
        this.customKey = customKey;
        this.currencyItem = null;
    }

    public CurrencyData() {
        this.type = CurrencyType.POKEDOLLAR;
        this.customKey = null;
        this.currencyItem = null;
    }

    public CurrencyType type() {
        return type;
    }

    public ItemStack currencyItem() {
        return currencyItem;
    }

    public String customKey() {
        return customKey;
    }
}
