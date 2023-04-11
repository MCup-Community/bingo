package mcup.gamemode.bingo.listeners;

import mcup.gamemode.bingo.Bingo;
import mcup.gamemode.bingo.stages.Hunt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

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
  public void onItemPickup(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player) {

      if (!(plugin.core.stageManager.getCurrentStage() instanceof Hunt))
        return;

      Player player = (Player) event.getEntity();
      Material material = event.getItem().getItemStack().getType();

      plugin.storage.updateCollectedItems(player, material);
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (!(plugin.core.stageManager.getCurrentStage() instanceof Hunt))
      return;

    plugin.storage.giveDefaultEquipment(event.getPlayer(), plugin.core.stageManager.getCurrentStage().timeLimit);
    plugin.storage.playersWithEquipment.add(event.getPlayer().getName());
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player)
      event.setCancelled(true);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (!(plugin.core.stageManager.getCurrentStage() instanceof Hunt))
      return;

    String replacement = plugin.getConfig().getString("dropReplace." + event.getBlock().getType().toString());

    if (replacement == null)
      return;

    Material replacementMaterial = Material.getMaterial(replacement);

    if (replacementMaterial == null)
      return;

    if (event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand()).isEmpty())
      return;

    event.setCancelled(true);
    event.getBlock().setType(Material.AIR);
    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(replacementMaterial));
  }
  protected final Bingo plugin;

  public PlayerListener(Bingo plugin_) {
    plugin = plugin_;
  }

}
