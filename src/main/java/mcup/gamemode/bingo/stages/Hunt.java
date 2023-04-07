package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Hunt extends GamemodeStage {

  @Override
  public void load() {
    super.load();

    initBossBarCountdown();

    plugin.core.apiManager.playerManager.setGlobalGamemode(GameMode.SURVIVAL);

    plugin.storage.initCollectedItems();
    plugin.storage.readBingoItems();


    Player player = Bukkit.getPlayer("GoracioNewport");
    plugin.storage.giveDefaultEquipment(player);
  }


  // Get item from Chest - InventoryInteract
  // Craft item - InventoryInteract
  // Pickup item - PlayerPickup



  protected Bingo plugin;
  public Hunt(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
    timeLimit = 3 * 60 * 20;
  }
}
