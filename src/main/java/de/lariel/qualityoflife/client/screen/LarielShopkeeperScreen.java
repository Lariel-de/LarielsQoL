package de.lariel.qualityoflife.client.screen;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import com.pixelmonmod.pixelmon.client.gui.Resources;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.gui.npc.ShopkeeperScreen;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.storage.ClientData;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.LarielShopTransactionPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.reputation.LarielPlayerReputationStoreManager;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopPurchaseStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class LarielShopkeeperScreen extends ShopkeeperScreen {
    private static final int ENOUGH_CURRENCY_COLOR = 14540253;
    private static final int NOT_ENOUGH_CURRENCY_COLOR = 16729156;
    private final List<LarielShopItem> larielItems;
    private final ResourceLocation shopkeeperId;
    private int ARROW_BUTTON_LEFT_EDGE;
    private int ARROW_BUTTON_RIGHT_EDGE;
    private int UP_ARROW_BUTTON_TOP_EDGE;
    private int UP_ARROW_BUTTON_BOTTOM_EDGE;
    private int DOWN_ARROW_BUTTON_TOP_EDGE;
    private int DOWN_ARROW_BUTTON_BOTTOM_EDGE;
    private int BUY_BUTTON_RIGHT_EDGE;
    private int BUY_BUTTON_TOP_EDGE;
    private int BUY_BUTTON_BOTTOM_EDGE;

    public LarielShopkeeperScreen(ResourceLocation shopkeeperId, List<LarielShopItem> shopItems, boolean sellable) {
        super(extractPixelmonItems(shopItems), sellable);
        this.shopkeeperId = shopkeeperId;

        buyItems.clear();

        for (var item : shopItems) {
            buyItems.add(item.getShopItem());
        }

        larielItems = shopItems;
    }

    private static List<ShopItem> extractPixelmonItems(List<LarielShopItem> items) {
        List<ShopItem> pixelmonItems = new ArrayList<>();
        for (var item : items) {
            pixelmonItems.add(item.getShopItem());
        }

        return pixelmonItems;
    }

    @Override
    protected void init() {
        super.init();

        var buyScreenTop = this.height / 2 - this.getBuyScreenHeight() / 2 + this.getBuyScreenTopEdgePadding();

        this.ARROW_BUTTON_LEFT_EDGE = miniLeft() + 27;
        this.ARROW_BUTTON_RIGHT_EDGE = this.ARROW_BUTTON_LEFT_EDGE + 25;

        this.UP_ARROW_BUTTON_TOP_EDGE = buyScreenTop + 45;
        this.UP_ARROW_BUTTON_BOTTOM_EDGE = this.UP_ARROW_BUTTON_TOP_EDGE + 7;

        this.DOWN_ARROW_BUTTON_TOP_EDGE = buyScreenTop + 60;
        this.DOWN_ARROW_BUTTON_BOTTOM_EDGE = this.DOWN_ARROW_BUTTON_TOP_EDGE + 7;

        this.BUY_BUTTON_RIGHT_EDGE = buyButtonLeft() + 49;
        this.BUY_BUTTON_TOP_EDGE = buyScreenTop + 73;
        this.BUY_BUTTON_BOTTOM_EDGE = this.BUY_BUTTON_TOP_EDGE + 18;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);

        renderReputation(graphics);
    }

    @Override
    protected void renderCost(GuiGraphics graphics, int i, List<ShopItem> listItems, float topLimit, String cost, int costWidth) {
        var larielItem = larielItems.get(i);
        var color = ENOUGH_CURRENCY_COLOR;
        var player = Minecraft.getInstance().player;
        final var ICON_X = listLeft() + 140;
        final var ICON_Y = (int) (topLimit + 6);
        final var TEXT_X = ICON_X + 10;
        final var TEXT_Y = ICON_Y + 1;

        if (player == null) return;

        if (getMaxBuyable(larielItem, player) < 1) {
            color = NOT_ENOUGH_CURRENCY_COLOR;
        }

        switch (larielItem.getCurrencyData().type()) {
            case POKEDOLLAR -> ScreenHelper.drawImageQuad(Resources.pokedollar, graphics, ICON_X, ICON_Y - 1, 6, 9, 0,
                    0, 1, 1, 1, 1, 1, 1, 0);
            case SCOREBOARD ->
                    ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, larielItem.getCurrencyData().customKey(), false,
                            16, ICON_X - 10, TEXT_Y, ENOUGH_CURRENCY_COLOR, true);
            case ITEM -> graphics.renderItem(larielItem.getCurrencyData().currencyItem(), ICON_X - 6, ICON_Y - 4);
            case CUSTOM -> {
                // do nothing -> NYI
            }
        }

        ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, String.valueOf(larielItem.getPrice()), false,
                20, TEXT_X, TEXT_Y, color, true);
    }

    @Override
    protected void renderMiniScreenPrice(GuiGraphics graphics, double price) {
        var larielItem = larielItems.get(selectedItem);
        var player = Minecraft.getInstance().player;
        var priceLabel = I18n.get("gui.shopkeeper.price");
        var priceAmount = String.valueOf(this.quantity * larielItem.getPrice());
        var PRICE_LABEL_LEFT_EDGE = miniLeft() + 30 - Minecraft.getInstance().font.width(priceLabel) / 2;
        var PRICE_LABEL_TOP_EDGE = buyScreenTop() + 6;
        var POKE_DOLLAR_LEFT_EDGE = miniLeft() + 29 - (Minecraft.getInstance().font.width(priceAmount) + 8) / 2 - 4;
        var POKE_DOLLAR_TOP_EDGE = buyScreenTop() + 18;
        var PRICE_AMOUNT_LEFT_EDGE = miniLeft() + 30 - (Minecraft.getInstance().font.width(priceAmount) + 8) / 2 + 4;
        var PRICE_AMOUNT_TOP_EDGE = buyScreenTop() + 18;
        var colour = 14540253;

        if (larielItem.getCurrencyData().type() == CurrencyType.POKEDOLLAR || player == null) {
            super.renderMiniScreenPrice(graphics, price);

            return;
        }
        graphics.drawString(Minecraft.getInstance().font, priceLabel, PRICE_LABEL_LEFT_EDGE, PRICE_LABEL_TOP_EDGE, 16777215);

        switch (larielItem.getCurrencyData().type()) {
            case POKEDOLLAR -> { /* Is already handled */}
            case SCOREBOARD ->
                    ScreenHelper.drawSquashedString(graphics, Minecraft.getInstance().font, larielItem.getCurrencyData().customKey(), false,
                            16, (float) POKE_DOLLAR_LEFT_EDGE - 10, (float) POKE_DOLLAR_TOP_EDGE, ENOUGH_CURRENCY_COLOR, true);
            case ITEM ->
                    graphics.renderItem(larielItem.getCurrencyData().currencyItem(), POKE_DOLLAR_LEFT_EDGE - 8, POKE_DOLLAR_TOP_EDGE - 4);
            case CUSTOM -> { /* do nothing -> NYI */ }
        }

        if (quantity > getMaxBuyable(larielItem, player)) {
            colour = NOT_ENOUGH_CURRENCY_COLOR;
        }

        graphics.drawString(this.font, priceAmount, PRICE_AMOUNT_LEFT_EDGE, PRICE_AMOUNT_TOP_EDGE, colour, true);
    }

    private void renderReputation(GuiGraphics graphics) {
        var player = Minecraft.getInstance().player;
        var buyScreenTop = this.height / 2 - this.getBuyScreenHeight() / 2 + this.getBuyScreenTopEdgePadding();

        if (player == null) return;

        var reputationState = LarielPlayerReputationStoreManager.get(player);
        var currentLevel = reputationState.getLevel(shopkeeperId);
        var currentLevelXp = reputationState.getXpInCurrentLevel(shopkeeperId);
        var nextLevelXp = reputationState.getNextLevelXp(shopkeeperId);

        graphics.drawString(this.font, "Level: " + currentLevel, listLeft() + 66, buyScreenTop + 12,
                16777215, true);

        ScreenHelper.drawSquashedString(graphics, font, "XP: " + currentLevelXp + "/" + nextLevelXp, false,
                40, listLeft() + 66, buyScreenTop + 26, 16777215, true);
    }

    @Override
    protected void renderMiniScreenBuySellButton(GuiGraphics graphics, int mouseX, int mouseY, double price, List<ShopItem> listItems) {
        var larielShopItem = larielItems.get(selectedItem);
        var player = Minecraft.getInstance().player;

        if (player == null) return;

        var buyLabel = I18n.get(this.currentTab == EnumBuySell.Buy ? "gui.shopkeeper.buy" : "gui.shopkeeper.sell");
        var MINI_SCREEN_BUY_LABEL_LEFT_EDGE = (float) (miniLeft() + 30) - (float) Minecraft.getInstance().font.width(buyLabel) / 2.0F;
        var MINI_SCREEN_BUY_LABEL_TOP_EDGE = (float) (buyScreenTop() + 78);
        var colour = 16777215;
        var validTransaction = quantity <= getMaxBuyable(larielShopItem, player);
        if (mouseX > buyButtonLeft() && mouseX < this.BUY_BUTTON_RIGHT_EDGE && mouseY > this.BUY_BUTTON_TOP_EDGE && mouseY < this.BUY_BUTTON_BOTTOM_EDGE) {
            if (validTransaction) {
                ScreenHelper.drawImageQuad(Resources.shopkeeper, graphics, (float) buyButtonLeft(), (float) this.BUY_BUTTON_TOP_EDGE, 49.0F, 18.0F, 0.01953125F, 0.796875F, 0.2109375F, 0.8671875F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
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
        var maxBuyable = getMaxBuyable(larielItem, player);

        // --- UP ARROW ---
        if (mouseX > ARROW_BUTTON_LEFT_EDGE && mouseX < ARROW_BUTTON_RIGHT_EDGE &&
                mouseY > UP_ARROW_BUTTON_TOP_EDGE && mouseY < UP_ARROW_BUTTON_BOTTOM_EDGE) {

            var newQuantity = this.quantity + 1;

            if (newQuantity <= maxBuyable) {

                this.quantity = newQuantity;
            } else {
                this.quantity = 1;
            }

            this.floatQuantity = 2.0F;
        }

        // --- DOWN ARROW ---
        if (mouseX > ARROW_BUTTON_LEFT_EDGE && mouseX < ARROW_BUTTON_RIGHT_EDGE &&
                mouseY > DOWN_ARROW_BUTTON_TOP_EDGE && mouseY < DOWN_ARROW_BUTTON_BOTTOM_EDGE) {

            if (maxBuyable <= 1) {
                this.quantity = 1;
                this.floatQuantity = 2.0F;
                return;
            }

            if (this.quantity > 1) {
                this.quantity--;
            } else {
                this.quantity = maxBuyable;
            }

            this.floatQuantity = 2.0F;
        }

        // --- BUY BUTTON ---
        if (mouseX > buyButtonLeft() && mouseX < BUY_BUTTON_RIGHT_EDGE &&
                mouseY > BUY_BUTTON_TOP_EDGE && mouseY < BUY_BUTTON_BOTTOM_EDGE) {

            if (this.quantity <= maxBuyable) {
                this.sendBuyPacket();
                this.selectedItem = -1;
            }
        }
    }

    private int getMaxBuyable(LarielShopItem larielItem, LocalPlayer player) {
        var maxAffordable = getMaxAffordableQuantity(larielItem, player);

        var maxStackable = checkRemainingSlots(larielItem.getShopItem().itemStack());
        maxStackable /= larielItem.getAmount();

        int maxDaily = Integer.MAX_VALUE;
        if (larielItem.getMaxSellCountPerDay() >= 0) {
            var purchasedToday = LarielShopPurchaseStore.getPurchasedToday(player, shopkeeperId, larielItem);
            maxDaily = larielItem.getMaxSellCountPerDay() - purchasedToday;
        }

        return Math.min(Math.min(maxAffordable, maxStackable), maxDaily);
    }

    @Override
    protected void sendBuyPacket() {
        var shopItemId = larielItems.get(selectedItem).getShopItem().uuid();
        LarielNetwork.sendToServer(new LarielShopTransactionPacket(shopkeeperId, shopItemId, this.quantity));
    }

    @Override
    protected void sendSellPacket() {
        // Do nothing -> I'm not supporting sell -> should be never called
    }

    @Override
    public void onClose() {
        var player = Minecraft.getInstance().player;

        if (player != null)
            player.closeContainer();
    }

    private int checkRemainingSlots(ItemStack buying) {
        var player = Minecraft.getInstance().player;
        if (player == null) return 0;

        if (buying == null || buying.isEmpty() || buying.getItem() == Items.AIR)
            return 0;

        var maxStack = buying.getMaxStackSize();
        var available = 0;

        try {
            for (var curStack : player.getInventory().items) {
                if (ItemStack.isSameItemSameComponents(curStack, buying)) {
                    available += maxStack - curStack.getCount();
                } else if (curStack.isEmpty()) {
                    available += maxStack;
                }
            }

            return Math.min(2304, available);

        } catch (Throwable t) {
            LarielsQoL.getLogger().catching(t);
            return 2304;
        }
    }

    private int getMaxAffordableQuantity(LarielShopItem item, LocalPlayer player) {
        var price = item.getPrice();

        return switch (item.getCurrencyData().type()) {
            case POKEDOLLAR -> (int) (ClientData.playerMoney.doubleValue() / price);

            case SCOREBOARD -> {
                var scoreboard = player.getScoreboard();
                var objective = scoreboard.getObjective(item.getCurrencyData().customKey());
                if (objective == null) yield 0;
                var score = scoreboard.getOrCreatePlayerScore(player, objective).get();
                yield score / price;
            }

            case ITEM -> {
                var count = player.getInventory().countItem(item.getCurrencyData().currencyItem().getItem());
                yield count / price;
            }

            case CUSTOM -> 0;
        };
    }

    private int buyScreenLeft() {
        return this.width / 2 - this.getBuyScreenWidth() / 2;
    }

    private int buyScreenTop() {
        return this.height / 2 - this.getBuyScreenHeight() / 2 + this.getBuyScreenTopEdgePadding();
    }

    private int listLeft() {
        return buyScreenLeft() + 12;
    }

    private int miniLeft() {
        return buyScreenLeft() + getBuyScreenWidth() + 8;
    }

    private int buyButtonLeft() {
        return miniLeft() + 5;
    }

}