package vn.thanhmagics.cratesclickitem;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import vn.thanhmagics.utils.ListenerUtils;
import vn.thanhmagics.utils.ObjectArrayUtils;
import vn.thanhmagics.utils.PlayerData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class CratesClickItem extends JavaPlugin {

    private static CratesClickItem instance;

    private SaveFile saveFile;

    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    private Map<String,ClickCrate> clickCrateMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();
        saveFile = new SaveFile(this,true);
        try {
            for (String id : getSaveFile().getConfig().getConfigurationSection("data").getKeys(false)) {
                ClickCrate crate = new ClickCrate(id);
                crate.setItemStack(ObjectArrayUtils.Companion.toObj(Objects.requireNonNull(getSaveFile().getConfig().getString("data." + id + ".is"))));
                crate.setCrate(ExcellentCratesAPI.getCrateManager().getCrateById(
                        Objects.requireNonNull(getSaveFile().getConfig().getString("data." + id + ".cid"))
                ));
            }
        } catch (Exception ignored) {}
        new ListenerUtils<PlayerJoinEvent>(this) {
            @Override
            public void onEvent(@NotNull PlayerJoinEvent event) {
                if (!playerDataMap.containsKey(event.getPlayer().getUniqueId())) {
                    PlayerData playerData = new PlayerData(event.getPlayer().getUniqueId());
                    playerData.setPlayer(event.getPlayer());
                    playerDataMap.put(event.getPlayer().getUniqueId(),playerData);
                }
            }
        };
        getCommand("cratesclickitem").setExecutor(new Commands());

    }

    public Map<String, ClickCrate> getClickCrateMap() {
        return clickCrateMap;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (ClickCrate clickCrate : clickCrateMap.values()) {
            getSaveFile().getConfig().set("data." + clickCrate.getId() + ".cid",clickCrate.getCrate().getName());
            ItemStack itemStack = clickCrate.getItemStack();
            String serializedItemStack = ObjectArrayUtils.Companion.objToString((Serializable) itemStack);
            getSaveFile().getConfig().set("data." + clickCrate.getId() + ".is", ObjectArrayUtils.Companion.objToString(serializedItemStack));
            getSaveFile().save();
        }
    }


    public Map<UUID, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public static CratesClickItem getInstance() {
        return instance;
    }

    public SaveFile getSaveFile() {
        return saveFile;
    }
}
