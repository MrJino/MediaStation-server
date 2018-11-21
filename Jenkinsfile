pipeline {
  agent any
  stages {
    stage('initialize') {
      steps {
        git(url: 'https://github.com/MrJino/MediaStation-server.git', branch: 'develop', changelog: true, poll: true)
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
    stage('boot') {
      steps {
        sh 'ssh -p 2002 -o StrictHostKeyChecking=no -i /var/lib/jenkins/.ssh/id_rsa for1self@for1self.iptime.org < SpringBoot.sh'
      }
    }
  }
}