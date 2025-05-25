package fr.neskuik.mod;

import co.aikar.commands.PaperCommandManager;
import fr.neskuik.mod.api.CheckUpdates;
import fr.neskuik.mod.api.PlayerCPSWrapper;
import fr.neskuik.mod.commands.*;
import fr.neskuik.mod.commands.sanctions.SanctionsCommand;
import fr.neskuik.mod.event.PlayerConnectionEvent;
import fr.neskuik.mod.listeners.InvseeListener;
import fr.neskuik.mod.listeners.ModEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private VanishCommand vanish;
    private ModCommand modCommand;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Plugin NeskMod starting...");

        PaperCommandManager manager = new PaperCommandManager(this);

        CheckUpdates.init(this);
        new PlayerCPSWrapper(null);

        /**
         * Inits of Listeners
         */

        vanish = new VanishCommand(this);
        modCommand = new ModCommand(this, vanish);

        /**
         * Registers Commands
         */

        getLogger().info("Registering commands...");

        manager.registerCommand(new FlyCommand());
        manager.registerCommand(new FreezeCommand());
        manager.registerCommand(new InvseeCommand());
        manager.registerCommand(modCommand);
        manager.registerCommand(new PtpCommand());
        manager.registerCommand(new TphCommand());
        manager.registerCommand(vanish);
        manager.registerCommand(new SanctionsCommand());
        manager.registerCommand(new CPSCommand());
        manager.registerCommand(new NeskModCommand(this));

        /**
         * Registers Listeners
         */

        getServer().getPluginManager().registerEvents(new InvseeListener(), this);
        getServer().getPluginManager().registerEvents(new ModEventListener(modCommand, vanish), this);
        getServer().getPluginManager().registerEvents(new FreezeCommand(), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionEvent(modCommand), this);


        getLogger().info("Plugin enabled in " + (System.currentTimeMillis() - startTime) + "ms !");
    }

    @Override
    public void onDisable() {
        long endTime = System.currentTimeMillis();
        getLogger().info("Plugin NeskMod stopping...");
        getLogger().info("Unregistering commands...");
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.unregisterCommands();
        getLogger().info("Unregistering listeners...");
        getLogger().info("Plugin disabled in " + (endTime - System.currentTimeMillis()) + "ms !");
        getServer().getPluginManager().disablePlugin(this);
    }

}