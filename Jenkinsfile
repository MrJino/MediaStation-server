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
        sh 'yes'
      }
    }
    stage('deploy') {
      steps {
        sh 'scp -P 2002 /var/lib/jenkins/workspace/MediaStation-server_develop/build/libs/MediaStation.war for1self@for1self.iptime.org:/volume1/was/'
      }
    }
  }
}