plugins {
  // todo figure out how to publish this platform for sola game template to use
  // id("technology.sola.sola-publishing")

  id("technology.sola.plugins.sola-android")
}

dependencies {
  api(project(":sola-engine"))
}

solaAndroid {
  isLibrary = true
}

// todo
//solaPublishing {
//  artifactId = "platform-android"
//}

android {
  namespace = "technology.sola.engine.platform.android"
}

