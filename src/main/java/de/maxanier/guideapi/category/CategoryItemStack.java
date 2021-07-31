package de.maxanier.guideapi.category;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.Category;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.GuiHelper;
import de.maxanier.guideapi.gui.BaseScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Objects;

public class CategoryItemStack extends Category {

    public ItemStack itemStack;

    public CategoryItemStack(Map<ResourceLocation, EntryAbstract> entries, Component name, ItemStack stack) {
        super(entries, name);
        this.itemStack = stack;
    }

    public CategoryItemStack(Component name, ItemStack stack) {
        super(name);
        this.itemStack = stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void draw(PoseStack stack, Book book, int categoryX, int categoryY, int categoryWidth, int categoryHeight, int mouseX, int mouseY, BaseScreen guiBase, boolean drawOnLeft, ItemRenderer renderItem) {
        GuiHelper.drawScaledItemStack(stack, this.itemStack, categoryX, categoryY, 1.5F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawExtras(PoseStack stack, Book book, int categoryX, int categoryY, int categoryWidth, int categoryHeight, int mouseX, int mouseY, BaseScreen guiBase, boolean drawOnLeft, ItemRenderer renderItem) {
        if (canSee(guiBase.player, guiBase.bookStack) && GuiHelper.isMouseBetween(mouseX, mouseY, categoryX, categoryY, categoryWidth, categoryHeight))
            guiBase.renderComponentTooltip(stack, this.getTooltip(), mouseX, mouseY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryItemStack that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(itemStack, that.itemStack);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }
}
