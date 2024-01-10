pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN_PSW = credentials('mysql-root')
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

        stage('Packaging and Pushing image') {
            steps {
                script{
                    // Set PATH explicitly
                    def dockerHome = tool 'docker'
                    env.PATH = "${dockerHome}/bin:${env.PATH}"

                    // Verify Docker version
                    sh 'docker --version'

                    sh 'echo "hungbeo003 | docker login -u hungltse04132@gmail.com --password-stdin"'
                    sh 'docker build -t hungltse04132/demo-cicd-springboot .'
                    sh 'docker login -u "hungltse04132@gmail.com" -p "hungbeo003" docker.io'
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

                sh 'docker run --name demo-mysql --rm --network dev -v demo-cicd-mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=demo_cicd  -d mysql:8.0 '
                sh 'sleep 20'
                def IP_ADDRESS = sh 'docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' demo-mysql'.trim()
                sh 'docker exec -i demo-mysql mysql -h ${ipAddress} -P 3306 --user=root --password=${MYSQL_ROOT_LOGIN_PSW} < mysql-script'
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull hungltse04132/demo-cicd-springboot'
                sh 'docker container stop demo-cicd-springboot || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '

                sh 'docker container run -d --rm --name demo-cicd-springboot -p 8081:8080 --network dev hungltse04132/demo-cicd-springboot'
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