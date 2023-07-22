pipeline {

    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'jenkins-deploy-branch', description: 'Branch used for production code')
        string(name: 'VAULT_TOKEN', defaultValue: 'x', description: 'VAULT_TOKEN')
    }


    stages {
        stage('Checkout') {
            steps {
                git branch: "${params.BRANCH}", url: 'https://github.com/tomdud-developer/speed-reading-app.git'
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

        stage('Stop Docker Container') {
            steps {
                sh 'docker stop speedreadingappbackend || true'
            }
        }

        stage('Remove Docker Container') {
            steps {
                sh 'docker rm speedreadingappbackend || true'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh "docker run -d -p 8081:8081 --name speedreadingappbackend -e VAULT_TOKEN=${params.VAULT_TOKEN} --restart=always speedreadingappbackend:latest"
            }
        }
    }
}
