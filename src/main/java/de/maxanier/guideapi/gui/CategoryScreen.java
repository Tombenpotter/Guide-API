package de.maxanier.guideapi.gui;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.button.ButtonBack;
import de.maxanier.guideapi.button.ButtonNext;
import de.maxanier.guideapi.button.ButtonPrev;
import de.maxanier.guideapi.button.ButtonSearch;
import de.maxanier.guideapi.network.PacketHandler;
import de.maxanier.guideapi.network.PacketSyncCategory;
import de.maxanier.guideapi.wrapper.EntryWrapper;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class CategoryScreen extends BaseScreen {

    public ResourceLocation outlineTexture;
    public ResourceLocation pageTexture;
    public Book book;
    public CategoryAbstract category;
    public HashMultimap<Integer, EntryWrapper> entryWrapperMap = HashMultimap.create();
    public ButtonBack buttonBack;
    public ButtonNext buttonNext;
    public ButtonPrev buttonPrev;
    public ButtonSearch buttonSearch;
    public int entryPage;
    @Nullable
    public EntryAbstract startEntry;

    public CategoryScreen(Book book, CategoryAbstract category, Player player, ItemStack bookStack, @Nullable EntryAbstract startEntry) {
        super(category.name, player, bookStack);
        this.book = book;
        this.category = category;
        this.pageTexture = book.getPageTexture();
        this.outlineTexture = book.getOutlineTexture();
        this.entryPage = 0;
        this.startEntry = startEntry;
    }

    @Override
    public void init() { //Init
        this.entryWrapperMap.clear();

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        //addButton
        addRenderableWidget(buttonBack = new ButtonBack(guiLeft + xSize / 6, guiTop, (btn) -> {
            this.minecraft.setScreen(new HomeScreen(book, player, bookStack));
        }, this));
        addRenderableWidget(buttonNext = new ButtonNext(guiLeft + 4 * xSize / 6, guiTop + 5 * ySize / 6, (btn) -> {
            if (entryPage + 1 < entryWrapperMap.asMap().size()) {
                nextPage();
            }
        }, this));
        addRenderableWidget(buttonPrev = new ButtonPrev(guiLeft + xSize / 5, guiTop + 5 * ySize / 6, (btn) -> {
            if (entryPage > 0) {
                prevPage();
            }
        }, this));
        addRenderableWidget(buttonSearch = new ButtonSearch((guiLeft + xSize / 6) - 25, guiTop + 5, (btn) -> {
            this.minecraft.setScreen(new SearchScreen(book, player, bookStack, this));
        }, this));

        int eX = guiLeft + 37;
        int eY = guiTop + 15;
        int i = 0;
        int pageNumber = 0;
        List<EntryAbstract> entries = Lists.newArrayList(category.entries.values());
        for (EntryAbstract entry : entries) {
            entry.onInit(book, category, this, player, bookStack);
            entryWrapperMap.put(pageNumber, new EntryWrapper(this, book, category, entry, eX, eY, 4 * xSize / 6, 10, player, this.font, bookStack));
            if (entry.equals(this.startEntry)) {
                this.startEntry = null;
                this.entryPage = pageNumber;
            }
            eY += 13;
            i++;

            if (i >= 11) {
                i = 0;
                eY = guiTop + 15;
                pageNumber++;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == this.minecraft.options.keyUse.getKey().getValue()) {
            this.minecraft.setScreen(new HomeScreen(book, player, bookStack));
            return true;
        } else if ((keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_RIGHT) && entryPage + 1 < entryWrapperMap.asMap().size()) {
            nextPage();
            return true;
        } else if ((keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_LEFT) && entryPage > 0) {
            prevPage();
            return true;
        }
        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int typeofClick) {
        boolean ret = super.mouseClicked(mouseX, mouseY, typeofClick);

        for (EntryWrapper wrapper : this.entryWrapperMap.get(entryPage)) {
            if (wrapper.isMouseOnWrapper(mouseX, mouseY) && wrapper.canPlayerSee()) {
                if (typeofClick == 0) wrapper.entry.onLeftClicked(book, category, mouseX, mouseY, player, this);
                else if (typeofClick == 1)
                    wrapper.entry.onRightClicked(book, category, mouseX, mouseY, player, this);
            }
        }

        if (typeofClick == 1)
            this.minecraft.setScreen(new HomeScreen(book, player, bookStack));
        return ret;
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double movement) {
        if (movement < 0)
            nextPage();
        else if (movement > 0)
            prevPage();

        return movement != 0 || super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, movement);
    }

    public void nextPage() {
        if (entryPage >= entryWrapperMap.asMap().size())
            entryPage = entryWrapperMap.asMap().size() - 1;
        if (entryPage != entryWrapperMap.asMap().size() - 1 && !entryWrapperMap.asMap().isEmpty())
            entryPage++;
    }

    @Override
    public void onClose() {
        super.onClose();

        PacketHandler.INSTANCE.sendToServer(new PacketSyncCategory(book.getCategoryList().indexOf(category), entryPage));
    }

    public void prevPage() {
        if (entryPage >= entryWrapperMap.asMap().size())
            entryPage = entryWrapperMap.asMap().size() - 1;
        if (entryPage != 0)
            entryPage--;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float renderPartialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);
        RenderSystem.setShaderTexture(0, pageTexture); //minecraft
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
        RenderSystem.setShaderTexture(0, outlineTexture);
        drawTexturedModalRectWithColor(stack, guiLeft, guiTop, 0, 0, xSize, ySize, book.getColor());

        entryPage = Mth.clamp(entryPage, 0, entryWrapperMap.size() - 1);

        for (EntryWrapper wrapper : this.entryWrapperMap.get(entryPage)) {
            if (wrapper.canPlayerSee()) {
                wrapper.draw(stack, mouseX, mouseY, this);
                wrapper.drawExtras(stack, mouseX, mouseY, this);
            }
            if (wrapper.isMouseOnWrapper(mouseX, mouseY) && wrapper.canPlayerSee()) {
                wrapper.onHoverOver(mouseX, mouseY);
            }
        }

        drawCenteredStringWithoutShadow(stack, font, String.format("%d/%d", entryPage + 1, entryWrapperMap.asMap().size()), guiLeft + xSize / 2, guiTop + 5 * ySize / 6, 0);
        drawCenteredString(stack, font, category.getName(), guiLeft + xSize / 2, guiTop - 10, Color.WHITE.getRGB());

        buttonPrev.visible = entryPage != 0;
        buttonNext.visible = entryPage != entryWrapperMap.asMap().size() - 1 && !entryWrapperMap.asMap().isEmpty();

        super.render(stack, mouseX, mouseY, renderPartialTicks);
    }
}
