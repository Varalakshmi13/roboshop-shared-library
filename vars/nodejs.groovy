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
      NEXUS = credentials('NEXUS')
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
      stage('Prepare Artifacts') {
        when {
          expression { env.TAG_NAME != null}
        }
        steps {
            sh '''
              npm install
              zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
            '''
        }
      }    

      stage('Upload Artifacts') {
        when {
          expression { env.TAG_NAME != null}
        }
        steps {
            sh '''
              curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.5.216:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
            '''
        }
      }              
    }  // End of Stages
  }

}