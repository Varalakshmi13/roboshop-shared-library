def call() {
  node {
    env.APP_TYPE = "nodejs"
    common.lintChecks()
    env.ARGS="-Dsonar.sources=."
    common.sonarCheck()
    common.testCheck()
    common.artifacts()

    if (env.TAG_NAME != null) {
      common.artifacts()
    }
  }
}
