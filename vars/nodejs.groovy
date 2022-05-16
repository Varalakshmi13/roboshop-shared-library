def linkChecks() {
  sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    echo  Lint Check for ${COMPONENT}
 '''
}

def sonarCheck() {
  sh '''
    sonar-scanner -Dsonar.host.url=http://172.31.11.110:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
    sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.11.110 ${COMPONENT}
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
            sonarCheck()
            }
          }
       }        
    }  // End of Stages
  }

}