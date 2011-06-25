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
