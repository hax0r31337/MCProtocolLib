package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientWindowActionPacket extends MinecraftPacket {
    private int windowId;
    private int slot;
    private int mode;
    private int button;
    private int actionId;
    private ItemStack clicked;

    @SuppressWarnings("unused")
    private ClientWindowActionPacket() {
    }

    public ClientWindowActionPacket(int windowId, int actionId, int slot, ItemStack clicked, int mode, int button) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.slot = slot;
        this.clicked = clicked;
        this.mode = mode;
        this.button = button;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getClickedItem() {
        return this.clicked;
    }

    public int getMode() {
        return this.mode;
    }

    public int getButton() {
        return this.getButton();
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.slot = in.readShort();
        this.button = in.readByte();
        this.actionId = in.readShort();
        this.mode = in.readByte();
        this.clicked = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        out.writeByte(this.button);
        out.writeShort(this.actionId);
        out.writeByte(this.mode);
        NetUtil.writeItem(out, this.clicked);
    }
}
