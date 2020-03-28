package amerifrance.guideapi.entry;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.BaseScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class EntryItemStack extends Entry {

    public ItemStack stack;

    public EntryItemStack(List<IPage> pageList, String name, ItemStack stack, boolean unicode) {
        super(pageList, name, unicode);
        this.stack = stack;
    }

    public EntryItemStack(List<IPage> pageList, String name, ItemStack stack) {
        this(pageList, name, stack, false);
    }

    public EntryItemStack(String name, boolean unicode, ItemStack stack) {
        super(name, unicode);
        this.stack = stack;
    }

    public EntryItemStack(String name, ItemStack stack) {
        super(name);
        this.stack = stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawExtras(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, BaseScreen guiBase, FontRenderer fontRendererObj) {
        if (stack != null)
            GuiHelper.drawScaledItemStack(stack, entryX + 2, entryY, 0.5F);

        super.drawExtras(book, category, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, guiBase, fontRendererObj);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntryItemStack)) return false;
        if (!super.equals(o)) return false;

        EntryItemStack that = (EntryItemStack) o;

        return stack != null ? stack.equals(that.stack) : that.stack == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        return result;
    }
}
