package de.maxanier.guideapi.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxanier.guideapi.GuideMod;
import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.GuiHelper;
import de.maxanier.guideapi.button.ButtonBack;
import de.maxanier.guideapi.button.ButtonNext;
import de.maxanier.guideapi.button.ButtonPrev;
import de.maxanier.guideapi.util.GuiUtilsCopy;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class SearchScreen extends BaseScreen {

    @Nonnull
    static List<List<Pair<EntryAbstract, CategoryAbstract>>> getMatches(Book book, @Nullable String query, Player player, ItemStack bookStack) {
        List<Pair<EntryAbstract, CategoryAbstract>> discovered = Lists.newArrayList();

        for (CategoryAbstract category : book.getCategoryList()) {
            if (!category.canSee(player, bookStack))
                continue;

            for (EntryAbstract entry : category.entries.values()) {
                if (!entry.canSee(player, bookStack))
                    continue;

                if (Strings.isNullOrEmpty(query) || entry.getName().getString().toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH)))
                    discovered.add(Pair.of(entry, category));
            }
        }

        return Lists.partition(discovered, 10);
    }

    private final Book book;
    private final ResourceLocation outlineTexture;
    private final ResourceLocation pageTexture;
    private final int renderXOffset = 37;
    private final int renderYOffset = 30;
    private final Screen parent;
    private ButtonNext buttonNext;
    private ButtonPrev buttonPrev;
    private EditBox searchField;
    private List<List<Pair<EntryAbstract, CategoryAbstract>>> searchResults;
    private int currentPage = 0;
    private String lastQuery = "";


    public SearchScreen(Book book, Player player, ItemStack bookStack, Screen parent) {
        super(book.getTitle(), player, bookStack);

        this.book = book;
        this.pageTexture = book.getPageTexture();
        this.outlineTexture = book.getOutlineTexture();
        this.parent = parent;
        this.searchResults = getMatches(book, null, player, bookStack);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        if (this.searchField.charTyped(p_charTyped_1_, p_charTyped_2_)) {
            this.updateSearch();
            return true;
        }
        return super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public void init() {

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        addRenderableWidget(new ButtonBack(guiLeft + xSize / 6, guiTop, (btn) -> {
            minecraft.setScreen(parent);

        }, this));
        addRenderableWidget(buttonNext = new ButtonNext(guiLeft + 4 * xSize / 6, guiTop + 5 * ySize / 6, (btn) -> {
            if (currentPage <= searchResults.size() - 1)
                currentPage++;
        }, this));
        addRenderableWidget(buttonPrev = new ButtonPrev(guiLeft + xSize / 5, guiTop + 5 * ySize / 6, (btn) -> {
            if (currentPage > 0)
                currentPage--;
        }, this));

        searchField = new EditBox(font, guiLeft + 43, guiTop + 12, 100, 10, new TranslatableComponent("guideapi.button.search"));
        searchField.setBordered(false);
        searchField.changeFocus(true);
        searchResults = getMatches(book, null, player, bookStack);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (!searchField.isFocused()) {
            return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
            searchField.changeFocus(false);

        if (searchField.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_)) {
            this.updateSearch();
        }

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int typeofClick) {
        if (!super.mouseClicked(mouseX, mouseY, typeofClick)) {
            if (typeofClick == 0) {
                int entryX = guiLeft + renderXOffset;
                int entryY = guiTop + renderYOffset;

                if (searchResults.size() != 0 && currentPage >= 0 && currentPage < searchResults.size()) {
                    List<Pair<EntryAbstract, CategoryAbstract>> pageResults = searchResults.get(currentPage);
                    for (Pair<EntryAbstract, CategoryAbstract> entry : pageResults) {
                        if (GuiHelper.isMouseBetween(mouseX, mouseY, entryX, entryY, 4 * xSize / 6, 10)) {
                            GuideMod.PROXY.openEntry(book, entry.getRight(), entry.getLeft(), player, bookStack);
                        }
                        entryY += 13;
                    }
                }
            } else if (typeofClick == 1) {
                if (GuiHelper.isMouseBetween(mouseX, mouseY, searchField.x, searchField.y, searchField.getInnerWidth(), searchField.getHeight())) {
                    searchField.setValue("");
                    lastQuery = "";
                    searchResults = getMatches(book, "", player, bookStack);
                    return true;
                } else {
                    minecraft.setScreen(parent);
                    return true;
                }
            }


            return searchField.mouseClicked(mouseX, mouseY, typeofClick);
        }
        return true;


    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double movement) {
        if (movement < 0 && buttonNext.visible && currentPage <= searchResults.size())
            currentPage++;
        else if (movement > 0 && buttonPrev.visible && currentPage > 0)
            currentPage--;

        return movement != 0 || super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, movement);

    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, pageTexture);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, outlineTexture);
        drawTexturedModalRectWithColor(stack, guiLeft, guiTop, 0, 0, xSize, ySize, book.getColor());

        fill(stack, searchField.x - 1, searchField.y - 1, searchField.x + searchField.getInnerWidth() + 1, searchField.y + searchField.getHeight() + 1, new Color(166, 166, 166, 128).getRGB());
        fill(stack, searchField.x, searchField.y, searchField.x + searchField.getInnerWidth(), searchField.y + searchField.getHeight(), new Color(58, 58, 58, 128).getRGB());
        searchField.render(stack, mouseX, mouseY, partialTicks);

        int entryX = guiLeft + renderXOffset;
        int entryY = guiTop + renderYOffset;

        if (searchResults.size() != 0 && currentPage >= 0 && currentPage < searchResults.size()) {
            List<Pair<EntryAbstract, CategoryAbstract>> pageResults = searchResults.get(currentPage);
            for (Pair<EntryAbstract, CategoryAbstract> entry : pageResults) {
                entry.getLeft().draw(stack, book, entry.getRight(), entryX, entryY, 4 * xSize / 6, 10, mouseX, mouseY, this, font);
                entry.getLeft().drawExtras(stack, book, entry.getRight(), entryX, entryY, 4 * xSize / 6, 10, mouseX, mouseY, this, font);

                if (GuiHelper.isMouseBetween(mouseX, mouseY, entryX, entryY, 4 * xSize / 6, 10)) {
                    if (GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS)
                        GuiUtilsCopy.drawHoveringText(stack, entry.getRight().getTooltip(), mouseX, mouseY, width, height, 300, font);
                }

                entryY += 13;
            }
        }

        buttonPrev.visible = currentPage != 0;
        buttonNext.visible = currentPage != searchResults.size() - 1 && !searchResults.isEmpty();

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private void updateSearch() {
        if (!searchField.getValue().equalsIgnoreCase(lastQuery)) {
            lastQuery = searchField.getValue();
            searchResults = getMatches(book, searchField.getValue(), player, bookStack);
            if (currentPage > searchResults.size())
                currentPage = searchResults.size() - 1;
        }
    }
}
