package de.rexlmanu.boilerplate.scheduler;

import com.google.inject.AbstractModule;
import de.rexlmanu.boilerplate.scheduler.folia.FoliaScheduler;
import de.rexlmanu.boilerplate.scheduler.paper.PaperScheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class SchedulerModule extends AbstractModule {
  private final JavaPlugin plugin;

  @Override
  protected void configure() {
    if (UniversalScheduler.isFolia) {
      this.bind(TaskScheduler.class).toInstance(new FoliaScheduler(this.plugin));
    } else if (UniversalScheduler.isExpandedSchedulingAvailable) {
      this.bind(TaskScheduler.class).toInstance(new PaperScheduler(this.plugin));
    } else {
      this.bind(TaskScheduler.class)
          .toInstance(new de.rexlmanu.boilerplate.scheduler.bukkit.BukkitScheduler(this.plugin));
    }
  }
}
