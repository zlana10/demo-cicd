pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN = credentials('root-mysql-password')
        DOCKER_REGISTRY_CREDENTIALS = credentials('dockerhub')
        DOCKER_REGISTRY_URL = 'https://hub.docker.com'
    }
	 stages {

        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.skip'
            }
        }

        stage('Check Docker Installation') {
            steps {
                script {
                    // Check if Docker is installed
                    def dockerInstalled = tool 'docker'
                    if (dockerInstalled) {
                        echo "Docker is installed at: ${dockerInstalled}"
                    } else {
                        error "Docker not found. Please install Docker and configure it in Jenkins."
                    }
                }
            }
        }

        stage('Packaging and Pushing image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: DOCKER_REGISTRY_URL) {
                        sh 'docker build -t hunglt1312/demo-cicd-springboot .'
                        sh 'docker push hunglt1312/demo-cicd-springboot'
                }
            }
        }

         stage('Deploy MySQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull mysql:8.0'
                sh 'docker network create dev || echo "this network exists"'
                sh 'docker container stop demo-cicd-mysql || echo "this container does not exist" '
                sh 'echo y | docker container prune '
                sh 'docker volume rm demo-cicd-mysql || echo "no volume"'

                sh "docker run --name demo-cicd-mysql --rm --network dev -v demo-cicd-mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=demo_cicd  -d mysql:8.0 "
                sh 'sleep 20'
                sh "docker exec -i demo-cicd-mysql mysql --user=root --password=${MYSQL_ROOT_LOGIN_PSW} < mysql_script"
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull hunglt1312/demo-cicd-springboot'
                sh 'docker container stop demo-cicd-springboot || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '

                sh 'docker container run -d --rm --name demo-cicd-springboot -p 8081:8080 --network dev hunglt1312/demo-cicd-springboot'
            }
        }

    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}