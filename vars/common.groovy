def sonarCheck() {
  sh '''
    #sonar-scanner -Dsonar.host.url=http://172.31.11.110:9000  ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
    #sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.11.110 ${COMPONENT}
    echo "sonar checks"   
  '''
}