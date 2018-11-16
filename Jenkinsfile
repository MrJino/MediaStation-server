pipeline {
  agent any
  stages {
    stage('initialize') {
      parallel {
        stage('initialize') {
          steps {
            git(url: 'https://github.com/MrJino/MediaStation-server.git', branch: 'master', changelog: true, poll: true)
          }
        }
        stage('develop') {
          steps {
            git(url: 'https://github.com/MrJino/MediaStation-server.git', branch: 'develop', changelog: true, poll: true)
            build 'build'
          }
        }
      }
    }
  }
}