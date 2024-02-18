package de.rexlmanu.boilerplate.scheduler;

import de.rexlmanu.boilerplate.scheduler.bukkit.BukkitScheduler;
import de.rexlmanu.boilerplate.scheduler.folia.FoliaScheduler;
import de.rexlmanu.boilerplate.scheduler.paper.PaperScheduler;
import org.bukkit.plugin.Plugin;

public class UniversalScheduler {
  public static final boolean isFolia =
      classExists("io.papermc.paper.threadedregions.RegionizedServer");
  public static final boolean isExpandedSchedulingAvailable =
      classExists("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

  public static TaskScheduler getScheduler(Plugin plugin) {
    return isFolia
        ? new FoliaScheduler(plugin)
        : (isExpandedSchedulingAvailable
            ? new PaperScheduler(plugin)
            : new BukkitScheduler(plugin));
  }

  private static boolean classExists(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
