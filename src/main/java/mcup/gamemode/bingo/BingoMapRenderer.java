package mcup.gamemode.bingo;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class BingoMapRenderer extends MapRenderer {
  @Override
  public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

    File textureFile = new File("./textures/diamond.png");
    BufferedImage image = null;
    try {
      image = ImageIO.read(textureFile);
      Image texture = image.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
      mapCanvas.drawImage(16, 16, texture);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected Bingo plugin;

  public BingoMapRenderer(Bingo plugin_) {
    plugin = plugin_;
  }
}
