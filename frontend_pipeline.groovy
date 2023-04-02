pipeline {
    agent any
    environment {
        VERSION = "${env.BUILD_ID}"
        IMAGE_REPO="nikhilyande/frontend"
    }
    stages {
        stage('git pull') {
            steps {
                //git 'https://github.com/nikhilyande/frontend.git'
                git credentialsId: 'github', url: 'https://github.com/nikhilyande/frontend.git'
            }
        }
        stage("create image and build app") {
            steps{
                //sudo docker build -t frontend:${VERSION} .
                sh '''
                sudo docker system prune -a -f
                sudo docker build -t ${IMAGE_REPO}:${VERSION} .
                sudo docker images
                '''
            }
        }
        stage("push image to repo") {
            steps{
                sh '''
                sudo docker push ${IMAGE_REPO}:${VERSION}
                sudo docker tag ${IMAGE_REPO}:${VERSION} ${IMAGE_REPO}:latest
                sudo docker push ${IMAGE_REPO}:latest
                
                '''
            }
        }
        stage("Push to Target VM"){
            steps{
                sshagent(['targetvm']) {
                    sh "ssh -o StrictHostKeyChecking=no -l ubuntu 13.232.190.224 sh ./fe_build.sh"
                }
            }
        }
        
        stage("K8 deployment"){
            steps{
                sshagent(['k8']) {
                    sh "scp -o StrictHostKeyChecking=no deploymentfront.yml ubuntu@43.205.96.28:/home/ubuntu"
                    script{
                        try{
                            sh "ssh ubuntu@43.205.96.28 sudo kubectl apply -f ."
                        }catch(error){
                            sh "ssh ubuntu@43.205.96.28 sudo kubectl create -f ."
                        }
                    }
                }
            }
        }   
    }
}