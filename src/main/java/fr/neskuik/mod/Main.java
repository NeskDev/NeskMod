package fr.neskuik.mod;

import co.aikar.commands.PaperCommandManager;
import fr.neskuik.mod.commands.*;
import fr.neskuik.mod.listeners.InvseeListener;
import fr.neskuik.mod.listeners.ModEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private VanishCommand vanish;
    private ModCommand modCommand;

    @Override
    public void onEnable() {
        getLogger().info("Plugin NeskMod starting...");

        PaperCommandManager manager = new PaperCommandManager(this);

        /**
         * Registers Commands
         */

        getLogger().info("Registering commands...");

        manager.registerCommand(new FlyCommand());
        manager.registerCommand(new FreezeCommand());
        manager.registerCommand(new InvseeCommand());
        manager.registerCommand(new ModCommand(this, vanish));
        manager.registerCommand(new PtpCommand());
        manager.registerCommand(new TphCommand());
        manager.registerCommand(new VanishCommand(this));

        /**
         * Registers Listeners
         */

        getServer().getPluginManager().registerEvents(new InvseeListener(), this);
        getServer().getPluginManager().registerEvents(new ModEventListener(modCommand, vanish), this);


        getLogger().info("Plugin NeskMod enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin NeskMod stopping...");

        getLogger().info("Plugin disabled!");
    }

}