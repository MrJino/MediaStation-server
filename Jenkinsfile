pipeline {
  agent any
  stages {
    stage('initialize') {
      steps {
        git(url: 'https://github.com/MrJino/MediaStation-server.git', branch: 'master', changelog: true, poll: true)
      }
    }
  }
}