pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN_PWD = credentials('mysql-root-pwd')
        DOCKER_HUB_LOGIN_PWD = credentials('docker-hub-pwd')
        DOCKER_REGISTRY_URL = 'docker.io'
    }
	 stages {

        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.skip'
            }
        }

        stage('Packaging and Pushing image') {
            steps {
                script{
                    // Set PATH explicitly
                    def dockerHome = tool 'docker'
                    env.PATH = "${dockerHome}/bin:${env.PATH}"

                    // Verify Docker version
                    sh 'docker --version'

                    sh 'echo "${DOCKER_HUB_LOGIN_PWD} | docker login -u hungltse04132@gmail.com --password-stdin"'
                    sh 'docker build -t hungltse04132/demo-cicd-springboot .'
                    sh 'docker login -u "hungltse04132@gmail.com" -p "${DOCKER_HUB_LOGIN_PWD}" ${DOCKER_REGISTRY_URL}'
                    sh 'docker push hungltse04132/demo-cicd-springboot'
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

                sh 'docker run --name demo-mysql --rm --network dev  --publish 3306:3306 -v demo-cicd-mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PWD} -e MYSQL_DATABASE=demo_cicd -d mysql:8.0 '
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull hungltse04132/demo-cicd-springboot'
                sh 'docker container stop demo-cicd-springboot || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '

                sh 'docker container run -d --rm --name demo-cicd-springboot -p 8081:8081 --network dev hungltse04132/demo-cicd-springboot'
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