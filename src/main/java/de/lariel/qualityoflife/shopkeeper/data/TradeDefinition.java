package de.lariel.qualityoflife.shopkeeper.data;

import com.google.gson.JsonElement;

public class TradeDefinition {
    public String item;
    public JsonElement nbt;
    public int price;
    public CurrencyJson currency;
    public double chance;
    public int Xp;
    public int maxSellCountPerDay = -1;
}