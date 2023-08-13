package vn.thanhmagics.cratesclickitem;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import vn.thanhmagics.utils.AbstractFileConfig;

public class SaveFile extends AbstractFileConfig {
    public SaveFile(@NotNull Plugin plugin, boolean resource) {
        super(plugin, resource);
    }

    @NotNull
    @Override
    public String getFileName() {
        return "save.yml";
    }

}
