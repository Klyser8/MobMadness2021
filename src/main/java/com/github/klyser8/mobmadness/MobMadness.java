package com.github.klyser8.mobmadness;

import com.github.klyser8.mobmadness.commands.*;
import com.github.klyser8.mobmadness.game.MiscListener;
import com.github.klyser8.mobmadness.game.items.ItemListener;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.WeaponListener;
import com.github.klyser8.mobmadness.game.mob.MadMobType;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.game.mob.MobListener;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulStateListener;
import com.github.klyser8.mobmadness.game.player.PlayerListener;
import com.github.klyser8.mobmadness.game.shop.ShopListener;
import com.github.klyser8.mobmadness.ui.UIEvents;
import com.github.klyser8.mobmadness.game.world.MapType;
import com.github.klyser8.mobmadness.util.ItemGlow;
import com.google.common.base.Enums;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Main class of the plugin.
 */
public final class MobMadness extends JavaPlugin {

    private final GameManager gameManager;

    public MobMadness() {
        gameManager = new GameManager(this);
    }

    /**
     * Runs when the plugin is enabled. Registers events and commands.
     */
    @Override
    public void onEnable() {
        registerCommands();
        registerGlow();
        Bukkit.getPluginManager().registerEvents(new RandomListener(this), this);
        getServer().getPluginManager().registerEvents(new UIEvents(), this);
        getServer().getPluginManager().registerEvents(new MiscListener(this), this);
        getServer().getPluginManager().registerEvents(new ShopListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        getServer().getPluginManager().registerEvents(new WeaponListener(this), this);
        getServer().getPluginManager().registerEvents(new MobListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new SoulStateListener(this), this);

        //TODO fix invisibility trider
        /*ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
//                }
            }
        });*/

    }

    @Override
    public void onDisable() {
        for (MapType type : gameManager.getGames().keySet()) {
            gameManager.stopGame(type);
        }
    }

    /**
     * Gets an instance of the game manager class.
     * @return Instance of {@link GameManager}
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Registers all commands through the {@link CommandManager}, alongside custom fields and tab completions.
     */
    private void registerCommands() {
        CommandManager commandManager = new CommandManager(this, true);
        commandManager.getCompletionHandler().register("#madmobs", input ->
                Arrays.stream(MadMobType.values()).map(MadMobType::toString).collect(Collectors.toList()));
        commandManager.getParameterHandler().register(MadMobType.class, argument ->
                new TypeResult(Enums.getIfPresent(
                        MadMobType.class, String.valueOf(argument).toUpperCase()).orNull(), argument));

        commandManager.getCompletionHandler().register("#maptypes", input ->
                Arrays.stream(MapType.values()).map(MapType::toString).collect(Collectors.toList()));
        commandManager.getParameterHandler().register(MapType.class, argument ->
                new TypeResult(Enums.getIfPresent(
                        MapType.class, String.valueOf(argument).toUpperCase()).orNull(), argument));

        commandManager.getCompletionHandler().register("#itemtypes", input ->
                Arrays.stream(ItemType.values()).map(ItemType::toString).collect(Collectors.toList()));
        commandManager.getParameterHandler().register(ItemType.class, argument ->
                new TypeResult(Enums.getIfPresent(
                        ItemType.class, String.valueOf(argument).toUpperCase()).orNull(), argument));

        commandManager.register(new SpawnMobCommand(this));
        commandManager.register(new StartGameCommand(this));
        commandManager.register(new StopGameCommand(this));
        commandManager.register(new PauseGameCommand(this));
        commandManager.register(new TeleportMapCommand(this));
        commandManager.register(new JoinGameCommand(this));
        commandManager.register(new LeaveGameCommand(this));
        commandManager.register(new GiveItemCommand(this));
        commandManager.register(new DebugCommand(this));
        commandManager.register(new SummonMobCommand(this));
    }

    /**
     * Util enchantment to make items glow.
     */
    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ItemGlow glow = new ItemGlow(new NamespacedKey(this, ItemGlow.key));
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
