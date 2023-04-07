package mcup.gamemode.bingo;

import mcup.core.local.data.Team;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;

public class BingoMapRenderer extends MapRenderer {
  @Override
  public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

    if (plugin.storage.scheduledMapInit.contains(player.getName())) {
      drawBorders(mapCanvas);
      drawItems(mapCanvas);
      drawMarks(mapCanvas, player);

      plugin.storage.scheduledMapInit.remove(player.getName());
    }

    Team playerTeam = plugin.core.apiManager.teamManager.getTeamByPlayer(player.getName());
    if (playerTeam != null && plugin.storage.scheduledMapUpdate.contains(player.getName())) {
      drawMarks(mapCanvas, player);
      plugin.storage.scheduledMapUpdate.remove(player.getName());
    }

  }

  private final int mapPadding = 4;
  private final int borderThickness = 1;
  private final int cellPadding = 3;

  private final int cellSize = 24;

  private final int gridWidth = 5;
  private final int gridHeight = 5;

  private final int mapSize = 128;

  private final int markMargin = -4;

  private void drawItems(MapCanvas mapCanvas) {

    int currentItemIndex = -1;

    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        currentItemIndex++;

        if (currentItemIndex >= plugin.storage.items.size())
          break;

        mapCanvas.drawImage(
          getCellCoordinate(i) + borderThickness + cellPadding,
          getCellCoordinate(j) + borderThickness + cellPadding,
          plugin.storage.items.get(currentItemIndex).texture
        );
      }
    }

  }

  private void drawMarks(MapCanvas mapCanvas, Player player) {

    Team playerTeam = plugin.core.apiManager.teamManager.getTeamByPlayer(player.getName());

    if (playerTeam == null || !plugin.storage.collectedItems.containsKey(playerTeam.name))
      return;

    int currentItemIndex = -1;

    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        currentItemIndex++;

        if (currentItemIndex >= plugin.storage.items.size())
          break;

        if (!plugin.storage.collectedItems.get(playerTeam.name).contains(plugin.storage.items.get(currentItemIndex).material))
          continue;

        drawTransparentImage(
          getCellCoordinate(i) + borderThickness + cellPadding + markMargin,
          getCellCoordinate(j) + borderThickness + cellPadding + markMargin,
          markImage,
          mapCanvas
        );


      }
    }
  }

  private void drawTransparentImage(int x, int y, Image image, MapCanvas mapCanvas) {

    byte[] bytes = MapPalette.imageToBytes(image);

    for(int x2 = 0; x2 < image.getWidth(null); ++x2) {
      for(int y2 = 0; y2 < image.getHeight(null); ++y2) {
        if (bytes[y2 * image.getWidth(null) + x2] != 0) {
          mapCanvas.setPixel(x + x2, y + y2, bytes[y2 * image.getWidth(null) + x2]);
        }
      }
    }

  }

  private void drawBorders(MapCanvas mapCanvas) {

    for (int i = mapPadding; i < mapSize - mapPadding; i++) {

      for (int j = 0; j < 4; j++) {

        mapCanvas.setPixelColor(
          getCellCoordinate(j + 1) - 1,
          i,
          Color.DARK_GRAY
        );


        mapCanvas.setPixelColor(
          getCellCoordinate(j + 1),
          i,
          Color.DARK_GRAY
        );


        mapCanvas.setPixelColor(
          i,
          getCellCoordinate(j + 1) - 1,
          Color.DARK_GRAY
        );


        mapCanvas.setPixelColor(
          i,
          getCellCoordinate(j + 1),
          Color.DARK_GRAY
        );
      }
    }
  }

  private int getCellCoordinate(int i) {
    return mapPadding + i * cellSize;
  }

  private final Bingo plugin;

  private Image markImage;

  public BingoMapRenderer(Bingo plugin_) {
    plugin = plugin_;

    try {
      File textureFile = new File("./textures/mark.png");
      BufferedImage image = ImageIO.read(textureFile);
      markImage = image.getScaledInstance(24, 24, Image.SCALE_DEFAULT);
    }

    catch (Exception e) {
      e.printStackTrace();
      plugin.getLogger().warning("Unable to load mark.png from textures folder!");
    }
  }
}
