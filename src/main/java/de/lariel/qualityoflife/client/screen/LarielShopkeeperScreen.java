package de.lariel.qualityoflife.client.screen;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import com.pixelmonmod.pixelmon.client.gui.Resources;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.gui.npc.ShopkeeperScreen;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.storage.ClientData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LarielShopkeeperScreen extends ShopkeeperScreen {
    private static final int ENOUGH_CURRENCY_COLOR = 14540253;
    private static final int NOT_ENOUGH_CURRENCY_COLOR = 16729156;
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

    private float incrementInterval = 8.0F;

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

        for (var item : shopItems) {
            buyItems.add(item.shopItem());
        }

        larielItems = shopItems;
    }

    private static List<ShopItem> extractPixelmonItems(List<LarielShopItem> items) {
        List<ShopItem> pixelmonItems = new ArrayList<>();
        for (var item : items) {
            pixelmonItems.add(item.shopItem());
        }

        return pixelmonItems;
    }

    @Override
    protected void init() {
        super.init();

        this.SCREEN_X_CENTER = this.width / 2;
        this.SCREEN_Y_CENTER = this.height / 2;
        var var10001 = this.SCREEN_X_CENTER;
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
        var moneyLabel = I18n.get("gui.shopkeeper.money");
        var playerMoneyLabel = String.valueOf(ClientData.playerMoney);
        var MONEY_LABEL_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(moneyLabel) / 2;
        var POKE_DOLLAR_LABEL_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(playerMoneyLabel + "8") / 2;
        var PLAYER_MONEY_LEFT_EDGE = this.BUY_SCREEN_LEFT_EDGE + 158 - this.minecraft.font.width(playerMoneyLabel + "8") / 2 + 8;
        var MONEY_LABEL_Y = this.BUY_SCREEN_TOP_EDGE + 12;
        var POKE_DOLLAR_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 25;
        var PLAYER_MONEY_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 26;
        graphics.drawString(this.minecraft.font, moneyLabel, MONEY_LABEL_LEFT_EDGE, MONEY_LABEL_Y, 16777215);
        ScreenHelper.drawImageQuad(Resources.pokedollar, graphics, (float) POKE_DOLLAR_LABEL_LEFT_EDGE, (float) POKE_DOLLAR_TOP_EDGE, 6.0F, 9.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
        graphics.drawString(this.minecraft.font, playerMoneyLabel, PLAYER_MONEY_LEFT_EDGE, PLAYER_MONEY_TOP_EDGE, 16777215);
    }

    @Override
    protected void renderCost(GuiGraphics graphics, int i, List<ShopItem> listItems, float topLimit, String cost, int costWidth) {
        var larielItem = larielItems.get(i);
        var color = ENOUGH_CURRENCY_COLOR;
        var player = Minecraft.getInstance().player;
        var ICON_X = this.LIST_LEFT_EDGE + 140;
        var ICON_Y = (int) (topLimit + 6);
        var TEXT_X = ICON_X + 10;
        var TEXT_Y = (int) (topLimit + 7);

        if (player == null) return;

        if (!playerHasEnoughCurrency(larielItem, player, 1))
            color = NOT_ENOUGH_CURRENCY_COLOR;

        switch (larielItem.currencyData().type()) {
            case POKEDOLLAR -> ScreenHelper.drawImageQuad(Resources.pokedollar, graphics, ICON_X, ICON_Y - 1, 6, 9, 0,
                    0, 1, 1, 1, 1, 1, 1, 0);
            case SCOREBOARD ->
                    ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, larielItem.currencyData().customKey(), false,
                            16, ICON_X - 10, TEXT_Y, ENOUGH_CURRENCY_COLOR, true);
            case ITEM -> graphics.renderItem(larielItem.currencyData().currencyItem(), ICON_X - 6, ICON_Y - 4);
            case CUSTOM -> {
                // do nothing -> NYI
            }
        }

        ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, String.valueOf(larielItem.price()), false,
                20, TEXT_X, TEXT_Y, color, true);
    }

    @Override
    protected void renderMiniScreenPrice(GuiGraphics graphics, double price) {
        var larielItem = larielItems.get(selectedItem);
        var player = Minecraft.getInstance().player;
        String priceLabel = I18n.get("gui.shopkeeper.price");
        String priceAmount = "" + (double) this.quantity * larielItem.price();
        int PRICE_LABEL_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 30 - this.minecraft.font.width(priceLabel) / 2;
        int PRICE_LABEL_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 6;
        int POKE_DOLLAR_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 29 - (this.minecraft.font.width(priceAmount) + 8) / 2 - 4;
        int POKE_DOLLAR_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 18;
        int PRICE_AMOUNT_LEFT_EDGE = this.MINI_SCREEN_LEFT_EDGE + 30 - (this.minecraft.font.width(priceAmount) + 8) / 2 + 4;
        int PRICE_AMOUNT_TOP_EDGE = this.BUY_SCREEN_TOP_EDGE + 18;
        int colour = 14540253;

        if (larielItem.currencyData().type() == CurrencyType.POKEDOLLAR || player == null) {
            super.renderMiniScreenPrice(graphics, price);

            return;
        }
        graphics.drawString(this.minecraft.font, priceLabel, PRICE_LABEL_LEFT_EDGE, PRICE_LABEL_TOP_EDGE, 16777215);

        switch (larielItem.currencyData().type()) {
            case POKEDOLLAR -> { /* Is already handled */}
            case SCOREBOARD ->
                    ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, larielItem.currencyData().customKey(), false,
                            16, (float) POKE_DOLLAR_LEFT_EDGE - 10, (float) POKE_DOLLAR_TOP_EDGE, ENOUGH_CURRENCY_COLOR, true);
            case ITEM ->
                    graphics.renderItem(larielItem.currencyData().currencyItem(), POKE_DOLLAR_LEFT_EDGE - 8, POKE_DOLLAR_TOP_EDGE - 4);
            case CUSTOM -> { /* do nothing -> NYI */ }
        }

        if (!playerHasEnoughCurrency(larielItem, player, quantity)) {
            colour = NOT_ENOUGH_CURRENCY_COLOR;
        }

        graphics.drawString(this.font, priceAmount, PRICE_AMOUNT_LEFT_EDGE, PRICE_AMOUNT_TOP_EDGE, colour, true);
    }

    @Override
    protected void renderMiniScreenBuySellButton(GuiGraphics graphics, int mouseX, int mouseY, double price, List<ShopItem> listItems) {
        var larielShopItem = larielItems.get(selectedItem);
        var player = Minecraft.getInstance().player;

        if (player == null) return;

        String buyLabel = I18n.get(this.currentTab == EnumBuySell.Buy ? "gui.shopkeeper.buy" : "gui.shopkeeper.sell", new Object[0]);
        float MINI_SCREEN_BUY_LABEL_LEFT_EDGE = (float) (this.MINI_SCREEN_LEFT_EDGE + 30) - (float) Minecraft.getInstance().font.width(buyLabel) / 2.0F;
        float MINI_SCREEN_BUY_LABEL_TOP_EDGE = (float) (this.BUY_SCREEN_TOP_EDGE + 78);
        int colour = 16777215;
        boolean validTransaction = playerHasEnoughCurrency(larielShopItem, player, quantity);
        if (mouseX > this.BUY_BUTTON_LEFT_EDGE && mouseX < this.BUY_BUTTON_RIGHT_EDGE && mouseY > this.BUY_BUTTON_TOP_EDGE && mouseY < this.BUY_BUTTON_BOTTOM_EDGE) {
            if (validTransaction) {
                ScreenHelper.drawImageQuad(Resources.shopkeeper, graphics, (float) this.BUY_BUTTON_LEFT_EDGE, (float) this.BUY_BUTTON_TOP_EDGE, 49.0F, 18.0F, 0.01953125F, 0.796875F, 0.2109375F, 0.8671875F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
            }
        }

        if (this.currentTab == EnumBuySell.Buy && !validTransaction) {
            colour = 7829367;
        }

        graphics.drawString(this.font, buyLabel, MINI_SCREEN_BUY_LABEL_LEFT_EDGE, MINI_SCREEN_BUY_LABEL_TOP_EDGE, colour, true);
    }

    @Override
    protected void clickBuyMiniScreen(double mouseX, double mouseY, boolean isInstant) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        if (!this.isBuyMiniScreenVisible()) return;

        var larielItem = larielItems.get(this.selectedItem);
        int maxAffordable = playerHasEnoughCurrency(larielItem, player, 1)
                ? getMaxAffordableQuantity(larielItem, player)
                : 0;

        int maxStackable = checkRemainingSlots(larielItem.shopItem().itemStack());
        int maxBuyable = Math.min(maxAffordable, maxStackable);

        // --- UP ARROW ---
        if (mouseX > ARROW_BUTTON_LEFT_EDGE && mouseX < ARROW_BUTTON_RIGHT_EDGE &&
                mouseY > UP_ARROW_BUTTON_TOP_EDGE && mouseY < UP_ARROW_BUTTON_BOTTOM_EDGE) {

            int newQuantity = this.quantity + 1;

            if (playerHasEnoughCurrency(larielItem, player, newQuantity)
                    && newQuantity <= checkRemainingSlots(larielItem.shopItem().itemStack())) {

                this.quantity = newQuantity;
            } else {
                this.quantity = 1;
            }

            this.floatQuantity = 2.0F;
            this.decreaseIncrementInterval();
        }

        // --- DOWN ARROW ---
        if (mouseX > ARROW_BUTTON_LEFT_EDGE && mouseX < ARROW_BUTTON_RIGHT_EDGE &&
                mouseY > DOWN_ARROW_BUTTON_TOP_EDGE && mouseY < DOWN_ARROW_BUTTON_BOTTOM_EDGE) {

            if (maxBuyable == 0) {
                this.quantity = 1;
                this.floatQuantity = 2.0F;
                this.decreaseIncrementInterval();
                return;
            }

            if (this.quantity > 1) {
                this.quantity--;
                this.floatQuantity = 2.0F;
                this.decreaseIncrementInterval();
                return;
            }

            this.quantity = maxBuyable;
            this.floatQuantity = 2.0F;
            this.decreaseIncrementInterval();
        }

        // --- BUY BUTTON ---
        if (mouseX > BUY_BUTTON_LEFT_EDGE && mouseX < BUY_BUTTON_RIGHT_EDGE &&
                mouseY > BUY_BUTTON_TOP_EDGE && mouseY < BUY_BUTTON_BOTTOM_EDGE) {

            if (playerHasEnoughCurrency(larielItem, player, this.quantity)) {
                this.sendBuyPacket();
                this.selectedItem = -1;
            }
        }

        if (isInstant) {
            this.incrementInterval = 8.0F;
        }
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

    private boolean playerHasEnoughCurrency(LarielShopItem item, LocalPlayer player, int quantity) {
        return switch (item.currencyData().type()) {
            case POKEDOLLAR -> ClientData.playerMoney.doubleValue() >= item.price() * quantity;

            case SCOREBOARD -> {
                var scoreboard = player.getScoreboard();
                var objective = scoreboard.getObjective(item.currencyData().customKey());
                if (objective == null) yield false;
                int score = scoreboard.getOrCreatePlayerScore(player, objective).get();
                yield score >= item.price() * quantity;
            }

            case ITEM -> {
                int count = player.getInventory().countItem(item.currencyData().currencyItem().getItem());
                yield count >= item.price() * quantity;
            }

            case CUSTOM -> false;
        };
    }

    private int checkRemainingSlots(ItemStack buying) {
        if (buying != null && buying != ItemStack.EMPTY && buying.getItem() != Items.AIR) {
            try {
                LocalPlayer player = Minecraft.getInstance().player;
                int available = 0;

                for (var curStack : player.getInventory().items) {
                    if (curStack != null && ItemStack.isSameItem(curStack, buying)) {
                        available += buying.getMaxStackSize() - curStack.getCount();
                    } else if (curStack == null || curStack == ItemStack.EMPTY || curStack.getItem() == Items.AIR) {
                        available += buying.getMaxStackSize();
                    }
                }

                return Math.min(2304, available);
            } catch (Throwable t) {
                t.printStackTrace();
                return 2304;
            }
        } else {
            return 0;
        }
    }

    private void decreaseIncrementInterval() {
        this.incrementInterval = Math.max(0.5F, this.incrementInterval * 0.75F);
    }

    private int getMaxAffordableQuantity(LarielShopItem item, LocalPlayer player) {
        int price = item.price();

        return switch (item.currencyData().type()) {
            case POKEDOLLAR -> (int)(ClientData.playerMoney.doubleValue() / price);

            case SCOREBOARD -> {
                var scoreboard = player.getScoreboard();
                var objective = scoreboard.getObjective(item.currencyData().customKey());
                if (objective == null) yield 0;
                int score = scoreboard.getOrCreatePlayerScore(player, objective).get();
                yield score / price;
            }

            case ITEM -> {
                int count = player.getInventory().countItem(item.currencyData().currencyItem().getItem());
                yield count / price;
            }

            case CUSTOM -> 0;
        };
    }
}