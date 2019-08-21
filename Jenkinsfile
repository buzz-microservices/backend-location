pipeline {
  agent none
  options { 
    buildDiscarder(logRotator(numToKeepStr: '5'))
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
		script{
		version = sh(returnStdout: true, script: "mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout")
 		}
		echo "App Version: ${version}"
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
            	executor -f ${pwd()}/Dockerfile -c ${pwd()} -d gcr.io/na-csa-msuarez/backend-location:${BUILD_NUMBER} -d gcr.io/na-csa-msuarez/backend-location:${version}-${commitHash} -d gcr.io/na-csa-msuarez/backend-location:latest
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
                sh "curl -X DELETE http://34.67.152.26:8080/api/charts/backend-location/0.0.1"
		sh "helm package --version 0.0.1 --app-version ${version} backend-location --debug --save=false"
		sh """curl -L --data-binary "@backend-location-0.0.1.tgz" http://34.67.152.26:8080/api/charts"""
		sh "helm init --client-only"
		sh "helm repo add chartmuseum http://34.67.152.26:8080"
                sh "helm upgrade backend-location chartmuseum/backend-location -i --namespace cje --set image.tag=${commitHash}"
                   }
              }
            }
        
    }
}
