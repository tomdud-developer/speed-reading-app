pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/tomdud-developer/speed-reading-app.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t speedreadingappbackend:latest .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker run -d -p 8081:8080 speedreadingappbackend:latest'
            }
        }
    }
}
