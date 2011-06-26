package net.stickycode.stile.sphere;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.stickycode.mockwire.Controlled;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;
import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.Version;
import net.stickycode.stile.version.component.ComponentVersionParser;
import net.stickycode.stile.version.range.ComponentVersionRangeParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

@RunWith(MockwireRunner.class)
public class LowestMatchingVersionDependencyResolverTest {

  @UnderTest
  LowestMatchingVersionDependencyResolver resolver;

  @UnderTest
  ComponentVersionRangeParser parser;

  @UnderTest
  ComponentVersionParser versionParser;

  @Controlled
  ArtifactRepository repository;

  @Test(expected = RuntimeException.class)
  public void notFound() {
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1,2)"))));
  }

  @Test(expected = RuntimeException.class)
  public void notFoundWithOneAbove() {
    registerVersions("a", versions("1.1"));
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1,2)"))));
  }

  @Test(expected = RuntimeException.class)
  public void notFoundWithOneBelow() {
    registerVersions("a", versions("0.9", "0.8"));
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1.1,2)"))));
  }

  @Test
  public void found() {
    Version version = v("1");
    registerVersions("a", versions(version));
    registerArtifact("a", version);
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1,2)"))));
  }

  @Test
  public void foundPointVersion() {
    Version version = v("1.1");
    String id = "a";
    registerVersions(id, versions(version));
    registerArtifact(id, version);
    resolver.resolve(Collections.singletonList(new Dependency(id, parser.parseVersionRange("[1.1,2)"))));
  }

  @Test
  public void conflict() {
    Version version = v("1.1");
    String id = "a";
    registerVersions(id, versions(version));
    registerArtifact(id, version);
    resolver.resolve(Collections.singletonList(new Dependency(id, parser.parseVersionRange("[1.1,2)"))));
  }

  private void registerArtifact(String id, Version version) {
    Artifact a = a(id, version);
    when(repository.load(Mockito.eq(id), Mockito.eq(version))).thenReturn(a);
  }

  private Artifact a(String id, Version version) {
    Artifact artifact = new Artifact(id, version);
    when(repository.load(Matchers.eq(id), Matchers.<Version> any())).thenReturn(artifact);
    return artifact;
  }

  private Artifact a(String id, String... dependencies) {
    Artifact artifact = a(id, v("1.1"));
    for (String dependency : dependencies) {
      artifact.addDependency(Spheres.Main, dependency(dependency));
    }
    return artifact;
  }

  private Dependency dependency(String id) {
    return new Dependency(id, parser.parseVersionRange("[1,2)"));
  }

  private void registerVersions(String id, List<Version> versions) {
    when(repository.lookupVersions(id)).thenReturn(versions);
  }

  private List<Version> versions(String... strings) {
    List<Version> versions = new ArrayList<Version>(strings.length);
    for (String version : strings) {
      versions.add(v(version));
    }
    return versions;
  }

  private List<Version> versions(Version... strings) {
    List<Version> versions = new ArrayList<Version>(strings.length);
    for (Version version : strings) {
      versions.add(version);
    }
    return versions;
  }

  private Version v(String version) {
    return versionParser.parse(version);
  }
}
