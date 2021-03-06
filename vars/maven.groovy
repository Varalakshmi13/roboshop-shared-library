def call() {

  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/Varalakshmi13/${COMPONENT}"
    env.APP_TYPE = "maven"
    common.lintChecks()
    env.ARGS="-Dsonar.java.binaries=target/"
    common.sonarCheck()
    common.testCases()

    if (env.TAG_NAME != null) {
      common.artifacts()
    }
  }
}