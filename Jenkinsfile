
pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    options {
        timestamps()
    }

    stages {
        stage('Verify Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build and Test') {
            steps {
                dir('backend') {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo 'VMS backend build and tests completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
