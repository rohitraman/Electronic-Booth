pipeline {
    agent any
    
    environment {
        CONTAINER_NAME='%city%'
        CONTAINER_PORT='%port%'
    }
    stages {
        stage('Print') {
            steps {
                script {
                    bat 'echo $CONTAINER_PORT'
                }
            }
        }
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

        stage ('Run Docker image') {
            steps {
                script {
                    bat 'docker-compose -p %city% up -d '
                }
            }
        }
    }

}