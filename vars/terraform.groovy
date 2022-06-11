def call() {
    properties([
        parameters([
            choice(choices: ['dev\nprod'], description: "Pick Env", name: "ENV"),
        ]),
    ])

    node {
        ansicolor('xterm') {
        sh 'rm -rf'
        git branch: 'main', url: "https://github.com/Varalakshmi13/${REPONAME}"

        stage('Terrafile INIT') {
            sh 'terrafile -f env-${ENV}/Terrafile'
        }

        stage('Terraform init') {
            sh 'terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars'
        }

        stage('Terraform Plan') {
            sh 'terraform plan -var-file=env-${ENV}/${ENV}.tfvars'
        }

        stage('Terraform Apply') {
            sh 'terraform apply -var-file=env-${ENV}/${ENV}.tfvars'
        }
    } 
}