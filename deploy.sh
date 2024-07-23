#!/bin/bash
set -e
cd /home/ubuntu/git-action-test

# 환경 변수 설정
export RDS_ENDPOINT="${RDS_ENDPOINT}"
export RDS_USERNAME="${RDS_USERNAME}"
export RDS_PASSWORD="${RDS_PASSWORD}"
export RDS_DB_NAME="${RDS_DB_NAME}"

# 환경 변수 설정 (필요한 경우)
export DOCKER_COMPOSE_FILE=docker-compose.yml

# Docker 이미지 pull (만약 원격 레지스트리를 사용한다면)
docker-compose pull

# Blue/Green 배포 로직
if [ "$(docker ps -q -f name=spring-container-blue)" ]; then
    # Blue가 실행 중이면 Green으로 배포
    echo "Deploying to Green"
    docker-compose up -d --no-deps spring-container-green vue-container-green
    sleep 30  # 새 컨테이너가 완전히 시작될 때까지 대기

    # Nginx 설정 업데이트 (Green으로 트래픽 전환)
    docker-compose exec -T nginx nginx -s reload

    # Blue 컨테이너 중지 및 제거
    docker-compose stop spring-container-blue vue-container-blue
    docker-compose rm -f spring-container-blue vue-container-blue
else
    # Green이 실행 중이거나 아무것도 실행 중이 아니면 Blue로 배포
    echo "Deploying to Blue"
    docker-compose up -d --no-deps spring-container-blue vue-container-blue
    sleep 30  # 새 컨테이너가 완전히 시작될 때까지 대기

    # Nginx 설정 업데이트 (Blue로 트래픽 전환)
    docker-compose exec -T nginx nginx -s reload

    # Green 컨테이너 중지 및 제거 (실행 중인 경우)
    docker-compose stop spring-container-green vue-container-green
    docker-compose rm -f spring-container-green vue-container-green
fi

# 사용하지 않는 Docker 이미지 정리
docker image prune -af

echo "Deployment completed successfully"