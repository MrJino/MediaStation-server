pipeline {
  agent any
  stages {
    stage('initialize') {
      parallel {
        stage('initialize') {
          steps {
            git(url: 'https://github.com/MrJino/MediaStation-server.git', branch: 'develop', changelog: true, poll: true)
          }
        }
        stage('local-variable') {
          steps {
            sh 'cp ../local-variable/server/application-syn.properties ./src/main/resources/'
          }
        }
      }
    }
    stage('build') {
      steps {
        sh './gradlew clean'
        sh './gradlew build'
      }
    }
    stage('deploy') {
      steps {
        sh 'scp -P 2002 -o StrictHostKeyChecking=no -i /var/lib/jenkins/.ssh/id_rsa build/libs/MediaStation.jar for1self@for1self.iptime.org:/volume1/tomcat/bin'
      }
    }
  }
}