package de.lariel.qualityoflife.client.screen;

import com.pixelmonmod.pixelmon.client.gui.npc.widget.DropDownWidget;
import de.lariel.qualityoflife.menu.MintTraderMenu;
import de.lariel.qualityoflife.network.packet.LarielMintTraderPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class LarielMintTraderScreen extends AbstractContainerScreen<MintTraderMenu> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath("larielsqualityoflife", "textures/gui/container/mint_trader.png");
    private Item selectedMint;
    private Component statusMessage = null;
    private long statusMessageUntil = 0;
    private int statusMessageColor;

    public LarielMintTraderScreen(MintTraderMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();

        //noinspection unused
        this.addRenderableWidget(Button.builder(
                Component.translatable("minttrader.larielsqualityoflife.trade"),
                btn -> PacketDistributor.sendToServer(new LarielMintTraderPacket(true, selectedMint))
        ).bounds(leftPos + 115, topPos + 33, 53, 20).build());

        selectedMint = LarielMintTraderPacket.DESIRED_MINTS.getFirst();

        DropDownWidget<Item> dropdown = new DropDownWidget<>(
                leftPos + 95,
                topPos + 16,
                65,
                11
        );

        dropdown.setOnSelected(item -> this.selectedMint = item)
                .setOptionConverter(s -> s.getDescription().getString())
                .setOptions(LarielMintTraderPacket.DESIRED_MINTS, LarielMintTraderPacket.DESIRED_MINTS.getFirst());
        dropdown.setSelected(selectedMint);

        this.addRenderableWidget(dropdown);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 1);

        for (Renderable r : this.renderables) {
            if (r instanceof DropDownWidget<?> dd) {
                dd.render(graphics, mouseX, mouseY, partialTicks);
            }
        }

        graphics.pose().popPose();

        if (selectedMint != null) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 1);

            graphics.drawString(
                    this.font,
                    selectedMint.getDescription().getString(),
                    leftPos + 98,
                    topPos + 18,
                    0xFF000000,
                    false
            );

            graphics.pose().popPose();
        }

        if (statusMessage != null && System.currentTimeMillis() < statusMessageUntil) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 2);

            graphics.drawString(
                    this.font,
                    statusMessage.getString(),
                    leftPos + 70,
                    topPos + 72,
                    this.statusMessageColor
            );

            graphics.pose().popPose();
        }

        super.renderTooltip(graphics, mouseX, mouseY);
    }

    public void showStatusMessage(Component msg, int durationTicks, boolean success) {
        this.statusMessage = msg;
        this.statusMessageColor = success ? 0xFF55FF55 : 0xFFFF5555;
        this.statusMessageUntil = System.currentTimeMillis() + (durationTicks * 50L);
    }
}

