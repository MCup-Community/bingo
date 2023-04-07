package mcup.gamemode.bingo.listeners;

import mcup.gamemode.bingo.Bingo;
import mcup.gamemode.bingo.stages.Hunt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerListener implements Listener {

  @EventHandler
  public void onInventoryInteract(InventoryClickEvent event) {

    if (!(plugin.core.stageManager.getCurrentStage() instanceof Hunt))
      return;

    Player player = (Player) event.getWhoClicked();
    Material material = event.getCurrentItem().getType();

    plugin.storage.updateCollectedItems(player, material);
  }

  @EventHandler
  public void on(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player) {

      if (!(plugin.core.stageManager.getCurrentStage() instanceof Hunt))
        return;

      Player player = (Player) event.getEntity();
      Material material = event.getItem().getItemStack().getType();

      plugin.storage.updateCollectedItems(player, material);
    }
  }

  protected final Bingo plugin;

  public PlayerListener(Bingo plugin_) {
    plugin = plugin_;
  }

}
