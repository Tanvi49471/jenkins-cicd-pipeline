
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Fetching source code from GitHub'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building Hotel Management Website'

                sh '''
                    test -f index.html
                    test -f style.css

                    mkdir -p dist
                    cp index.html style.css dist/
                '''
            }
        }

        stage('Test') {
            steps {
                echo 'Testing project files'

                sh '''
                    grep -qi '<html' index.html
                    grep -q 'stylesheet' index.html
                    test -s style.css

                    echo "All basic checks passed!"
                '''
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging website files'

                archiveArtifacts artifacts: 'dist/**',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Hotel website pipeline completed!'
        }

        failure {
            echo 'FAILED: Please check the Jenkins console output.'
        }
    }
}
