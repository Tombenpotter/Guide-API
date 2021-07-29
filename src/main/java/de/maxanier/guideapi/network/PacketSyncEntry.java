package de.maxanier.guideapi.network;

import de.maxanier.guideapi.GuideMod;
import de.maxanier.guideapi.api.IGuideItem;
import de.maxanier.guideapi.api.util.NBTBookTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

public class PacketSyncEntry {

    public int category;
    public ResourceLocation entry;
    public int page;

    public PacketSyncEntry() {
        this.category = -1;
        this.entry = new ResourceLocation(GuideMod.ID, "none");
        this.page = -1;
    }

    public PacketSyncEntry(int category, ResourceLocation entry, int page) {
        this.category = category;
        this.entry = entry;
        this.page = page;
    }

    static void encode(PacketSyncEntry msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.category);
        buf.writeResourceLocation(msg.entry);
        buf.writeInt(msg.page);
    }

    static PacketSyncEntry decode(FriendlyByteBuf buf) {
        PacketSyncEntry msg = new PacketSyncEntry();
        msg.category = buf.readInt();
        msg.entry = buf.readResourceLocation();
        msg.page = buf.readInt();
        return msg;
    }

    public static void handle(final PacketSyncEntry msg, Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context ctx = contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        Validate.notNull(player);
        ctx.enqueueWork(() -> {
            ItemStack book = player.getOffhandItem();
            if (book.isEmpty() || !(book.getItem() instanceof IGuideItem))
                book = player.getMainHandItem();

            if (!book.isEmpty() && book.getItem() instanceof IGuideItem) {
                if (msg.category != -1 && !msg.entry.equals(new ResourceLocation(GuideMod.ID, "none")) && msg.page != -1) {
                    if (!book.hasTag())
                        book.setTag(new CompoundTag());

                    book.getTag().putInt(NBTBookTags.CATEGORY_TAG, msg.category);
                    book.getTag().putString(NBTBookTags.ENTRY_TAG, msg.entry.toString());
                    book.getTag().putInt(NBTBookTags.PAGE_TAG, msg.page);
                }
            }
        });
        ctx.setPacketHandled(true);
    }


}
