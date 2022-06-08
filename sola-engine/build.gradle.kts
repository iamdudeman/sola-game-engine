plugins {
  id("sola.java-conventions")
}

dependencies {
  // TODO get from maven when it is published there
  api(files("libs/sola-json-1.0.2.jar"))
  api(files("libs/sola-ecs-2.0.0.jar"))
}
