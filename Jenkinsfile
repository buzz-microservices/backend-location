pipeline {
  agent none
  options { 
    buildDiscarder(logRotator(numToKeepStr: '2'))
    skipDefaultCheckout true
  }
  stages {
        stage('Build') {
	 agent {
            kubernetes {
                label 'maven'
                yamlFile 'pod-templates/maven-pod.yaml'
                }
            }	
            steps {
              container('maven') {
                checkout scm
                sh 'mvn -B -DskipTests clean package'
                stash includes: 'target/*.jar', name: 'location'
		stash includes: 'Dockerfile', name: 'Dockerfile'
              }
	    }
        }
        stage('Test') { 
         agent {
            kubernetes {
                label 'maven'
                yamlFile 'pod-templates/maven-pod.yaml'
                }
            }
            steps {
              container('maven') {
                checkout scm
                sh 'mvn test'
              }
             script{
                   commitHash = sh(returnStdout: true, script: "git rev-parse HEAD | cut -c1-7 | tr -d '\n'")
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }
        stage('Build Docker Image') {
         agent {
            kubernetes {
                label 'kaniko'
                yamlFile 'pod-templates/kaniko-pod.yaml'
                }
            }
            steps {
              container(name: 'kaniko', shell: '/busybox/sh') {
                unstash 'location'
                unstash 'Dockerfile'
                withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
            	sh """#!/busybox/sh
            	executor -f ${pwd()}/Dockerfile -c ${pwd()} -d gcr.io/na-csa-msuarez/backend-location:${commitHash} -d gcr.io/na-csa-msuarez/backend-location:latest
                """
                   }
              }
            }
        }
        stage('Helm install') {
         agent {
            kubernetes {
                label 'helm-kubectl'
                yamlFile 'pod-templates/helm-kubectl-pod.yaml'
                }
            }
            steps {
                checkout scm
              container('helm-kubectl') {
		sh "helm package --version 0.0.1 --app-version ${commitHash} backend-location --debug"
		sh """ls backend-location && curl -L --data-binary "@backend-location-0.0.1.tgz" http://34.67.152.26:8080/api/charts"""
		sh "helm upgrade --name backend-location chartmuseum/backend-location -i --set image.tag=${commitHash}"
                   }
              }
            }
        
    }
}
