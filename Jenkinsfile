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
        stage ('Run Docker image') {
            steps {
                script {
                    bat 'docker-compose -p %city% up -d'
                }
            }
        }
    }

}