package de.rexlmanu.paperpluginstarter;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class StarterLoader implements PluginLoader {
  @Override
  public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
    MavenLibraryResolver resolver = new MavenLibraryResolver();
    resolver.addDependency(
        new Dependency(new DefaultArtifact("org.jetbrains:annotations:24.0.1"), null));
    resolver.addDependency(
        new Dependency(new DefaultArtifact("cloud.commandframework:cloud-paper:1.8.3"), null));
    resolver.addDependency(
        new Dependency(
            new DefaultArtifact("com.github.Exlll.ConfigLib:configlib-paper:v4.2.0"), null));
    resolver.addDependency(
        new Dependency(new DefaultArtifact("com.google.inject:guice:7.0.0"), null));

    resolver.addRepository(
        new RemoteRepository.Builder(
            "paper", "default", "https://repo.papermc.io/repository/maven-public/")
            .build());
    resolver.addRepository(
        new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
    resolver.addRepository(
        new RemoteRepository.Builder(
            "sonatype-snapshots",
            "default",
            "https://oss.sonatype.org/content/repositories/snapshots")
            .build());

    classpathBuilder.addLibrary(resolver);
  }
}
