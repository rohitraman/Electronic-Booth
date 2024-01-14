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

        stage ('Run Docker image') {
            steps {
                script {
                    bat 'docker-compose up -d -p ${cityName}'
                }
            }
        }
    }

}