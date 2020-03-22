package api.impl;

import api.IPage;
import api.impl.abstraction.CategoryAbstract;
import api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.BaseScreen;
import amerifrance.guideapi.gui.EntryScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Page implements IPage {

    protected boolean unicode;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, BaseScreen guiBase, FontRenderer fontRendererObj) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawExtras(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, BaseScreen guiBase, FontRenderer fontRendererObj) {
    }

    @Override
    public boolean canSee(Book book, CategoryAbstract category, EntryAbstract entry, PlayerEntity player, ItemStack bookStack, EntryScreen guiEntry) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onLeftClicked(Book book, CategoryAbstract category, EntryAbstract entry, double mouseX, double mouseY, PlayerEntity player, EntryScreen guiEntry) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRightClicked(Book book, CategoryAbstract category, EntryAbstract entry, double mouseX, double mouseY, PlayerEntity player, EntryScreen guiEntry) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onInit(Book book, CategoryAbstract category, EntryAbstract entry, PlayerEntity player, ItemStack bookStack, EntryScreen guiEntry) {
    }

    public void setUnicodeFlag(boolean flag) {
        this.unicode = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        return true;
    }
}
