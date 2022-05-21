env.APP_TYPE = "nodejs"

def call() {
  node {
    common.lintCheck()
    env.ARGS="-Dsonar.sources=."
    common.sonarCheck()
    common.testCheck()
  }
}
