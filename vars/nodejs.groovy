

def call() {
  node {
    env.APP_TYPE = "nodejs"
    common.lintCheck()
    env.ARGS="-Dsonar.sources=."
    common.sonarCheck()
    common.testCheck()
  }
}
