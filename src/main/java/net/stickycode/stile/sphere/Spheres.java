/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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

import net.stickycode.stile.artifact.Sphere;

public enum Spheres
    implements Sphere {

  Main("Sphere for contributers to the primary artifact"),

  /**
   * For unittesting where the internals of a components algorithms should be verified.
   */
  Test("Sphere for unit testing the primary artifact"),

  /**
   * To mix is to combine to form a greater whole. This is the essence of integration testing in
   * that we combine all the parts together and see how it behaves as a system.
   */
  Mix("Sphere for integration testing the primary artifact"),

  /**
   * To mingle is to combine while maintaining individuality so is representative of component testing.
   *
   * When component testing we are trying to isolate the immediate component interactions.
   */
  Mingle("Sphere for component testing the primary artifact");

  private String description;

  private Spheres(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name().toLowerCase();
  }

}
