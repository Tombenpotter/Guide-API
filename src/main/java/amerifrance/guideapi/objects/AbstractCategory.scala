package amerifrance.guideapi.objects

import java.util

import amerifrance.guideapi.gui.{GuiBase, GuiHome}
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector

abstract class AbstractCategory(entryList: util.List[AbstractEntry] = new util.ArrayList[AbstractEntry](), unlocCategoryName: String, itemstack: ItemStack) {

  var entries: util.List[AbstractEntry] = entryList
  var unlocalizedCategoryName: String = unlocCategoryName
  var stack = itemstack

  def addEntry(entry: AbstractEntry) = {
    this.entries.add(entry)
  }

  def addEntryList(list: util.List[AbstractEntry]) = {
    this.entries.addAll(list)
  }

  def removeEntry(entry: AbstractEntry) = {
    this.entries.remove(entry)
  }

  def removeCategoryList(list: util.List[AbstractEntry]) = {
    this.entries.remove(list)
  }

  def getLocalizedName(): String = {
    return StatCollector.translateToLocal(unlocalizedCategoryName)
  }

  def getTooltip: util.List[String] = {
    val list: util.ArrayList[String] = new util.ArrayList[String]
    list.add(getLocalizedName)
    return list
  }

  def onLeftClicked(book: Book, mouseX: Int, mouseY: Int, player: EntityPlayer, guiHome: GuiHome)

  def onRightClicked(book: Book, mouseX: Int, mouseY: Int, player: EntityPlayer, guiHome: GuiHome)

  def draw(book: Book, categoryX: Int, categoryY: Int, categoryWidth: Int, categoryHeight: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, drawOnLeft: Boolean, renderItem: RenderItem)

  def drawExtras(book: Book, categoryX: Int, categoryY: Int, categoryWidth: Int, categoryHeight: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, drawOnLeft: Boolean, renderItem: RenderItem)

  def canSee(player: EntityPlayer): Boolean
}
