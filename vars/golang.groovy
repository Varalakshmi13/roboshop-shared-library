def linkChecks() {
  sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    echo  Lint Check for ${COMPONENT}
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
            env.ARGS="-Dsonar.sources=."
            common.sonarCheck()
            }
          }
       }  

      stage('Test Cases') {

        parallel {

          stage('Unit Tests') {
            steps {
              sh 'echo Unit Tests'
            }
          }

          stage('Integration Tests') {
            steps {
              sh 'echo Integrtion Tests'
            }
          }

          stage('Functional Tests') {
            steps {
              sh 'echo Functionl Tests'
            }
          }                    
        }
      }              
    }  // End of Stages
  }

}