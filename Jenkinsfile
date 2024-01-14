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

        environment {
            CONTAINER_NAME='%city%'
            CONTAINER_PORT='%port%'
        }

        stage ('Run Docker image') {
            steps {
                script {
                    bat 'docker-compose -p %city% up -d '
                }
            }
        }
    }

}