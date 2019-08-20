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
                script{
                def scmVars = checkout scm
                def commitHash = scmVars.GIT_COMMIT

                 }
//                checkout scm
		echo "pipeline GIT_COMMIT is  -- ${env.commitHash}"
                sh 'mvn test' 
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
            	executor -f ${pwd()}/Dockerfile -c ${pwd()} -d gcr.io/na-csa-msuarez/backend-location:${env.GIT_COMMIT} -d gcr.io/na-csa-msuarez/backend-location:latest
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
              container('helm-kubectl') {
		sh 'helm -h'
                   }
              }
            }
        
    }
}
