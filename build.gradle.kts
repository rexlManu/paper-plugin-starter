plugins {
	`java-library`
	alias(libs.plugins.spotless)
	alias(libs.plugins.lombok)
	alias(libs.plugins.runpaper)
	// alias(libs.plugins.userdev)
	alias(libs.plugins.shadow)
	alias(libs.plugins.paperyml)
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
	// If you need nms, you can uncomment the paperweight lines
	// This is not default because the compile time increases by a lot
	// paperweight.paperDevBundle(libs.versions.minecraft)
	compileOnly(libs.paper)

	library(libs.bundles.cloud)
	paperLibrary(libs.configlib)
	library(libs.guice)
	library(libs.classgraph)
	compileOnly(libs.invui)
}


spotless {
	java {
		target("**/*.java")
		googleJavaFormat(libs.versions.googlejavaformat.get())
		removeUnusedImports()
		formatAnnotations()
		trimTrailingWhitespace()
		endWithNewline()
	}
	format("misc") {
		target("*.gradle", "*.gradle.kts", "*.md", ".gitignore")

		trimTrailingWhitespace()
		indentWithTabs()
		endWithNewline()
	}
}

tasks {
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}
	processResources {
		filteringCharset = Charsets.UTF_8.name()
	}
	javadoc {
		options.encoding = Charsets.UTF_8.name()
	}
	assemble {
		// dependsOn(reobfJar)
	}
	jar {
		enabled = false
	}
	shadowJar {
		archiveClassifier.set("")
		relocate("org.bstats", "de.rexlmanu.paperpluginstarter.dependencies.bstats")
		from(file("LICENSE"))

		dependencies {
			exclude("META-INF/NOTICE")
			exclude("META-INF/maven/**")
			exclude("META-INF/versions/**")
			exclude("META-INF/**.kotlin_module")
		}
		minimize()
	}
	runServer {
		minecraftVersion(libs.versions.minecraftserver.get())
	}
}

paper {
	main = "de.rexlmanu.paperpluginstarter.StarterPlugin"
	loader = "de.rexlmanu.paperpluginstarter.StarterLoader"
	apiVersion = "1.19"
	author = "Emmanuel Lampe | rexlManu <mail@emmanuel-lampe.de>"
	generateLibrariesJson = true
}
