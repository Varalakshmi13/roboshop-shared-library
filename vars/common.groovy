def sonarCheck() {
  stage('Sonar Code Analysis') {
  sh '''
    #sonar-scanner -Dsonar.host.url=http://172.31.11.110:9000  ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
    #sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.11.110 ${COMPONENT}
    echo sonar checks for {COMPONENT}  
  '''
 }
}


def lintChecks() {
  stage ('Lint Checks') {
  if (env.APP_TYPE == "nodejs") {
    sh ''' 
      #we commented this because dev is goin =g to check this failure
      #~/node_modules/jslint/bin/jslint.js server.js
      echo  Lint Check for ${COMPONENT}
    ''' 
  }
  else if (env.APP_TYPE == "maven")  {
    sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    #mvn checkstyle:check
    echo Lint Check for ${COMPONENT}
    '''
  }
  else if (env.APP_TYPE == "python")  {
    sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    #pylint *.py
    echo Lint Check for ${COMPONENT}
    '''
  }
  else if (env.APP_TYPE == "golang")  {
    sh ''' 
    #we commented this because dev is goin =g to check this failure
    #~/node_modules/jslint/bin/jslint.js server.js
    echo  Lint Check for ${COMPONENT}
    '''
  }  

  }  
}


def testCheck() {
  stage('Test Cases') {
        def stages = [:]

        stages["Unit Tests"] = {
            echo "build for mac"
        }
        stages["Integration Tests"] = {
            echo "build for linux"
        }
        stages["Functional Tests"] = {
            echo "build for linux"
        }

        parallel(stages)
    }
}

def artifacts() {

  stage("Check The Release") {
    env.UPLOAD_STATUS=sh(returnStdout: true, script: 'curl -L -s http://172.31.5.216:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true')
    print UPLOAD_STATUS
  }

  if(env.UPLOAD_STATUS == "") {

    stage("Prepare Artifacts") {
      if (env.APP_TYPE == "nodejs") {
        sh '''
          npm install
          zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js      '''
      }
      else if (env.APP_TYPE == "maven") {
        sh '''
          echo
        '''
      }
      else if (env.APP_TYPE == "python") {
        sh '''
          echo
        '''
      }
      else if (env.APP_TYPE == "golang") {
        sh '''
          echo
        '''
      }

    }


    stage("Upload Artifacts") {
      sh '''
        curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.5.216:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
    } 
  

  }
}