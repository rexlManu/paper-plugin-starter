package de.rexlmanu.paperpluginstarter;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import java.util.List;
import java.util.Map;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class StarterLoader implements PluginLoader {
  private static final List<String> DEPENDENCIES =
      List.of(
          "org.jetbrains:annotations:24.0.1",
          "org.incendo:cloud-paper:2.0.0-beta.2",
          "org.incendo:cloud-annotations:2.0.0-beta.2",
          "org.incendo:cloud-minecraft-extras:2.0.0-beta.2",
          "com.github.Exlll.ConfigLib:configlib-paper:v4.4.0",
          "com.google.inject:guice:7.0.0",
          "io.github.classgraph:classgraph:4.8.162");

  private static final Map<String, String> REPOSITORIES =
      Map.of(
          "paper", "https://repo.papermc.io/repository/maven-public/",
          "jitpack", "https://jitpack.io",
          "sonatype-snapshots", "https://oss.sonatype.org/content/repositories/snapshots");

  @Override
  public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
    MavenLibraryResolver resolver = new MavenLibraryResolver();

    DEPENDENCIES.stream()
        .map(s -> new Dependency(new DefaultArtifact(s), null))
        .forEach(resolver::addDependency);

    REPOSITORIES.entrySet().stream()
        .map(
            entry ->
                new RemoteRepository.Builder(entry.getKey(), "default", entry.getValue()).build())
        .forEach(resolver::addRepository);

    classpathBuilder.addLibrary(resolver);
  }
}
