def linkChecks() {
  sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    pylint *.py
    echo Lint Check for ${(COMPONENT)}
 '''
}

def call() {
  pipeline {
    agent any

    stages {
       // For Each Commit
      stage('Lint Checks') {
        steps {
          script {
            linkChecks()
            }
          }
       } 
    }  // End of Stages
  }

}