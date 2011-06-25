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
  
  @Test
  public void found() {
    Version version = v("1");
    registerVersions("a", versions(version));
    when(repository.load(Mockito.eq("a"), Mockito.eq(version))).thenReturn(new Artifact("a", version));
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1,2)"))));
  }
  
  @Test
  public void foundPointVersion() {
    Version version = v("1.1");
    registerVersions("a", versions(version));
    when(repository.load(Mockito.eq("a"), Mockito.eq(version))).thenReturn(new Artifact("a", version));
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1.1,2)"))));
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
