package de.rexlmanu.boilerplate.scheduler.folia;

import de.rexlmanu.boilerplate.scheduler.BaseScheduledTask;
import de.rexlmanu.boilerplate.scheduler.TaskScheduler;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaScheduler implements TaskScheduler {

  final Plugin plugin;

  public FoliaScheduler(Plugin plugin) {
    this.plugin = plugin;
  }

  private final RegionScheduler regionScheduler = Bukkit.getServer().getRegionScheduler();
  private final GlobalRegionScheduler globalRegionScheduler =
      Bukkit.getServer().getGlobalRegionScheduler();
  private final AsyncScheduler asyncScheduler = Bukkit.getServer().getAsyncScheduler();

  @SneakyThrows
  @Override
  public boolean isGlobalThread() {
    return (boolean)
        Bukkit.getServer().getClass().getMethod("isGlobalTickThread").invoke(Bukkit.getServer());
  }

  @Override
  public boolean isTickThread() {
    return Bukkit.getServer()
        .isPrimaryThread(); // The Paper implementation checks whether this is a tick thread, this
    // method exists to avoid confusion.
  }

  @Override
  public boolean isEntityThread(Entity entity) {
    return Bukkit.getServer().isOwnedByCurrentRegion(entity);
  }

  @Override
  public boolean isRegionThread(Location location) {
    return Bukkit.getServer().isOwnedByCurrentRegion(location);
  }

  @Override
  public BaseScheduledTask runTask(Runnable runnable) {
    return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
  }

  @Override
  public BaseScheduledTask runTaskLater(Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    if (delay <= 0) {
      return runTask(runnable);
    }
    return new FoliaScheduledTask(
        globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
  }

  @Override
  public BaseScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
  }

  @Override
  public BaseScheduledTask runTask(Plugin plugin, Runnable runnable) {
    return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
  }

  @Override
  public BaseScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    if (delay <= 0) {
      return runTask(plugin, runnable);
    }
    return new FoliaScheduledTask(
        globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
  }

  @Override
  public BaseScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
  }

  @Override
  public BaseScheduledTask runTask(Location location, Runnable runnable) {
    return new FoliaScheduledTask(regionScheduler.run(plugin, location, task -> runnable.run()));
  }

  @Override
  public BaseScheduledTask runTaskLater(Location location, Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    if (delay <= 0) {
      return runTask(runnable);
    }
    return new FoliaScheduledTask(
        regionScheduler.runDelayed(plugin, location, task -> runnable.run(), delay));
  }

  @Override
  public BaseScheduledTask runTaskTimer(
      Location location, Runnable runnable, long delay, long period) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        regionScheduler.runAtFixedRate(plugin, location, task -> runnable.run(), delay, period));
  }

  @Override
  public BaseScheduledTask runTask(Entity entity, Runnable runnable) {
    return new FoliaScheduledTask(entity.getScheduler().run(plugin, task -> runnable.run(), null));
  }

  @Override
  public BaseScheduledTask runTaskLater(Entity entity, Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    if (delay <= 0) {
      return runTask(entity, runnable);
    }
    return new FoliaScheduledTask(
        entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay));
  }

  @Override
  public BaseScheduledTask runTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), null, delay, period));
  }

  @Override
  public BaseScheduledTask runTaskAsynchronously(Runnable runnable) {
    return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
  }

  @Override
  public BaseScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        asyncScheduler.runDelayed(
            plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
  }

  @Override
  public BaseScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
    return new FoliaScheduledTask(
        asyncScheduler.runAtFixedRate(
            plugin, task -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS));
  }

  @Override
  public BaseScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
    return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
  }

  @Override
  public BaseScheduledTask runTaskLaterAsynchronously(
      Plugin plugin, Runnable runnable, long delay) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        asyncScheduler.runDelayed(
            plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
  }

  @Override
  public BaseScheduledTask runTaskTimerAsynchronously(
      Plugin plugin, Runnable runnable, long delay, long period) {
    // Folia exception: Delay ticks may not be <= 0
    delay = getOneIfNotPositive(delay);
    return new FoliaScheduledTask(
        asyncScheduler.runAtFixedRate(
            plugin, task -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS));
  }

  @Override
  public void execute(Runnable runnable) {
    globalRegionScheduler.execute(plugin, runnable);
  }

  @Override
  public void execute(Location location, Runnable runnable) {
    regionScheduler.execute(plugin, location, runnable);
  }

  @Override
  public void execute(Entity entity, Runnable runnable) {
    entity.getScheduler().execute(plugin, runnable, null, 1L);
  }

  @Override
  public void cancelTasks() {
    globalRegionScheduler.cancelTasks(plugin);
    asyncScheduler.cancelTasks(plugin);
  }

  @Override
  public void cancelTasks(Plugin plugin) {
    globalRegionScheduler.cancelTasks(plugin);
    asyncScheduler.cancelTasks(plugin);
  }

  private long getOneIfNotPositive(long x) {
    return x <= 0 ? 1L : x;
  }
}
