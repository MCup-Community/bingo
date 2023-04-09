package mcup.gamemode.bingo;

import mcup.core.local.data.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Storage {

  public void readBingoItems() {

    if (plugin.getConfig().getList("items") == null) {
      plugin.getLogger().warning("No items value was found in plugin config!");
      return;
    }

    items.clear();

    for (String itemName : plugin.getConfig().getStringList("items")) {

      try {
        Material itemMaterial = Material.getMaterial(itemName.toUpperCase());

        File textureFile = new File("./textures/" + itemName.toLowerCase() + ".png");
        BufferedImage image = ImageIO.read(textureFile);
        Image itemTexture = image.getScaledInstance(16, 16, Image.SCALE_DEFAULT);

        items.add(new BingoItem(itemMaterial, itemTexture));
        materialsSet.add(itemMaterial);
      }

      catch (Exception e) {
        e.printStackTrace();
        plugin.getLogger().warning("Unable to read item " + itemName + " from plugin config");
      }

    }

  }

  public void initCollectedItems() {

    for (Team team : plugin.core.apiManager.teamManager.getTeams().values())
      collectedItems.put(team.name, new HashSet<>());

  }

  public int getBingoGridSize() {
    return (int)Math.sqrt(items.size());
  }

  public void updateCollectedItems(Player player, Material material) {
    Team playerTeam = plugin.core.apiManager.teamManager.getTeamByPlayer(player.getName());

    if (playerTeam == null)
      return;

    if (!materialsSet.contains(material))
      return;

    if (collectedItems.get(playerTeam.name) == null) {
      plugin.getLogger().warning("Team " + playerTeam + " not initialized in collected items map!");
      return;
    }

    if (collectedItems.get(playerTeam.name).contains(material))
      return;

    int singleItemScoreValue = plugin.getConfig().getInt("singleItemScoreValue");
    collectedItems.get(playerTeam.name).add(material);

    for (mcup.core.local.data.Player teamPlayer : playerTeam.players)
      scheduledMapUpdate.add(teamPlayer.nickname);

    checkBingo(player, material);
    plugin.core.apiManager.scoreManager.addScorePlayer(player.getName(), singleItemScoreValue, "Собран предмет: " + material.name());


  }

  public void checkBingo(Player player, Material material) {

    Team playerTeam = plugin.core.apiManager.teamManager.getTeamByPlayer(player.getName());

    int index = -1;

    for (int i = 0; i < items.size(); i++)
      if (items.get(i).material == material)
        index = i;

    if (index == -1)
      return;

    int row = index / getBingoGridSize();
    int column = index % getBingoGridSize();

    int bingoScoreValue = plugin.getConfig().getInt("bingoScoreValue");

    boolean bingoCollected = false;

    if (checkHorizontalBingo(row, playerTeam)) {
      plugin.core.apiManager.scoreManager.addScorePlayer(player.getName(), bingoScoreValue, "Бинго по горизонтали!");
      bingoCollected = true;
    }


    if (checkVerticalBingo(column, playerTeam)) {
      plugin.core.apiManager.scoreManager.addScorePlayer(player.getName(), bingoScoreValue, "Бинго по вертикали!");
      bingoCollected = true;
    }


    if (checkDiagonalBingo(0, playerTeam)) {
      plugin.core.apiManager.scoreManager.addScorePlayer(player.getName(), bingoScoreValue, "Бинго по диагонали!");
      bingoCollected = true;
    }


    if (checkDiagonalBingo(1, playerTeam)) {
      plugin.core.apiManager.scoreManager.addScorePlayer(player.getName(), bingoScoreValue, "Бинго по диагонали!");
      bingoCollected = true;
    }

    if (bingoCollected) {
      plugin.core.apiManager.playerManager.sendTeamTitle(
        ChatColor.YELLOW + "Бинго!",
        "Отличная работа!",
        10,
        30,
        10,
        playerTeam
      );

      plugin.core.apiManager.playerManager.playTeamSound(Sound.ITEM_TRIDENT_THUNDER, 1.0f, playerTeam);
    }
  }

  private boolean checkHorizontalBingo(int row, Team team) {

    for (int i = 0; i < getBingoGridSize(); i++) {
      int index = row * getBingoGridSize() + i;

      if (!collectedItems.get(team.name).contains(items.get(index).material))
        return false;
    }

    return true;
  }

  private boolean checkVerticalBingo(int column, Team team) {

    for (int i = 0; i < getBingoGridSize(); i++) {
      int index = column + i * getBingoGridSize();

      if (!collectedItems.get(team.name).contains(items.get(index).material))
        return false;
    }

    return true;
  }

  private boolean checkDiagonalBingo(int corner, Team team) {

    if (corner == 0) { // Top left to bottom right

      for (int i = 0; i < getBingoGridSize(); i++) {
        int index = i * getBingoGridSize() + i;

        if (!collectedItems.get(team.name).contains(items.get(index).material))
          return false;
      }

    }

    else { // Top right to bottom left

      for (int i = 0; i < getBingoGridSize(); i++) {
        int index = i * getBingoGridSize() + (getBingoGridSize() - i - 1);

        if (!collectedItems.get(team.name).contains(items.get(index).material))
          return false;
      }

    }
    return true;

  }

  public HashMap<String, HashSet<Material>> collectedItems = new HashMap<>();

  public ArrayList<BingoItem> items = new ArrayList<>();
  public HashSet<Material> materialsSet = new HashSet<>();

  public HashSet <String> scheduledMapInit = new HashSet<>();

  public HashSet <String> scheduledMapUpdate = new HashSet<>();

  public void giveDefaultEquipment(Player player) {
    giveBingoMap(player);
  }

  public void giveBingoMap(Player player) {

    scheduledMapInit.add(player.getName());

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

  public void resetStorage() {
    items.clear();
    materialsSet.clear();
    collectedItems.clear();
    scheduledMapInit.clear();
  }

  private final Bingo plugin;
  public Storage(Bingo plugin_) {
    plugin = plugin_;
  }
}

class BingoItem {
  Material material;
  Image texture;

  BingoItem(Material material_, Image texture_) {
    material = material_;
    texture = texture_;
  }
}