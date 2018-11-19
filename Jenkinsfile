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
        sh './gradlew build'
      }
    }
    stage('deploy') {
      steps {
        sh 'scp -P 2002 -i /var/lib/jenkins/.ssh/id_rsa build/libs/MediaStation.war for1self@or1self.iptime.org:/volume1/was/'
      }
    }
  }
}