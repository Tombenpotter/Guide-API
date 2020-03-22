package amerifrance.guideapi.wrapper;

import api.impl.Book;
import api.impl.abstraction.CategoryAbstract;
import api.util.GuiHelper;
import amerifrance.guideapi.gui.BaseScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class CategoryWrapper extends AbstractWrapper {

    public Book book;
    public CategoryAbstract category;
    public int x, y, width, height;
    public PlayerEntity player;
    public FontRenderer renderer;
    public ItemRenderer renderItem;
    public boolean drawOnLeft;
    public ItemStack bookStack;

    public CategoryWrapper(Book book, CategoryAbstract category, int x, int y, int width, int height, PlayerEntity player, FontRenderer renderer, ItemRenderer renderItem, boolean drawOnLeft, ItemStack bookStack) {
        this.book = book;
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.renderer = renderer;
        this.renderItem = renderItem;
        this.drawOnLeft = drawOnLeft;
        this.bookStack = bookStack;
    }

    @Override
    public void onHoverOver(int mouseX, int mouseY) {
    }

    @Override
    public boolean canPlayerSee() {
        return category.canSee(player, bookStack);
    }

    @Override
    public void draw(int mouseX, int mouseY, BaseScreen gui) {
        category.draw(book, x, y, width, height, mouseX, mouseY, gui, drawOnLeft, renderItem);
    }

    @Override
    public void drawExtras(int mouseX, int mouseY, BaseScreen gui) {
        category.drawExtras(book, x, y, width, height, mouseX, mouseY, gui, drawOnLeft, renderItem);
    }

    @Override
    public boolean isMouseOnWrapper(double mouseX, double mouseY) {
        return GuiHelper.isMouseBetween(mouseX, mouseY, x, y, width, height);
    }
}
