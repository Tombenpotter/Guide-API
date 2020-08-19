package de.maxanier.guideapi.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.maxanier.guideapi.GuideMod;
import de.maxanier.guideapi.api.button.ButtonGuideAPI;
import de.maxanier.guideapi.api.util.GuiHelper;
import de.maxanier.guideapi.gui.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class ButtonBack extends ButtonGuideAPI {

    public ButtonBack(int widthIn, int heightIn, Button.IPressable onPress, BaseScreen guiBase) {
        super(widthIn, heightIn, 18, 10, new TranslationTextComponent("guideapi.button.back"), onPress, guiBase);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) { //visible
            RenderHelper.enableStandardItemLighting();
            RenderSystem.enableBlend();
            RenderSystem.disableLighting();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(GuideMod.ID, "textures/gui/book_colored.png"));
            if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, width, height)) { //x,y,width,height
                this.blit(stack, x, y + 1, 70, 201, 18, 10);
                GuiUtils.drawHoveringText(stack, getHoveringText(), mouseX, mouseY, guiBase.width, guiBase.height, -1, Minecraft.getInstance().fontRenderer);
            } else {
                this.blit(stack, x, y, 94, 201, 18, 10);
            }
            RenderSystem.disableBlend();
            RenderHelper.disableStandardItemLighting();
        }
    }
}
