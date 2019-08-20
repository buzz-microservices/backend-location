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
                stash includes: 'target/*.jar', name: 'location'
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
                label 'maven'
                yamlFile 'pod-templates/kaniko-pod.yaml'
                }
            }
            steps {
              container(name: 'kaniko', shell: '/busybox/sh') {
                unstash 'location'
                withEnv(['PATH+EXTRA=/busybox']) {
            	sh '''#!/busybox/sh
            	/kaniko/executor --context `pwd` --destination gcr.io/na-csa-msuarez/backend-location:latest
            	'''
                   }
              }
            }
        }
    }
}
