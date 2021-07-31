package de.maxanier.guideapi.page;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.GuiHelper;
import de.maxanier.guideapi.api.util.IngredientCycler;
import de.maxanier.guideapi.gui.BaseScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class PageItemStack extends PageText {

    private final IngredientCycler ingredientCycler = new IngredientCycler();
    public Ingredient ingredient;


    public PageItemStack(FormattedText draw, Ingredient ingredient) {
        super(draw, 60);
        this.ingredient = ingredient;
    }

    /**
     * @param draw       - Unlocalized text to draw
     * @param ingredient - ItemStack to render
     */
    public PageItemStack(FormattedText draw, ItemStack ingredient) {
        this(draw, Ingredient.of(ingredient));
    }

    /**
     * @param draw - Unlocalized text to draw
     * @param item - Item to render
     */
    public PageItemStack(FormattedText draw, Item item) {
        this(draw, new ItemStack(item));
    }

    /**
     * @param draw  - Unlocalized text to draw
     * @param block - Block to render
     */
    public PageItemStack(FormattedText draw, Block block) {
        this(draw, new ItemStack(block));
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawExtras(PoseStack stack, Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, BaseScreen guiBase, Font fontRendererObj) {
        ingredientCycler.tick(guiBase.getMinecraft());
        ingredientCycler.getCycledIngredientStack(ingredient, 0).ifPresent(s -> {
            GuiHelper.drawScaledItemStack(stack, s, guiLeft + 101, guiTop + 20, 3);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageItemStack that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ingredient != null ? ingredient.hashCode() : 0);
        return result;
    }
}
