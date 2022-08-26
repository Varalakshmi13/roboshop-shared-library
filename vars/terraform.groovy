def call() {

    TERRAFORM_DIR = "null"
    if (TERRAFORM_DIR == "null") {
       env.TERRAFORM_DIR = "./"
    }
    properties([
        parameters([
            choice(choices: 'dev\nprod', description: "Pick Env", name: "ENV"),
        ]),
    ])

    node {
        ansiColor('xterm') {
          sh 'rm -rf'
          git branch: 'main', url: "https://github.com/Varalakshmi13/${REPONAME}"

          stage('Terrafile INIT') {
              sh '''
                cd ${TERRAFORM_DIR}
                terrafile -f env-${ENV}/Terrafile
                '''
          }

          stage('Terraform init') {
              sh '''
                cd ${TERRAFORM_DIR}
                terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars
                '''
          }

          stage('Terraform Plan') {
              sh '''
                cd ${TERRAFORM_DIR}
                terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                '''
          }

          stage('Terraform Apply') {
              sh '''
                cd ${TERRAFORM_DIR}
                terraform apply -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
                '''
          }
        } 

    }
}