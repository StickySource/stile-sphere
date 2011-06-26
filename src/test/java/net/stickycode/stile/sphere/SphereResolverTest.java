/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.RedEngine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.stile.sphere;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

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

@RunWith(MockwireRunner.class)
public class SphereResolverTest {

  @UnderTest
  ComponentVersionParser parser;

  @UnderTest
  ComponentVersionRangeParser rangeParser;

  @UnderTest
  SphereResolver resolver;

  @UnderTest
  LowestMatchingVersionDependencyResolver dependencyResolver;
  
  @Controlled
  ArtifactRepository repository;

  @Test
  public void resolveSelf() {
    Artifact a = artifact("a");
    Classpath classpath = resolve(dependency("a"));
    assertThat(classpath).containsOnly(a);
  }

  @Test
  public void resolveDirectDependency() {
    Artifact a = artifact("a", "b");
    Artifact b = artifact("b");
    
    when(repository.lookupVersions("b")).thenReturn(Collections.singletonList(version()));
    
    Classpath classpath = resolve(dependency("a"));
    assertThat(classpath).containsOnly(a, b);
  }

  @Test
  public void resolveDirectDependencies() {
    Artifact a = artifact("a", "b", "c");
    Artifact b = artifact("b");
    Artifact c = artifact("c");

    when(repository.lookupVersions("b")).thenReturn(Collections.singletonList(version()));
    when(repository.lookupVersions("c")).thenReturn(Collections.singletonList(version()));

    Classpath classpath = resolve(dependency("a"));
    assertThat(classpath).containsOnly(a, b, c);
  }

  @Test
  public void resolveSingleTransitionDependencies() {
    Artifact a = artifact("a", "b");
    Artifact b = artifact("b", "c");
    Artifact c = artifact("c");
    
    when(repository.lookupVersions("b")).thenReturn(Collections.singletonList(version()));
    when(repository.lookupVersions("c")).thenReturn(Collections.singletonList(version()));

    Classpath classpath = resolve(dependency("a"));
    assertThat(classpath).containsOnly(a, b, c);
  }

  private Classpath resolve(Dependency artifact) {
    return resolver.resolveClasspath(Spheres.Main, artifact.getId(), version());
  }

  private Dependency dependency(String id) {
    return new Dependency(id, rangeParser.parseVersionRange("[1,2)"));
  }

  private Artifact artifact(String id) {
    Version version = version();
    Artifact artifact = new Artifact(id, version);
    when(repository.load(Matchers.eq(id), Matchers.<Version>any())).thenReturn(artifact);
    return artifact;
  }

  private Version version() {
    return parser.parse("1");
  }

  private Artifact artifact(String id, String... dependencies) {
    Artifact artifact = artifact(id);
    for (String dependency : dependencies) {
      artifact.addDependency(Spheres.Main, dependency(dependency));
    }
    return artifact;
  }

}
