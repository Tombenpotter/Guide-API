package de.maxanier.guideapi.gui;

import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.button.ButtonBack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Simple GuiEntry which back button leads to a previous entry and not to the category page
 */
public class LinkedEntryScreen extends EntryScreen {

    private final EntryAbstract from;
    private final int fromPage;

    public LinkedEntryScreen(Book book, CategoryAbstract category, EntryAbstract entry, Player player, ItemStack bookStack, EntryAbstract from, int fromPage) {
        super(book, category, entry, player, bookStack);
        this.from = from;
        this.fromPage = fromPage;
    }

    @Override
    public void init() {
        super.init();
        this.removeWidget(this.buttonBack);
        this.addRenderableWidget(this.buttonBack = new ButtonBack(this.guiLeft + this.xSize / 6, this.guiTop, (btn) -> {
            EntryScreen e = new EntryScreen(book, category, from, player, bookStack);
            e.pageNumber = fromPage;
            this.minecraft.setScreen(e);
        }, this));
    }
}
