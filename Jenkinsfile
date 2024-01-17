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

        stage('Build Docker image') {
            steps {
                script {
                    bat 'docker build -t booth-backend .'
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
        stage ('Run Docker Compose') {
            steps {
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        bat 'rmdir /s /q .terraform'
                    }
                    
                }
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        bat 'del .terraform.*'
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