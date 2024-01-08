pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN = credentials('mysql-root')
    }
	 stages {

        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.skip'
            }
        }

        stage('Packaging/Pushing imagae') {

            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
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

                sh "docker run --name demo-mysql --rm --network dev -v demo-cicd-mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=demo_cicd  -d mysql:8.0 "
                sh 'sleep 20'
                sh "docker exec -i demo-mysql mysql --user=root --password=${MYSQL_ROOT_LOGIN_PSW} < mysql_script"
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