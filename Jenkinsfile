pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    bat 'mvn clean package'
                }
            }
        }
        
        stage ('Update .env') {
            steps {
                script {
                    bat 'del .env'
                }

                script {
                    bat "echo CONTAINER_NAME=%city% >> .env" 
                }

                script {
                    bat "echo CONTAINER_PORT=%port% >> .env" 
                }
            }
        }
        stage('Build Docker image') {
            steps {
                script {
                    bat 'docker build -t booth-backend .'
                }
            }
        }
        stage ('Run Docker Compose') {
            steps {
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        bat 'rmdir /s /q .terraform'
                    }
                    
                }
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        bat 'del .terraform.lock.hcl'
                    }
                }
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        bat 'del terraform.tfstate'
                    }
                }
                script {
                    bat 'terraform init'
                }

                script {
                    bat 'terraform apply --var "city=%city%" --auto-approve'
                }
            }
        }
    }

}