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

        stage('Package') {
            steps {
                sh './gradlew bootJar'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t speedreadingapp:latest .'
            }
        }

        stage('Run Docker Container') {
            steps {
                // Uruchom kontener z użyciem zbudowanego obrazu
                sh 'docker run -d -p 8080:8081 speedreadingapp:latest'
            }
        }
    }
}
