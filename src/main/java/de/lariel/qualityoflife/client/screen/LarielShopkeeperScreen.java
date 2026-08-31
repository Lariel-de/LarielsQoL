package de.lariel.qualityoflife.client.screen;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import com.pixelmonmod.pixelmon.client.gui.Resources;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.gui.npc.ShopkeeperScreen;
import com.pixelmonmod.pixelmon.storage.ClientData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LarielShopkeeperScreen extends ShopkeeperScreen {
    private final int BUY_SCREEN_WIDTH;
    private final int BUY_SCREEN_HEIGHT;
    private final int TAB_WIDTH;
    private final int TAB_HEIGHT;
    private final float BUY_SCREEN_IMAGE_US;
    private final float BUY_SCREEN_IMAGE_VE;
    private final int LIST_ITEM_WIDTH;
    private final int LIST_ITEM_HEIGHT;
    private final int EXIT_BUTTON_WIDTH;
    private final int EXIT_BUTTON_HEIGHT;
    private final int BUY_SCREEN_TOP_EDGE_PADDING;
    private final int MINI_SCREEN_WIDTH;
    private final int MINI_SCREEN_HEIGHT;

    private final List<LarielShopItem> larielItems;

    private int SCREEN_X_CENTER;
    private int SCREEN_Y_CENTER;
    private int BUY_SCREEN_LEFT_EDGE;
    private int BUY_SCREEN_TOP_EDGE;
    private int TAB_TOP_EDGES;
    private int BUY_TAB_LEFT_EDGE;
    private int SELL_TAB_LEFT_EDGE;
    private int LIST_LEFT_EDGE;
    private int LIST_TOP_EDGE;
    private int ARROW_LEFT_EDGE;
    private int ARROW_RIGHT_EDGE;
    private int UP_ARROW_TOP_EDGE;
    private int UP_ARROW_BOTTOM_EDGE;
    private int DOWN_ARROW_TOP_EDGE;
    private int DOWN_ARROW_BOTTOM_EDGE;
    private int EXIT_BUTTON_LEFT_EDGE;
    private int EXIT_BUTTON_TOP_EDGE;
    private int EXIT_BUTTON_RIGHT_EDGE;
    private int EXIT_BUTTON_BOTTOM_EDGE;
    private int MINI_SCREEN_LEFT_EDGE;
    private int ARROW_BUTTON_LEFT_EDGE;
    private int ARROW_BUTTON_RIGHT_EDGE;
    private int UP_ARROW_BUTTON_TOP_EDGE;
    private int UP_ARROW_BUTTON_BOTTOM_EDGE;
    private int DOWN_ARROW_BUTTON_TOP_EDGE;
    private int DOWN_ARROW_BUTTON_BOTTOM_EDGE;
    private int ARROW_IMAGE_LEFT_EDGE;
    private int UP_ARROW_IMAGE_TOP_EDGE;
    private int DOWN_ARROW_IMAGE_TOP_EDGE;
    private int BUY_BUTTON_LEFT_EDGE;
    private int BUY_BUTTON_RIGHT_EDGE;
    private int BUY_BUTTON_TOP_EDGE;
    private int BUY_BUTTON_BOTTOM_EDGE;

    public LarielShopkeeperScreen(List<LarielShopItem> shopItems, boolean sellable) {
        super(extractPixelmonItems(shopItems), sellable);

        this.BUY_SCREEN_WIDTH = 197;
        this.BUY_SCREEN_HEIGHT = 201;
        this.TAB_WIDTH = 58;
        this.TAB_HEIGHT = 30;
        this.BUY_SCREEN_IMAGE_US = 0.23046875F;
        this.BUY_SCREEN_IMAGE_VE = 0.78515625F;
        this.LIST_ITEM_WIDTH = 173;
        this.LIST_ITEM_HEIGHT = 21;
        this.EXIT_BUTTON_WIDTH = 14;
        this.EXIT_BUTTON_HEIGHT = 17;
        this.BUY_SCREEN_TOP_EDGE_PADDING = -4;
        this.MINI_SCREEN_WIDTH = 59;
        this.MINI_SCREEN_HEIGHT = 97;

        buyItems.clear();

        for (LarielShopItem item : shopItems) {
            buyItems.add(item.shopItem());
        }

        larielItems = shopItems;
    }

    private static List<ShopItem> extractPixelmonItems(List<LarielShopItem> items) {
        List<ShopItem> pixelmonItems = new ArrayList<>();
        for (LarielShopItem item : items) {
            pixelmonItems.add(item.shopItem());
        }

        return pixelmonItems;
    }

    @Override
    protected void init() {
        super.init();

        this.SCREEN_X_CENTER = this.width / 2;
        this.SCREEN_Y_CENTER = this.height / 2;
        int var10001 = this.SCREEN_X_CENTER;
        Objects.requireNonNull(this);
        this.BUY_SCREEN_LEFT_EDGE = var10001 + 197 / -2;
        var10001 = this.SCREEN_Y_CENTER;
        Objects.requireNonNull(this);
        var10001 += 201 / -2;
        Objects.requireNonNull(this);
        this.BUY_SCREEN_TOP_EDGE = var10001 + -4;
        this.TAB_TOP_EDGES = this.BUY_SCREEN_TOP_EDGE + 12;
        this.BUY_TAB_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 9;
        var10001 = this.BUY_TAB_LEFT_EDGE;
        Objects.requireNonNull(this);
        this.SELL_TAB_LEFT_EDGE = var10001 + 58 + 1;
        this.LIST_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 12;
        this.LIST_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 53;
        this.ARROW_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 90;
        this.ARROW_RIGHT_EDGE = this.ARROW_LEFT_EDGE + 17;
        this.UP_ARROW_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 41;
        this.UP_ARROW_BOTTOM_EDGE = this.UP_ARROW_TOP_EDGE + 10;
        this.DOWN_ARROW_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 180;
        this.DOWN_ARROW_BOTTOM_EDGE = this.DOWN_ARROW_TOP_EDGE + 10;
        this.EXIT_BUTTON_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 179;
        this.EXIT_BUTTON_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 181;
        var10001 = this.EXIT_BUTTON_LEFT_EDGE;
        Objects.requireNonNull(this);
        this.EXIT_BUTTON_RIGHT_EDGE = var10001 + 14;
        var10001 = this.EXIT_BUTTON_TOP_EDGE;
        Objects.requireNonNull(this);
        this.EXIT_BUTTON_BOTTOM_EDGE = var10001 + 17;
        var10001 = this.BUY_SCREEN_LEFT_EDGE;
        Objects.requireNonNull(this);
        this.MINI_SCREEN_LEFT_EDGE = var10001 + 197 + 8;
        this.ARROW_BUTTON_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 27;
        this.ARROW_BUTTON_RIGHT_EDGE = this.ARROW_BUTTON_LEFT_EDGE + 25;
        this.UP_ARROW_BUTTON_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 45;
        this.UP_ARROW_BUTTON_BOTTOM_EDGE = this.UP_ARROW_BUTTON_TOP_EDGE + 7;
        this.DOWN_ARROW_BUTTON_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 60;
        this.DOWN_ARROW_BUTTON_BOTTOM_EDGE = this.DOWN_ARROW_BUTTON_TOP_EDGE + 7;
        this.ARROW_IMAGE_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 36;
        this.UP_ARROW_IMAGE_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 44;
        this.DOWN_ARROW_IMAGE_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 60;
        this.BUY_BUTTON_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 5;
        this.BUY_BUTTON_RIGHT_EDGE = this.BUY_BUTTON_LEFT_EDGE + 49;
        this.BUY_BUTTON_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 73;
        this.BUY_BUTTON_BOTTOM_EDGE = this.BUY_BUTTON_TOP_EDGE + 18;
    }

    @Override
    protected void renderPlayerMoney(GuiGraphics graphics) {
        String moneyLabel = I18n.get("gui.shopkeeper.money");
        String playerMoneyLabel = String.valueOf(ClientData.playerMoney);
        int MONEY_LABEL_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(moneyLabel) / 2;
        int POKE_DOLLAR_LABEL_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(playerMoneyLabel + "8") / 2;
        int PLAYER_MONEY_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(playerMoneyLabel + "8") / 2 + 8;
        int MONEY_LABEL_Y = this.BUY_SCREEN_TOP_EDGE + 12;
        int POKE_DOLLAR_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 25;
        int PLAYER_MONEY_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 26;
        graphics.drawString(this.minecraft.font, moneyLabel, MONEY_LABEL_LEFT_EDGE, MONEY_LABEL_Y, 16777215);
        ScreenHelper.drawImageQuad(Resources.pokedollar, graphics, (float) POKE_DOLLAR_LABEL_LEFT_EDGE, (float) POKE_DOLLAR_TOP_EDGE, 6.0F, 9.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
        graphics.drawString(this.minecraft.font, playerMoneyLabel, PLAYER_MONEY_LEFT_EDGE, PLAYER_MONEY_TOP_EDGE, 16777215);
    }

    @Override
    protected void renderCost(GuiGraphics graphics, int i, List<ShopItem> listItems, float topLimit, String cost, int costWidth) {
        var larielItem = larielItems.get(i);
        int colour = 14540253;
        var player = Minecraft.getInstance().player;

        if (player == null) return;

        switch (larielItem.currencyData().type()) {
            case POKEDOLLAR -> {
                if (larielItem.shopItem().buyPrice() > ClientData.playerMoney.doubleValue())
                    colour = 16729156;
            }
            case SCOREBOARD -> {
                var scoreboard = player.getScoreboard();
                var objective = scoreboard.getObjective(larielItem.currencyData().customKey());

                if (objective == null) {
                    colour = 16729156;
                    break;
                }

                var coins = scoreboard.getOrCreatePlayerScore(player, objective).get();

                if (coins < larielItem.price())
                    colour = 16729156;
            }
            case ITEM -> {
                int currencyItem = player.getInventory().countItem(larielItem.currencyData().currencyItem().getItem());
                if (currencyItem < larielItem.price())
                    colour = 16729156;
            }
            case CUSTOM -> {
                // do nothing -> NYI
            }
        }

        int ICON_X = this.LIST_LEFT_EDGE + 140;
        int ICON_Y = (int) (topLimit + 6);

        if (larielItem.currencyData().type() == CurrencyType.ITEM) {
            graphics.renderItem(larielItem.currencyData().currencyItem(), ICON_X - 6, ICON_Y - 4);
        } else {
            ScreenHelper.drawImageQuad(Resources.pokedollar, graphics, ICON_X, ICON_Y - 1, 6, 9, 0, 0, 1, 1, 1, 1, 1, 1, 0);
        }

        int TEXT_X = ICON_X + 10;
        int TEXT_Y = (int) (topLimit + 7);

        ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, String.valueOf(larielItem.price()), false,
                9999, TEXT_X, TEXT_Y, colour, false);
    }

    @Override
    protected void sendBuyPacket() {
        // ToDo: Send own packages
//        NetworkHelper.sendToServer(new ShopTransactionPacket(true, ((ShopItem)this.buyItems.get(this.selectedItem)).uuid(), this.quantity));
    }

    @Override
    protected void sendSellPacket() {
        // ToDo: Send own packages
//        NetworkHelper.sendToServer(new ShopTransactionPacket(false, ((ShopItem)this.sellItems.get(this.selectedItem)).uuid(), this.quantity));
    }
}