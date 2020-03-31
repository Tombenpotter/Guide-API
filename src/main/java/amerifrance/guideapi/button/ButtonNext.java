package amerifrance.guideapi.button;

import amerifrance.guideapi.GuideMod;
import amerifrance.guideapi.api.button.ButtonGuideAPI;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.gui.BaseScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class ButtonNext extends ButtonGuideAPI {

    public ButtonNext(int widthIn, int heightIn, Button.IPressable onPress, BaseScreen guiBase) {
        super(widthIn, heightIn, 18, 10, onPress,guiBase);
    }

    @Override
    public void renderButton( int mouseX, int mouseY, float partialTicks) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(GuideMod.ID, "textures/gui/book_colored.png"));
            if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, width, height)) {
                this.blit(x,y+1,47,201,18,10);
                GuiUtils.drawHoveringText(getHoveringText(),mouseX,mouseY,guiBase.width,guiBase.height,-1,Minecraft.getInstance().fontRenderer);
            } else {
                this.blit(x,y,24,201,18,10);
            }
            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();

    }

    public List<String> getHoveringText() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(TextHelper.localizeEffect("guideapi.button.next"));
        return list;
    }
}
