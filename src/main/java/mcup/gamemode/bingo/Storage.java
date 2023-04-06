package mcup.gamemode.bingo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.HashMap;

public class Storage {

  public void readBingoItems() {
    
  }

  public void giveBingoMap(Player player) {
    ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
    MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();

    MapView mapView = Bukkit.createMap(player.getWorld());

    BingoMapRenderer mapRenderer = new BingoMapRenderer(plugin);

    mapView.getRenderers().clear();
    mapView.addRenderer(mapRenderer);

    mapMeta.setMapView(mapView);
    mapItem.setItemMeta(mapMeta);

    player.getInventory().addItem(mapItem);
  }

  private final Bingo plugin;
  public Storage(Bingo plugin_) {
    plugin = plugin_;
  }
}
