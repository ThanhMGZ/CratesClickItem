package vn.thanhmagics.cratesclickitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import vn.thanhmagics.utils.ListenerUtils;

import java.util.ArrayList;
import java.util.List;

public class ClickCrate {

    private Crate crate;

    private ItemStack itemStack;


    private String id;

    public ClickCrate(String id) {
        this.id = id;
        init();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Crate getCrate() {
        return crate;
    }

    public void setCrate(Crate crate) {
        this.crate = crate;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    private void init() {
        new ListenerUtils<CrateOpenEvent>(CratesClickItem.getInstance()) {
            @Override
            public void onEvent(@NotNull CrateOpenEvent crateOpenEvent) {
                if (getCrate() == null)
                    return;
                if (crateOpenEvent.getCrate().equals(crate)) {
                    if (getItemStack() == null)
                        return;
                    if (!removeItemFromPlayerInv(crateOpenEvent.getPlayer().getInventory(), getItemStack())) {
                        crateOpenEvent.setCancelled(true);
                    }
                }
            }
        };
    }


    private boolean removeItemFromPlayerInv(PlayerInventory inventory,ItemStack itemStack) {
        ItemStack stack = itemStack.clone();
        stack.setAmount(1);
        for (int i = 0; i < inventory.getContents().length; i++) {
            ItemStack is = inventory.getContents()[i];
            if (is == null)
                continue;
            ItemStack is1 = is.clone();
            is1.setAmount(1);
            if (stack.equals(is1)) {
                int m = itemStack.getAmount();
                if (is.getAmount() >= m) {
                    ItemStack nis = itemStack.clone();
                    nis.setAmount(is.getAmount() - m);
                    inventory.setItem(i,nis);
                    return true;
                }
            }
        }
        return false;
    }


}
