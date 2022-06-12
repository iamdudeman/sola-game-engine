plugins {
  id("sola.java-conventions")
}

dependencies {
  // TODO get from maven when it is published there
  api(files("libs/sola-json-2.0.1.jar"))
  api(files("libs/sola-ecs-2.0.0.jar"))
}
