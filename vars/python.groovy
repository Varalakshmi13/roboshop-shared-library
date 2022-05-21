def call() {

  node {
    git branch: 'main', url: "https://github.com/Varalakshmi13/${COMPONENT}"
    env.APP_TYPE = "python"
    common.lintChecks()
    env.ARGS="-Dsonar.sources=."
    common.sonarCheck()
    common.testCases()

    if (env.TAG_NAME != null) {
      common.artifacts()
    }
  }
}
