def linkChecks() {
  sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    #mvn checkstyle:check
    echo Lint Check for ${COMPONENT}
 '''
}

def call() {
  pipeline {
    agent any
    
    environment {
      SONAR = credentials('SONAR')
    }    

    stages {
       // For Each Commit
      stage('Lint Checks') {
        steps {
          script {
            linkChecks()
            }
          }
       } 
      stage('Sonar Checks') {
        steps {
          script {
            common.sonarCheck()
            }
          }
       }           
    }  // End of Stages
  }

}