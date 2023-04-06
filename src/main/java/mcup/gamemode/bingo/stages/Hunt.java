package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Hunt extends GamemodeStage {

  @Override
  public void load() {
    super.load();

    initBossBarCountdown();

    Player player = Bukkit.getPlayer("GoracioNewport");

    plugin.storage.giveBingoMap(player);
  }





  protected Bingo plugin;
  public Hunt(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
    timeLimit = 3 * 60 * 20;
  }
}
