pipeline {
  agent {
    node {
      label 'maven'
    }
  }

    environment {
        DOCKER_CREDENTIAL_ID = 'docker-id'
        GITHUB_CREDENTIAL_ID = 'github-id'
        KUBECONFIG_CREDENTIAL_ID = 'kubeconfig'
        REGISTRY = 'docker.io'
        DOCKERHUB_NAMESPACE = 'nandonus'
        GITHUB_ACCOUNT = 'shenjiamingnus'
        APP_NAME = 'deal-hunter'
    }

    stages {
        stage ('checkout scm') {
            steps {
                checkout(scm)
            }
        }

//         stage ('unit test') {
//             steps {
//                 container ('maven') {
//                     sh 'mvn clean test'
//                 }
//             }
//         }

        stage ('build & push') {
            steps {
                container ('maven') {
                    sh 'mvn clean package -DskipTests'
                    sh 'cd dh-brand && podman build -f Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/dh-brand:SNAPSHOT-$BUILD_NUMBER .'
//                     sh 'podman build -f dh-email/Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/dh-email:SNAPSHOT-$BUILD_NUMBER .'
//                     sh 'podman build -f dh-product/Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/dh-product:SNAPSHOT-$BUILD_NUMBER .'
                    sh 'cd dh-gateway && podman build -f Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/dh-gateway:SNAPSHOT-$BUILD_NUMBER .'
                    sh 'cd dh-user && podman build -f Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/dh-user:SNAPSHOT-$BUILD_NUMBER .'
                    withCredentials([usernamePassword(passwordVariable : 'DOCKER_PASSWORD' ,usernameVariable : 'DOCKER_USERNAME' ,credentialsId : "$DOCKER_CREDENTIAL_ID" ,)]) {

                        sh 'echo "$DOCKER_PASSWORD" | podman login $REGISTRY -u "$DOCKER_USERNAME" --password-stdin'
                        sh 'podman push $REGISTRY/$DOCKERHUB_NAMESPACE/dh-brand:SNAPSHOT-$BUILD_NUMBER'
//                         sh 'podman push $REGISTRY/$DOCKERHUB_NAMESPACE/dh-email:SNAPSHOT-$BUILD_NUMBER'
//                         sh 'podman push $REGISTRY/$DOCKERHUB_NAMESPACE/dh-product:SNAPSHOT-$BUILD_NUMBER'
                        sh 'podman push $REGISTRY/$DOCKERHUB_NAMESPACE/dh-gateway:SNAPSHOT-$BUILD_NUMBER'
                        sh 'podman push  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-user:SNAPSHOT-$BUILD_NUMBER'
                    }
                }
            }
        }

        stage('push latest'){
           when{
             branch 'main'
           }
           steps{
                container ('maven') {
                  sh 'podman tag  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-brand:SNAPSHOT-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/dh-brand:latest '
//                   sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-email:SNAPSHOT-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/dh-email:latest '
//                   sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-product:SNAPSHOT-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/dh-product:latest '
                  sh 'podman tag $REGISTRY/$DOCKERHUB_NAMESPACE/dh-gateway:SNAPSHOT-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/dh-gateway:latest '
                  sh 'podman tag  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-user:SNAPSHOT-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/dh-user:latest '
                  sh 'podman push  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-brand:latest '
//                   sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-email:latest '
//                   sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-product:latest '
                  sh 'podman push $REGISTRY/$DOCKERHUB_NAMESPACE/dh-gateway:latest '
                  sh 'podman push  $REGISTRY/$DOCKERHUB_NAMESPACE/dh-user:latest '
                }
           }
        }

        stage('deploy to dev') {
          when{
            branch 'main'
          }
          steps {
            container ('maven') {
              withCredentials([
                kubeconfigFile(
                credentialsId: env.KUBECONFIG_CREDENTIAL_ID,
                variable: 'KUBECONFIG')
                ]) {
                sh 'envsubst < dh-brand/deploy/dh-brand-deploy.yaml | kubectl apply -f -'
                sh 'envsubst < dh-gateway/deploy/dh-gateway-deploy.yaml | kubectl apply -f -'
                sh 'envsubst < dh-user/deploy/dh-user-deploy.yaml | kubectl apply -f -'
//                 sh 'envsubst < dh-brand/deploy/dh-brand-deploy.yaml | kubectl apply -f -'
//                 sh 'envsubst < dh-brand/deploy/dh-brand-deploy.yaml | kubectl apply -f -'
              }
            }
          }
        }
    }
}