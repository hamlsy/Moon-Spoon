#!/bin/bash
set -e

cd /home/ubuntu/git-action-test

# 환경 변수 파일 권한 설정
chmod 644 .env
chmod +x .env
# 환경 변수 설정
set -a
source .env
set +a


# 환경 변수 설정 (필요한 경우)
export DOCKER_COMPOSE_FILE=docker-compose.yml

#Docker 이미지 최신화
docker-compose build

# 기존 컨테이너 정리
docker-compose down --remove-orphans

# Docker 이미지 pull (만약 원격 레지스트리를 사용한다면)
docker-compose pull

# Vue.js 종속성 설치
#cd vue-cli
#npm install
#cd ..
update_nginx_config() {
    local active_container=$1
    sed -i '/upstream backend {/,/}/c\upstream backend {\n    server '"$active_container"':8080;\n}' nginx.conf
    docker-compose exec -T nginx nginx -s reload
}

# 컨테이너 시작
docker-compose up -d nginx
docker-compose up -d spring-blue

# Blue/Green 배포 로직
if [ "$(docker ps -q -f name=spring-container-blue)" ]; then
    # Blue가 실행 중이면 Green으로 배포
    echo "Deploying to Green"
    # spring-green = 서비스 이름, 컨테이너 이름이 아님
    docker-compose up -d --no-deps spring-green
    sleep 30  # 새 컨테이너가 완전히 시작될 때까지 대기

    update_nginx_config spring-green

    # Nginx 설정 업데이트 (Green으로 트래픽 전환)
    docker-compose exec -T nginx nginx -s reload

    # Blue 컨테이너 중지 및 제거
    docker-compose stop spring-blue
    docker-compose rm -f spring-blue
else
    # Green이 실행 중이거나 아무것도 실행 중이 아니면 Blue로 배포
    echo "Deploying to Blue"
    docker-compose up -d --no-deps spring-blue
    sleep 30  # 새 컨테이너가 완전히 시작될 때까지 대기

    update_nginx_config spring-blue

    # Nginx 설정 업데이트 (Blue로 트래픽 전환)
    docker-compose exec -T nginx nginx -s reload

    # Green 컨테이너 중지 및 제거 (실행 중인 경우)
    docker-compose stop spring-green
    docker-compose rm -f spring-green
fi

# 사용하지 않는 Docker 이미지 정리
docker image prune -af

echo "Deployment completed successfully"
